package redisTest.fakeWebRetailer;

import redis.clients.jedis.Jedis;
import test.JedisPoolUntil;

import java.util.Map;
import java.util.UUID;

/**
 * Created by 18435 on 2018/6/27.
 */
public class AddToCartTest {
    AddToCart addToCart =new AddToCart();
    LoginAndCookie loginAndCookie =new LoginAndCookie();


    public static void main(String[] args) throws InterruptedException {
        Jedis conn = JedisPoolUntil.getJedisPoolInstance().getResource();
        AddToCartTest addToCartTest =new AddToCartTest();
        addToCartTest.testShopppingCartCookies(conn);
    }
    /**
     * 添加商品到购物车测试
     * @param conn
     * @throws InterruptedException
     */
    public void testShopppingCartCookies(Jedis conn) throws InterruptedException {
         /*
        这里令牌自动生成
        UUID.randomUUID().toString():javaJDK提供的一个自动生成主键的方法
         */
        String token = UUID.randomUUID().toString();

        System.out.println("我们将刷新我们的会话.");
        loginAndCookie.updateToken(token, "user2", "item2");
        System.out.println("并在购物车中添加一个商品");
        addToCart.addToCard("user2", "item2", 3);
        Map<String, String> r = conn.hgetAll("cart:" + token);
        System.out.println("我们的购物车目前有:");
        for (Map.Entry<String, String> entry : r.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }
    }
}
