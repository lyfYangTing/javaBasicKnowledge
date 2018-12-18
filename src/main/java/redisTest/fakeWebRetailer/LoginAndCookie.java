package redisTest.fakeWebRetailer;

import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import test.JedisPoolUntil;

/**
 * Created by 18435 on 2018/6/27.
 * 登录和缓存功能
 */
public class LoginAndCookie {

    /**
     * 更新令牌cookie：token
     *
     * @param token
     * @param userId
     */
    public void updateToken(String token, String userId, String itemId) {
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
        Transaction transaction = jedis.multi();
        long updateTime = System.currentTimeMillis() / 1000;
        //String hkey = "login:" + token; 多次一举  内部键token每个用户的都不一样，当然不会被覆盖掉了
        transaction.hset("login:", token, userId);//对用户存储在登录散列里的信息进行更新   维持令牌与已登陆用户之间的映射
        transaction.zadd("recent:", updateTime, token);//最近登录用户集合

        if (StringUtils.isNotEmpty(itemId)) {
            //添加到  用户浏览商品列表中
            transaction.zadd("viewed:" + token, updateTime, itemId);
            transaction.zremrangeByRank("viewed:" + token, 0, -26);
            transaction.zincrby("viewed:",-1,itemId);//商品浏览次数排行榜  浏览一次减1是由于，分数越小，排序越靠前，要将被浏览次数最多的商品放到第一个位置，就需要不断自减
        }
        transaction.exec();
    }

    /**
     * 获取令牌对应用户  ：检查用户是否登录
     *
     * @param token
     * @return
     */
    public String getTokenUser(String token) {
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
        return jedis.hget("login:", token);
    }
}

