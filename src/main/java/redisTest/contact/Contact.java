package redisTest.contact;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import test.JedisPoolUntil;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by 18435 on 2018/7/10.
 */
public class Contact{

    /**
     * 添加更新联系人
     * @param userId        联系人列表归属用户
     * @param contactId     联系人
     */
    public void addOrUpdateContact(String userId,String contactId){
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
        String key = "contact:"+ userId;
        Transaction transaction = jedis.multi();
        try{
            transaction.watch(key);
            transaction.lrem(key,0,contactId);  //如果已存在，先删除
            transaction.lpush(key,contactId);
            long llen = transaction.llen(key).get();
            if(llen > 100){//联系人长度大于100，则修剪成100个
                transaction.ltrim(key,0,100);
            }
            transaction.exec();
        }catch (Exception e){
            transaction.discard();
            addOrUpdateContact(userId,contactId);
        }
    }

    /**
     * 移除最近联系人
     * @param userId    联系人列表归属用户
     * @param contactId   被用户移除的联系人
     */
    public void removeContact(String userId,String contactId){
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
        String key = "contact:"+userId;
        jedis.lrem(key,0,contactId);
    }


    /**
     * 获取自动补全列表并查找匹配的用户
     * @param userId    联系人列表归属用户
     * @param prefix    需匹配的前缀
     * @return
     */
    public List<String> fetchAutocompleteList(String userId,String prefix){
        Jedis jedis = JedisPoolUntil.getJedisPoolInstance().getResource();
        String key = "contact:"+ userId;
        List<String> matchList = new LinkedList<>(); //匹配列表
        List<String> contactList = jedis.lrange(key,0,-1);
        contactList.forEach(contactId -> {
            if(contactId.startsWith(prefix)){
                matchList.add(contactId);
            }
        });
        return matchList;
    }

    public static void main(String[] args) {
        System.out.println();
    }
}
