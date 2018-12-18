package redisTest.socialNetworkingSite;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redisTest.contact.RedisLockTest;
import test.JedisPoolUntil;

import java.util.Map;
import java.util.Set;

/**
 * Created by 18435 on 2018/7/24.
 * 创建新的用户（用户相关信息存储在  散列中）
 * 创建状态消息 （散列）
 */
public class UserAndStatus {

    /**
     * 创建用户
     *
     * @param login    登录用户
     * @param userName 用户名
     */
    public Long createUser(String login, String userName) {
        Jedis conn = JedisPoolUntil.getJedisPoolInstance().getResource();
        String loginName = login.toLowerCase();
        //加锁  如果加锁不成功 说明给定的用户名已经被其他用户占用了   并发控制
        String lock = RedisLockTest.acquireLock("registered:" + loginName, 1);
        if (StringUtils.isEmpty(lock)) {
            return null;
        }
        String loginId = conn.hget("users:", loginName);
        //key : users:    hash结构，存储的是 loginName与用户Id的映射   如果给定的用户名已经被映射到了某个用户id，那么程序就不再将这个用户名分配给其他人
        if (StringUtils.isNotEmpty(loginId)) {
            RedisLockTest.releaseLock("registered:" + loginName, lock);
            return null;
        }
        Long id = conn.incr("user:id:");
        Pipeline pipeline = conn.pipelined();
        Map<String, String> dataMap = new HashedMap();
        dataMap.put("login", login);
        dataMap.put("id", id.toString());
        dataMap.put("name", userName);
        dataMap.put("follows", "0");
        dataMap.put("following", "0");
        dataMap.put("posts", "0");
        dataMap.put("signup", String.valueOf(System.currentTimeMillis()));

        pipeline.hmset("user:" + id, dataMap);
        pipeline.hset("users:", loginName, String.valueOf(id));
        pipeline.exec();
        RedisLockTest.releaseLock("registered:" + loginName, lock);//释放对用户名加的锁
        return id;
    }

    /**
     * 创建状态消息散列
     *
     * @param uid     用户id
     * @param message
     * @param data
     * @return
     */
    public static Long createStatus(String uid, String message, Map<String, String> data) {
        Jedis conn = JedisPoolUntil.getJedisPoolInstance().getResource();
        //先验证用户账号是否存在
        String login = conn.hget("user:" + uid, "login");
        if (StringUtils.isEmpty(login)) {
            return null;
        }
        //为这条状态消息创建一个ID
        Long sid = conn.incr("status:id:");

        Pipeline pipeline = conn.pipelined();
        data.put("message", message);
        data.put("posted", String.valueOf(System.currentTimeMillis()));
        data.put("id", sid.toString());
        data.put("uid", uid);
        data.put("login", login);

        pipeline.hmset("status:" + sid, data);
        pipeline.hincrBy("user:" + uid, "posts", 1);

        pipeline.exec();
        return sid;
    }

    /**
     * 从时间线里面获取给定页数的最新状态消息
     * 主页时间线将成双成对的状态信息Id（用于获取状态信息本身） 和时间戳(用于排序)记录到有序集合中
     *
     * 主页时间线(home:)：包含用户以及用户正在关注的人所发布的状态消息组成
     * 个人时间线(profile:)：只包含用户自己发布的状态消息
     * @param uid        用户id
     * @param timeline   获取哪条时间线 例如：home:
     * @param page       获取第page页时间线
     * @param count      每页要有多少条状态消息
     * @return
     */
    public Map<String,Map<String,String>> getStatusMessage(String uid,String timeline,int page,int count){
        Jedis conn = JedisPoolUntil.getJedisPoolInstance().getResource();
        Set<String> sids = conn.zrevrange(timeline+uid,(page-1)*count,page*count - 1 );
        Map<String,Map<String,String>> result = new HashedMap();
        sids.forEach( sid -> {//过滤掉已经被删除了的状态消息
            Map<String,String> statusMessage = conn.hgetAll("status:"+sid);
            if(!statusMessage.isEmpty()){
                result.put(sid,statusMessage);
            }else{
                conn.zrem("home:"+ uid,sid);//将已经被删除了的状态消息从用户主页时间线移除
            }
        });
        return result;
    }

    /**
     * 删除已发布的状态消息
     * @param uid     用户id
     * @param sid  状态消息id
     * @return
     */
    public boolean deleteStatus(String uid,String sid){
        String key = "status:"+ sid;

        Jedis conn = JedisPoolUntil.getJedisPoolInstance().getResource();
        Map<String,String> statusMessage = conn.hgetAll(key);
        if(statusMessage.isEmpty()){//消息已被删除
            return false;
        }

        Pipeline pipeline = conn.pipelined();
        String lock = RedisLockTest.acquireLock(key,5);//加锁  防止两个程序同时删除同一条状态消息的情况出现
        if(StringUtils.isEmpty(lock)){
            return false;
        }
        if(!uid.equals(statusMessage.get("uid"))){//如果uid指定的用户并非状态信息的发布人，那么函数直接返回，不能被删除
            RedisLockTest.releaseLock(key,lock);
            return false;
        }
        //删除状态消息
        pipeline.del("status:"+sid);

        //从用户的主页时间线和个人时间线里移除该状态信息
        pipeline.zrem("profile:"+uid,sid);
        pipeline.zrem("home:"+uid,sid);

        //用户已经发布的状态消息的数量减1
        pipeline.hincrBy("user:"+uid,"posts",-1);
        pipeline.exec();
        RedisLockTest.releaseLock(key,lock);
        return true;
    }
}
