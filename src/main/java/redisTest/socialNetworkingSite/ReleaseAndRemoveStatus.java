package redisTest.socialNetworkingSite;

import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Tuple;
import test.JedisPoolUntil;

import java.util.Map;
import java.util.Set;

/**
 * Created by 18435 on 2018/7/25.
 * 状态消息的发布与删除
 *
 * 对用户的个人时间线进行更新
 * 对关注者的主页时间线进行更新
 */
public class ReleaseAndRemoveStatus {

    private final int POSTS_PER_PASS = 1000;
    private final int HOME_TIMELINE_SIZE = 1000;

    /**
     * 状态消息的发布：
     * 1.创建一条状态消息
     * 2.对用户的个人时间线进行更新
     * 3.对关注者的主页时间线进行更新
     * @param uid   用户Id
     * @param message  消息
     * @param data   状态消息体
     * @return
     */
    public Long updatePostStatus(String uid,String message,Map<String, String> data){
        Jedis conn = JedisPoolUntil.getJedisPoolInstance().getResource();
        Long sid = UserAndStatus.createStatus(uid,message,data);
        if(sid == null){//没有创建成功状态消息
            return null;
        }

        String posted = conn.hget("status:"+sid,"posted");
        if(StringUtils.isEmpty(posted)){//没有获取到状态消息的创建时间
            return null;
        }

        //将状态信息添加到个人时间线中
        conn.zadd("profile:"+uid,Double.valueOf(posted),sid.toString());

        //将状态信息添加到所有关注者主页时间线中
        this.syndicateStatus(uid,sid.toString(),Double.valueOf(posted),0);
        return sid;
    }

    /**
     * 将状态消息添加到所有关注者主页时间线中:函数每次被调用时，最多只会将状态消息发送给1000个关注者
     *
     * 用户的关注者数量较少时（少于1000）：立即更新每个关注者的主页时间线
     * 用户的关注者数量较多：先更新前1000个，剩下的通过延迟任务更新
     *
     * @param uid
     * @param sid
     * @param posted
     */
    public void syndicateStatus(String uid,String sid,double posted,double start){

        Jedis conn = JedisPoolUntil.getJedisPoolInstance().getResource();

        //以上次被更新最后一个关注者为起点，获取接下来的1000个关注者  start在不断变化
        Set<Tuple> set = conn.zrangeByScoreWithScores("followers:"+uid,String.valueOf(start),"inf",0,POSTS_PER_PASS);

        Pipeline pipeline = conn.pipelined();
        for(Tuple tuple:set){
            //发送状态信息给关注者
            pipeline.zadd("home:"+ tuple.getElement(),posted,sid);
            //更新start值
            start = tuple.getScore();
            //对关注者的主页时间线进行修剪 防止它超过限定的最大长度
            pipeline.zremrangeByRank("home:"+tuple.getElement(),0,-HOME_TIMELINE_SIZE-1);
        }

        if(set.size() >= POSTS_PER_PASS){//如果需要更新的关注者数量超过POSTS_PER_PASS，那么延迟任务里面继续执行剩余的更新操作
            //execcute_later(conn,"default","syndicateStatus",[conn,uid,post,start])
        }
    }
}
