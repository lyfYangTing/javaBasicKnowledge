package test;

import redis.clients.jedis.Jedis;

/**
 * Created by 18435 on 2018/6/22.
 */
public class TestMasterSlaver {
    public static void main(String[] args) {
        Jedis jedis_M = new Jedis("127.0.0.1",6379);//主机
        Jedis jedis_S = new Jedis("127.0.0.1",6380);//从机

        jedis_S.slaveof("127.0.0.1",6379);  //连接主机

        jedis_M.set("class","1122");
        String result = jedis_S.get("class");
        System.out.println(result);//输出  1122
    }
}
