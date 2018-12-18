package redisTest.contact;

import redis.clients.jedis.*;
import test.JedisPoolUntil;

import java.util.List;
import java.util.UUID;

/**
 * Created by 18435 on 2018/7/11.
 */
public class Semaphore {

    /**
     * 构建基本的计数信号量成
     * @param semname   资源名
     * @param limit     最多能够同时被访问的进程数
     * @param timeout   超时
     * @return
     */
    public String acquireSemaphore(String semname,int limit,int timeout){
        String identifier = UUID.randomUUID().toString();
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
        long now = System.currentTimeMillis();
        Pipeline pipeline = jedis.pipelined();
        //清理过期的信号量持有者
        pipeline.zremrangeByScore(semname,0,now - timeout*1000);
        pipeline.zadd(semname,now,identifier);
        Response<Long> rank=pipeline.zrank(semname, identifier);
        pipeline.syncAndReturnAll();
        if((long)rank.get() < limit){
            return identifier;
        }
        //获取信号量失败  删除之前添加的标识符
        jedis.zrem(semname,identifier);
        return null;
    }

    /**
     * 释放信号量
     * @param semname     资源名
     * @param identifier
     * @return
     */
    public boolean releaseSemaphore(String semname,String identifier){
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
        long zrem = jedis.zrem(semname,identifier);
        if (zrem > 0){//信号量已经被正确地释放
            return true;
        }
        return false; //表示该信号量已经因为过期而被删除了
    }

    /**
     * 构建公平的计数信号量
     * 一个超时有序集合   一个信号量拥有者有序集合   一个计数器
     * @param semname         资源名
     * @param limit           最多能够同时被访问的进程数
     * @param timeout         超时
     * @return
     */
    public String acquireFailSemaphore(String semname,int limit,int timeout){
        String identifier = UUID.randomUUID().toString();
        String czset = semname + ":owner"; //信号量拥有者有序集合
        String ctr = semname + ":counter"; //计数器
        long now = System.currentTimeMillis();
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
        Transaction transaction = jedis.multi();
        //清除过期的信号量
        transaction.zremrangeByScore(semname,0,now - timeout*1000);
        //更新信号量拥有者有序集合  将已超时的信号量从信号量拥有者有序集合中移除
        ZParams zParams = new ZParams();
        zParams.weightsByDouble(0,1);
        transaction.zinterstore(czset,zParams,semname,czset);
        transaction.incr(ctr);
        List<Object> result = transaction.exec();
        int counter = ((Long)result.get(result.size()-1)).intValue(); //对计数器执行自增操作，并获取自增后的值

        transaction = jedis.multi();
        transaction.zadd(semname,now,identifier);
        transaction.zadd(czset,counter,identifier);
        transaction.zrank(czset,identifier);
        result = transaction.exec();
        int rank = ((Long)result.get(result.size()-1)).intValue();
        if(rank < limit){//获取信号量成功
            return identifier;
        }
        //获取失败，清理无用数据
        transaction = jedis.multi();
        transaction.zrem(semname,identifier);
        transaction.zrem(czset,identifier);
        transaction.exec();
        return null;
    }

    /**
     * 释放公平信号量
     * @param semname
     * @param identifier
     * @return
     */
    public boolean releaseFairSemaphore(String semname,String identifier){
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
        String czset = semname + ":owner"; //信号量拥有者有序集合
        Transaction transaction = jedis.multi();
        transaction.zrem(semname,identifier);
        transaction.zrem(czset,identifier);
        List<Object> result = transaction.exec();
        return (long)result.get(result.size()-1) == 1;
    }

    /**
     * 刷新信号量
     * zadd命令返回值 被成功添加的新成员数量 不包括那些被更新的，已经存在的成员
     * @param semname
     * @param identifier
     * @return
     */
    public boolean refreshFairSemaphore(String semname,String identifier){
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
        long result = jedis.zadd(semname,System.currentTimeMillis(),identifier);//如果本来就存在 则分数被更新
        if(result == 1){//表示在执行之前已经被过期处理掉了，这里是新添加了一个成员，所以需要将其移除
            // 如果是新增的，那么证明信号量已经过期了  则释放信号量
            this.releaseFairSemaphore(semname,identifier);
            return false;
        }
        return true;
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
        return null;
    }

    /**
     * 释放锁
     * @param name
     * @param identifier
     * @return
     */
    public boolean releaseLock(String name,String identifier){
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
     * 对获取公平信号量加锁 消除竞争条件
     * @param semname
     * @param limit
     * @param timeout
     */
    public String acquireSemaphorewithLock(String semname,int limit,int timeout){
        String identifier = this.acquireLockWithTimeout(semname,10,timeout);
        if(identifier!=null){
            try{
                return  this.acquireFailSemaphore(semname,limit,timeout);
            }finally {
                this.releaseLock(semname,identifier);
            }
        }
        return null;
    }
}
