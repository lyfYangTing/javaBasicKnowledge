package redisTest.fakeWebRetailer;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ZParams;
import test.JedisPoolUntil;

/**
 * Created by 18435 on 2018/6/28.
 * 守护进程：做的工作
 * 1.删除所有排名在20000名之后的商品
 * 2.将剩余的所有商品浏览次数减半
 * 3.每间隔5分钟执行一次
 */
public class RescaleViewed extends Thread{
    private boolean quit;

    public RescaleViewed(boolean quit) {
        this.quit = quit;
    }

    @Override
    public void run() {
        while(quit){
            Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
            if(jedis.zcard("viewed:") < 10){
                break;
            }
            jedis.zremrangeByRank("viewed:",10,-1); //删除所有排名在10名之后的商品
            ZParams params = new ZParams();
            params.weightsByDouble(0.5);
            jedis.zinterstore("viewed:",params,"viewed:");//将剩余的所有商品浏览次数减半
            try {
                sleep(5*60*1000);//休眠5分钟
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
        for(int i=1;i<=20;i++){
            jedis.zadd("viewed:",i*2,String.valueOf(i));
        }
        System.out.println("未清理前的浏览商品数量："+jedis.zcard("viewed:"));

        RescaleViewed rescaleViewed = new RescaleViewed(true);
        rescaleViewed.start();

        System.out.println("清理后的浏览商品数量："+jedis.zcard("viewed:"));
    }
}
