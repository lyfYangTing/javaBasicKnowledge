package redisTest;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import test.JedisPoolUntil;

/**
 * Created by 18435 on 2018/7/4.
 */
public class ItemMarket {

    /**
     * 将卖家自己包裹中的商品添加到市场中
     *
     * @param itemId
     * @param sellerId
     * @param price
     */
    public boolean listItem(String itemId, String sellerId, float price) {
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();

        Pipeline pipeline = jedis.pipelined();
        String inventory = "inventory:%s";
        String.format(inventory, sellerId);
        String item = "%s:%s";
        String.format(item, itemId, sellerId);
        long end = System.currentTimeMillis() + 5;
        while (System.currentTimeMillis() < end) {
            try {
                pipeline.watch(inventory);
                if (!pipeline.sismember(inventory, itemId).get()) {
                    //需要取消监控
                    return false;
                }
                pipeline.multi();
                pipeline.zadd("market:", price, item);
                pipeline.zrem(inventory, itemId);
                pipeline.exec();
                return true;
            } catch (redis.clients.jedis.exceptions.JedisException e){
                e.printStackTrace();
                listItem(itemId,sellerId,price); //接着来
            }
        }
        return false;
    }

    /**
     * 购买商品
     * @param buyierId
     * @param itemId
     * @param sellerId
     * @param lprice
     * @return
     */
    public boolean purchaseItem(String buyierId,String itemId,String sellerId,double lprice){
        String buyer = "users:%s";
        String.format(buyer,buyierId);
        String seller = "users:%s";
        String.format(seller,sellerId);
        String item = "%s:%s";
        String.format(item, itemId, sellerId);
        String inventory = "inventory:%s";
        String.format(inventory, buyierId);
        long end = System.currentTimeMillis() + 5;
        Pipeline pipeline = JedisPoolUntil.getJedisPoolInstance().getResource().pipelined();
        while (System.currentTimeMillis() < end){
            try{
                pipeline.watch("market:",buyer);
                double price = pipeline.zscore("market:",item).get();
                String funds = pipeline.hget(buyer,"funds").get();
                if(!pipeline.sismember("market:",item).get()){
                    System.out.println("商品已被卖出");
                    return false;
                }
                if(price != lprice || price > Double.parseDouble(funds)){
                    System.out.println("余额不足");
                    return false;
                }
                pipeline.multi();
                pipeline.hincrByFloat(seller,"funds",price);
                pipeline.hincrByFloat(buyer,"funds",-price);
                pipeline.sadd(inventory,itemId);
                pipeline.zrem("market:",item);
                pipeline.exec();
                return true;
            }catch (redis.clients.jedis.exceptions.JedisException e){
                e.printStackTrace();
                purchaseItem(buyierId,itemId,sellerId,lprice); //接着来
            }
        }
        return false;
    }
}
