package redisTest.queue;

import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;
import test.JedisPoolUntil;

import java.util.List;

/**
 * Created by 18435 on 2018/7/12.
 * Redis 先进先出队列应用
 */
public class FifoQueue {
    private boolean quit;

    public FifoQueue(boolean quit) {
        this.quit = quit;
    }

    /**
     * 将一封待发送邮件推入一个由列表结构表示的队列里
     * @param seller
     * @param item
     * @param buyer
     * @param price
     */
    public void sendSoldEmailViaQueue(String seller,String item,String buyer,double price){
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
        JSONObject data = new JSONObject();
        data.put("seller_id",seller);
        data.put("item_id",item);
        data.put("price",price);
        data.put("buyer_id",buyer);
        data.put("time",System.currentTimeMillis());
        jedis.rpush("queue:email",data.toJSONString());
    }

    /**
     * 从队列里获取待发送邮件 最后根据获取到的信息来发送邮件
     * BLPOP命令：如果列表为空，返回一个 nil 。
     * 否则，返回一个含有两个元素的列表，第一个元素是被弹出元素所属的 key ，第二个元素是被弹出元素的值。
     */
    public void processSoldEmailQueue(){
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
        while (quit){
            List<String> result = jedis.blpop(30,"queue:email");
            if(result==null){
                continue;
            }
            String data = result.get(1);//返回列表不是空，则取出被弹出的数据
            try{
                this.fetchDataAndSendSoldEmail(data);
                System.out.println("Send sold email" + data);
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("Failed to send sold email" + data);
            }
        }
    }

    /**
     * 把邮件真正地发送出去
     * @param to_send
     */
    public void fetchDataAndSendSoldEmail(String to_send){
        //解析数据
        JSONObject data = JSONObject.parseObject(to_send);
        //发送邮件
    }
}
