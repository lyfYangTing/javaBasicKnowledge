package redisTest.fakeWebRetailer;

import redis.clients.jedis.Jedis;
import test.JedisPoolUntil;

/**
 * Created by 18435 on 2018/6/27.
 * 购物车实现
 */
public class AddToCart {
    /**
     * 购物车
     * @param user   登录用户
     * @param itemId    商品id
     * @param count     商品数量
     */
    public void addToCard(String user,String itemId,int count){
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
//        为什么要这样玩？这样玩hash结构的value需要是一个集合 然后里面存放商品的信息   查询不方便添加也不方便
//        所以直接用商品id作为其内部键，然后商品数量作为其值不是一个好的解决方案吗
//        Map<String,String> map = new HashedMap();
//        map.put("itemId",itemId);
//        map.put("count",String.valueOf(count));
        if(count > 0){
            jedis.hset("cart"+user,itemId,String.valueOf(count));  //向购物车里添加商品
        }else {
            jedis.hdel("cart"+user,itemId); //count数量小于0就删除  该商品
        }
    }
}
