package redisTest.contact;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import test.JedisPoolUntil;

import java.util.UUID;

/**
 * Created by 18435 on 2018/7/11.
 *
 */
public class RedisLockTest {

    /**
     * 获取锁  执行命令setnx(set if not exist) 如果不存在，则执行set，返回1    如果已存在，不做任何操作，返回0
     * 可以达到的效果   正确地实现基本的加锁功能
     * @param lockName
     * @param acquireTimeout  如果程序在获取锁的时候失败，会不断进行尝试
     * @return
     */
    public static String acquireLock(String lockName , int acquireTimeout){
        String identifier = UUID.randomUUID().toString();//用这个值来防止
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
        long endTime = System.currentTimeMillis() + acquireTimeout * 1000;
        while(System.currentTimeMillis()<endTime){
            String result = jedis.set("lock:"+lockName,identifier,"NX");
            //函数 设置成功返回OK,失败返回null
            if("OK".equals(result)){
                return identifier;
            }
            try {
                Thread.sleep(1000); //休眠1秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 释放锁
     * @param name
     * @param identifier
     * @return
     */
    public static boolean releaseLock(String name,String identifier){
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
        Pipeline pipeline = jedis.pipelined();
        String lockName = "lock:"+ name;
        while (true){
            try{
                pipeline.watch(lockName);
                if(pipeline.get(lockName).equals(identifier)){
                    pipeline.multi();
                    pipeline.del(lockName);
                    pipeline.exec();
                    return true;
                }
                break;
            }catch (Exception e) {//有其他客户端修改了锁，重试
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 加锁的时候  给锁加上超时限制特性   解决因为持有者崩溃而无法释放的锁
     * 这个特性确保了锁总会在有需要的时候被释放，而不会被某个客户端一直把持着
     *
     * String set(String key, String value, String nxxx, String expx, long time);
     * 该方法是： 存储数据到缓存中，并制定过期时间和当Key存在时是否覆盖。
     * nxxx： 只能取NX或者XX，如果取NX，则只有当key不存在是才进行set，如果取XX，则只有当key已经存在时才进行set
     * expx： 只能取EX或者PX，代表数据过期时间的单位，EX代表秒，PX代表毫秒。
     * time： 过期时间，单位是expx所代表的单位。
     * @param name
     * @param acquireTimeout
     * @param lockTimeout
     * @return
     */
    public String acquireLockWithTimeout(String name,int acquireTimeout,int lockTimeout){
        String identifier = UUID.randomUUID().toString();//用这个值来防止
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
        long endTime = System.currentTimeMillis() + acquireTimeout;
        while(System.currentTimeMillis()<endTime){
            String result = jedis.set("lock:"+name,identifier,"NX","EX",lockTimeout);
            //函数 设置成功返回OK,失败返回null
            if("OK".equals(result)){
                return identifier;
            }
            try {
                Thread.sleep(1); //休眠1毫秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
