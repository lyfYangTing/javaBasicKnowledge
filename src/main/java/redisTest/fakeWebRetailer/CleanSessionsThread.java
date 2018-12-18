package redisTest.fakeWebRetailer;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by 18435 on 2018/6/27.
 * 更新登录会话
 */
public class CleanSessionsThread extends Thread{
    private Jedis conn;
    private int limit;
    private boolean quit;

    public CleanSessionsThread(int limit) {
        this.conn = new Jedis("localhost");
        this.conn.select(15);
        this.limit = limit;
    }

    public void quit() {
        quit = true;
    }

    public void run(){
        while (!quit) {
            long countCookie = conn.zcard("recent:");
            if (countCookie <= limit) {//不要清除  休眠1分钟
                try {
                    Thread.sleep(1000);
                    continue;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                //需要清理数据  cookie
                //方法1 ：long endIndex = countCookie-limit<=1000 ? countCookie - limit : 1000;
                long endIndex = Math.min(countCookie-limit,1000);
                Set<String> cookies = conn.zrange("recent:", 0, endIndex - 1 );
                Transaction transaction = conn.multi();
                transaction.zremrangeByRank("reccent:", 0, endIndex -1 ); //从最近登录列表中移除掉
                Set<String> viewedCookies = new HashSet<>();
                for (String cookie : cookies) {
                    viewedCookies.add("viewed:" + cookie);
                }
                transaction.hdel("login:",cookies.toArray(new String[cookies.size()]));//清除登录数据
                transaction.del(viewedCookies.toArray(new String[viewedCookies.size()]));//清除浏览数据
            }
        }
    }
}
