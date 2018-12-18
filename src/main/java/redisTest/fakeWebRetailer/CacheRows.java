package redisTest.fakeWebRetailer;

import org.apache.commons.collections.map.HashedMap;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import test.JedisPoolUntil;

import java.util.Map;

/**
 * Created by 18435 on 2018/6/28.
 * 数据行缓存
 */
public class CacheRows {

    private static Map<String,Good> map  = new HashedMap();

    static {
        Good good = new Good("1",2);
        Good good1 = new Good("2",3);
        map.put(good.getGoodId(),good);
        map.put(good.getGoodId(),good1);
    }

    /**
     * 数据行缓存调度
     * @param rowId
     * @param delay  delay<=0 的时候，表示该数据行不需要缓存了，应该被移除出   调度队列和延迟队列  缓存的数据行也应该被缓存
     *
     * */
    public void scheduleRowCache(String rowId,long delay){
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
        Transaction transaction = jedis.multi();
        transaction.zadd("delay:",delay,rowId);  //需要循环执行   每间隔delay再执行一次数据行缓存
        //调度初始值  开始执行 没缓存一次数据行，score都会进行更新，最新的score = 原来的score + delay
        transaction.zadd("schedule:",System.currentTimeMillis()/1000,rowId);
        transaction.exec();
    }

    /**
     * 获取商品
     * @param goodId
     * @return
     */
    public static Good getGood(String goodId){
        return map.get(goodId);
    }
}
