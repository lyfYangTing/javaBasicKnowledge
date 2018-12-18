package test;

import org.apache.commons.collections.map.HashedMap;
import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by 18435 on 2018/6/22.
 */
public class RedisTest {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("127.0.0.1",6379);
        //System.out.println(jedis.ping());   测试连通性
        //key
        Set<String> set = jedis.keys("*");
        for(Iterator iterator = set.iterator();iterator.hasNext();){
            String key = (String)iterator.next();
            System.out.println(key);
        }
        System.out.println("jedis.exists=====>" + jedis.exists("k2"));
        System.out.println(jedis.ttl("k1"));

        //String
        jedis.append("k1","myredis");
        System.out.println(jedis.get("k1"));
        jedis.set("k4","v4_redis");
        System.out.println("- - - - - -  - - - - - - - - - - - - - - - - - - - -");
        jedis.mset("str1","v1","str2","v2","str3","v3");
        System.out.println(jedis.mget("str1","str2","str3"));

        //list
        System.out.println("- - - - - -  - - - - - - - - - - - - - - - - - - - -");
        jedis.lpush("myjedislist","v1","v2","v3","v4","v5");
        List<String> list = jedis.lrange("myjedislist",0,-1);
        for(String value : list){
            System.out.println(value);
        }

        //set
        jedis.sadd("orders","jd001");
        jedis.sadd("orders","jd002");
        jedis.sadd("orders","jd003");
        Set<String> set1 = jedis.smembers("orders");
        for(Iterator iterator = set1.iterator();iterator.hasNext();){
            String string = (String)iterator.next();
            System.out.println(string);
        }
        jedis.srem("orders","jd002");
        System.out.println(jedis.smembers("orders").size());

        //hash
        jedis.hset("jedishash","userName","zhangsan");
        System.out.println(jedis.hget("jedishash","userName"));

        Map<String,String> map = new HashedMap();
        map.put("telephone","13811814763");
        map.put("address","atguigu");
        map.put("email","abc@163.com");
        jedis.hmset("jedishash",map);
        List<String> result = jedis.hmget("jedishash","telephone","address","email");
        for (String element : result){
            System.out.println(element);
        }

        //zset
        jedis.zadd("zset01",60d,"v1");
        jedis.zadd("zset01",70d,"v2");
        jedis.zadd("zset01",80d,"v3");
        jedis.zadd("zset01",90d,"v4");

        Set<String> s1 = jedis.zrange("zset01",0,-1);
        for(Iterator iterator = s1.iterator();iterator.hasNext();){
            String string = (String)iterator.next();
            System.out.println(string);
        }

    }
}
