package test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by 18435 on 2018/6/22.
 */
public class JedisPoolUntil {
    private static  volatile JedisPool pool = null;
    private JedisPoolUntil(){}

    public static  JedisPool getJedisPoolInstance(){
        if(pool==null){
            synchronized (JedisPoolUntil.class){
                if(pool == null){
                    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
                    jedisPoolConfig.setMaxTotal(1000);
                    jedisPoolConfig.setMaxIdle(32);
                    jedisPoolConfig.setMaxWaitMillis(1000*100);
                    jedisPoolConfig.setTestOnBorrow(true);//连通性测试
                    pool = new JedisPool(jedisPoolConfig,"127.0.0.1",6379);
                }
            }
        }
        return pool;
    }

    public static void release(JedisPool jedisPool, Jedis jedis){
        if(jedis !=null){
            jedisPool.returnResourceObject(jedis);
        }
    }






}
