package redisTest.fakeWebRetailer;

import redis.clients.jedis.Jedis;
import test.JedisPoolUntil;

import java.util.UUID;

/**
 * Created by 18435 on 2018/6/27.
 * 登录缓存测试
 */
public class LoginAndCookieTest {
    /**
     * 登录测试
     *
     * @throws InterruptedException
     */
    public static void testLoginCookies(Jedis conn) throws InterruptedException {
        /*
        这里令牌自动生成
        UUID.randomUUID().toString():javaJDK提供的一个自动生成主键的方法
         */
        String token = UUID.randomUUID().toString();
        //创建登录和缓存类的对象
        LoginAndCookie fakeWebRetailer = new LoginAndCookie();
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

    public static void main(String[] args) {
        Jedis conn = JedisPoolUntil.getJedisPoolInstance().getResource();
        LoginAndCookieTest loginAndCookieTest =new LoginAndCookieTest();
        try {
            loginAndCookieTest.testLoginCookies(conn);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
