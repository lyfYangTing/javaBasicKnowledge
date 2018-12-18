package redisTest.fakeWebRetailer;

import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;
import test.JedisPoolUntil;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by 18435 on 2018/6/28.
 */
public class CacheRowsThread extends Thread{
    private boolean quit;

    public CacheRowsThread(boolean quit){
        this.quit = quit;
    }

    @Override
    public void run() {
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
        while(!quit){
            //查找此时需要缓存的数据行
            Set<Tuple> set = jedis.zrangeByScoreWithScores("schedule:",0,0);
            //为什么要取第一个元素  因为第一个元素是最迫切需要被执行的
            Tuple firstTuple = new ArrayList<Tuple>(set).get(0);
            if(firstTuple!=null && firstTuple.getScore()>System.currentTimeMillis()/1000){
                try {
                    sleep(50); //暂时没有行需要被缓存  休眠50毫秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            //获取延迟时间
            double delay = jedis.zscore("delay:",firstTuple.getElement());
            Transaction transaction = jedis.multi();
            if(delay <= 0){//该数据行不需要缓存了，删除掉
                //删除数据行数据
                transaction.del("inv:"+firstTuple.getElement());
                //将该数据行号 从任务队列和延时队列中移除
                transaction.zrem("schedule:",firstTuple.getElement());
                transaction.zrem("delay:",firstTuple.getElement());
                transaction.exec();
                continue;
            }
            //缓存数据行 String 类型  key(行号)->value(json格式的数据行)  设置缓存值
            Good good = CacheRows.getGood(firstTuple.getElement());
            transaction.set("inv:"+ firstTuple.getElement(), JSONObject.toJSONString(good));
            //更新下次任务的执行时间
            transaction.zadd("schedule:",delay + firstTuple.getScore(),firstTuple.getElement());
            transaction.exec();
        }
    }
}
