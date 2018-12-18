package redisTest.contact;

import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import test.JedisPoolUntil;

import java.util.List;
import java.util.UUID;

/**
 * Created by 18435 on 2018/7/10.
 * 工会自动补全功能发送邮件
 */
public class AddressBook {
    private final  String valid_characters = "`abcdefghijklmnopqrstuvwxyz{";
    /**
     *
     * @param prefix  前缀
     * @return
     */
    public JSONObject findPrefixRange(String prefix){
        JSONObject jsonObject = new JSONObject();
        char[] valid = valid_characters.toCharArray();
        char[] prefixChar = prefix.toCharArray();
        char oldSuffix = prefixChar[prefixChar.length-1];
        char newSuffix = valid[valid_characters.indexOf(oldSuffix)-1];
        jsonObject.put("predecessor",prefix.substring(0,prefixChar.length-1)+newSuffix);
        jsonObject.put("successor",prefix + valid[valid.length-1]);
        System.out.println("前缀的前驱为："+ jsonObject.get("predecessor"));
        System.out.println("前缀的后驱为："+ jsonObject.get("successor"));
        return  jsonObject;
    }

    /**
     * 获取工会中中前缀为prefix的用户
     * @param prefix  前缀
     * @param userId  用户
     * @return
     */
    public List<String> autocompleteOnPrefix(String prefix,String userId){
        String key = "members:"+userId;
        JSONObject jsonObject = this.findPrefixRange(prefix); //获取前缀的前驱和后驱
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
        Transaction transaction = jedis.multi();
        String uuid = UUID.randomUUID().toString();
        String predecessor = jsonObject.getString("predecessor") + uuid;
        String successor = jsonObject.getString("successor") + uuid;
        try{
            //将前缀元素的前驱和后驱元素添加到集合中
            transaction.zadd(key,0,predecessor);
            transaction.zadd(key,0,successor);

            //获取前驱和后驱元素的位置   计算查找范围的起始值
            long  predecessorIndex = transaction.zrank(key,predecessor).get();
            long  successorIndex = transaction.zrank(key,successor).get();
            //之间的元素多于10个时就取10个元素   少于10个则全取
            long erange = Math.min(predecessorIndex+9,successorIndex-2);

            transaction.zrem(key,predecessor,successor);//先移除 再获取
            List<String> list = transaction.lrange(key,predecessorIndex,erange).get();
            return list;
        }catch (Exception e){
            e.printStackTrace();
            this.autocompleteOnPrefix(prefix,userId);
        }
        return null;
    }

    /**
     * 加入工会
     * @param userId
     * @param guild
     */
    public void join_guide(String userId,String guild){
        String key = "members:"+userId;
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
        jedis.zadd(key,0,guild);
    }

    /**
     * 离开工会
     * @param userId
     * @param guild
     */
    public void leave_guide(String userId,String guild){
        String key = "members:"+userId;
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
        jedis.zrem(key,guild);
    }

}
