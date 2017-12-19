package insideClass.applicationScene.connectionPool;

import java.sql.Connection;
import java.util.Date;
import java.util.Timer;

/**
 * Created by 18435 on 2017/12/18.
 * PoolConn类不大可能被除了ConnectionPool类的其他类使用到，把它作为ConnectionPool的私有内部类不会影响到其他类。
 * 同时，我们可以看到，使用了内部类，使得我们可以将该内部类的数据公开，ConnectionPool类可以直接操作PoolConn类的数据成员，避免了因set和get方法带来的麻烦。
 * 下面的例子，是使用内部类使得代码得到简化和方便。
 * 还有些情况下，你可能要避免你的类被除了它的外部类以外的类使用到，这时候你却不得不使用内部类来解决问题。
 */
public class ConnectionPool implements Pool{
    //存在Connection的数组
    private PoolConn[] poolConns;
    //连接池的最小连接数
    private int min;
    //连接池的最大连接数
    private int max;
    //一个连接的最大使用次数
    private int maxUseCount;
    //一个连接的最大空闲时间
    private long maxTimeout;
    //同一时间的Connection最大使用个数
    private int maxConns;
    //定时器
    private Timer timer;

    public boolean init() {
        try
        {
            this.poolConns = new PoolConn[this.min];
            for(int i=0;i<this.min;i++)
            {
                PoolConn poolConn = new PoolConn();
                //poolConn.conn = ConnectionManager.getConnection();
                poolConn.isUse = false;
                poolConn.lastAccess = new Date().getTime();
                poolConn.useCount = 0;
                this.poolConns[i] = poolConn;
            }
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    public void destory() {

    }

    public Connection getConn() {
        return null;
    }

    //内部类  获取一个Connection
    private class PoolConn{
        public Connection conn;
        public boolean isUse;
        public long lastAccess;
        public int useCount;
    }
}
