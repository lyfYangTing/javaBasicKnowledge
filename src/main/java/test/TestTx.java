package test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * Created by 18435 on 2018/6/22.
 */
public class TestTx {

    public boolean transMethod(){
        Jedis jedis = new Jedis("127.0.0.1",6379);
        int balance ; //可用余额
        int debt ; //欠款
        int amtToSubtract = 10 ; //实刷额度

        jedis.watch("balance");
        balance = Integer.parseInt(jedis.get("balance"));

        if(balance < amtToSubtract){
            jedis.unwatch();
            System.out.println("modify,余额不足");
            return false;
        }else{
            System.out.println("**************transaction");
            Transaction transaction = jedis.multi();
            transaction.decrBy("balance",amtToSubtract);
            transaction.incrBy("debt",amtToSubtract);
            transaction.exec();
            balance = Integer.parseInt(jedis.get("balance"));
            debt = Integer.parseInt(jedis.get("debt"));

            System.out.println("************"+balance);
            System.out.println("************"+debt);

            return true;
        }
    }

    public static void main(String[] args) {
        TestTx testTx = new TestTx();
        boolean retValue = testTx.transMethod();
        System.out.println("main value------------:"+retValue);
    }
}
