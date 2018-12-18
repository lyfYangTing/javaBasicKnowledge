package redisTest;

import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import test.JedisPoolUntil;

import java.util.UUID;

/**
 * Created by 18435 on 2018/6/27.
 */
public class FakeWebRetailer {
    
    /**
     * 更新令牌cookie：token
     *
     * @param token
     * @param userId
     */
    public void updateToken(String token, String userId, String itemId) {
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
        Transaction transaction = jedis.multi();
        long updateTime = System.currentTimeMillis() / 1000;
        //String hkey = "login:" + token; 多次一举  内部键token每个用户的都不一样，当然不会被覆盖掉了
        transaction.hset("login:", token, userId);//对用户存储在登录散列里的信息进行更新   维持令牌与已登陆用户之间的映射
        transaction.zadd("recent:", updateTime, token);//最近登录用户集合

        if (StringUtils.isNotEmpty(itemId)) {
            //添加到  用户浏览商品列表中
            transaction.zadd("viewed:" + token, updateTime, itemId);
            transaction.zremrangeByRank("viewed:" + token, 0, -26);
        }
    }

    /**
     * 获取令牌对应用户
     *
     * @param token
     * @return
     */
    public String getTokenUser(String token) {
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
        return jedis.hget("login:", token);
    }

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

    /**
     * 登录测试
     *
     * @throws InterruptedException
     */
    public void testLoginCookies(Jedis conn) throws InterruptedException {
        /*
        这里令牌自动生成
        UUID.randomUUID().toString():javaJDK提供的一个自动生成主键的方法
         */
        String token = UUID.randomUUID().toString();
        //创建登录和缓存类的对象
        FakeWebRetailer fakeWebRetailer = new FakeWebRetailer();
        /*
        使用 fakeWebRetailer 中的updateToken()的方法更新令牌
        conn:Redis连接，user1:用户，item1:商品
         */
        fakeWebRetailer.updateToken(token, "user1", "item1");
        System.out.println("我们刚刚登录/更新了令牌:" + token);
        System.out.println("用户使用: 'user1'");
        System.out.println();

        System.out.println("当我们查找令牌时会得到什么用户名");
        String r = fakeWebRetailer.getTokenUser(token);
        //r是令牌
        System.out.println(r);
        System.out.println();
        assert r != null;

        System.out.println("让我们把cookies的最大数量降到0来清理它们");
        System.out.println("我们开始用线程来清理，一会儿再停止");

        CleanSessionsThread thread = new CleanSessionsThread(0);
        thread.start();
        Thread.sleep(1000);
        thread.quit();
        Thread.sleep(2000);
        if (thread.isAlive()) {
            throw new RuntimeException("线程还存活?!?");
        }
        //Hlen命令用于获取哈希表中字段的数量
        long s = conn.hlen("login:");
        System.out.println("目前仍可提供的sessions次数如下: " + s);
        assert s == 0;
    }

}
}
