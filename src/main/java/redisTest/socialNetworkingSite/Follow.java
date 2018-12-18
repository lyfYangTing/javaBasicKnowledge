package redisTest.socialNetworkingSite;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Tuple;
import test.JedisPoolUntil;

import java.util.*;

/**
 * Created by 18435 on 2018/7/24.
 * 关注者集合 (自己被哪些用户关注)      用户id       被某人关注时的时间戳
 * 正在关注集合 （自己关注了哪些用户）     用户id    开始关注某人时的时间戳
 */
public class Follow {

    private final int HOME_TIMELINE_SIZE = 1000;

    /**
     * 关注操作
     *
     * @param uid
     * @param other_uid
     * @return
     */
    public boolean followUser(String uid, String other_uid) {
        Jedis conn = JedisPoolUntil.getJedisPoolInstance().getResource();

        String fkey1 = "following:" + uid;   //指定用户 正在关注集合
        String fkey2 = "followers:" + other_uid;// 被关注用户   关注者集合

        if (StringUtils.isNotEmpty(String.valueOf(conn.zscore(fkey1, other_uid)))) {//已经关注了 不能重复关注
            return false;
        }
        ;
        Pipeline pipeline = conn.pipelined();

        long now = System.currentTimeMillis();
        pipeline.zadd(fkey1, now, other_uid);
        pipeline.zadd(fkey2, now, uid);
        pipeline.zrevrangeWithScores("profile:" + other_uid, 0, HOME_TIMELINE_SIZE - 1);
        List<Object> result = pipeline.exec().get();

        pipeline.hincrBy("user:" + uid, "following", (Integer) result.get(result.size() - 3));
        pipeline.hincrBy("user:" + other_uid, "followers", (Integer) result.get(result.size() - 2));

        if (result.get(result.size() - 1) != null) {
            Set<Tuple> set = (Set<Tuple>) result.get(result.size() - 1);
            Map<String, Double> map = new HashedMap();
            set.forEach(tuple -> map.put(tuple.getElement(), tuple.getScore()));
            conn.zadd("home:" + uid, map);
        }

        pipeline.zremrangeByRank("home:" + uid, 0, -HOME_TIMELINE_SIZE);//对执行关注操作的用户的组主页时间线进行更新，并保留时间线上面最新的1000条状态消息
        pipeline.exec();
        return true;
    }

    /**
     * 取消关注操作
     *
     * @param uid
     * @param other_uid
     * @return
     */
    public boolean unFollowUser(String uid, String other_uid) {
        Jedis conn = JedisPoolUntil.getJedisPoolInstance().getResource();

        String fkey1 = "following:" + uid;   //指定用户 正在关注集合
        String fkey2 = "followers:" + other_uid;// 被关注用户   关注者集合

        if (StringUtils.isEmpty(String.valueOf(conn.zscore(fkey1, other_uid)))) {//没有关注，所以不能取消
            return false;
        }

        Pipeline pipeline = conn.pipelined();

        pipeline.zrem(fkey1,other_uid);
        pipeline.zrem(fkey2,uid);
        pipeline.zrevrangeWithScores("profile:" + other_uid, 0, HOME_TIMELINE_SIZE - 1);
        List<Object> result = pipeline.exec().get();

        pipeline.hincrBy("user:" + uid, "following", -(Integer) result.get(result.size() - 3));
        pipeline.hincrBy("user:" + other_uid, "followers", -(Integer) result.get(result.size() - 2));

        if (result.get(result.size() - 1) != null) {//移除被取消关注的用户发布的所有状态信息
            Set<Tuple> set = (Set<Tuple>) result.get(result.size() - 1);
            set.forEach(tuple -> pipeline.zrem("home:"+uid,tuple.getElement()));
        }

        pipeline.exec();
        return true;
    }
}
