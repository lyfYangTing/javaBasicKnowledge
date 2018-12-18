package test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by 18435 on 2018/6/22.
 */
public class TestPool {
    public static void main(String[] args) {
        JedisPool jedisPool = JedisPoolUntil.getJedisPoolInstance();
        JedisPool jedisPool1 = JedisPoolUntil.getJedisPoolInstance();
        System.out.println(jedisPool == jedisPool1);//单例  true

        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.set("aa","bb");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JedisPoolUntil.release(jedisPool,jedis);//释放
        }

    }
}
