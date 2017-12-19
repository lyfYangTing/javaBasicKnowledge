package insideClass.applicationScene.connectionPool;

import java.sql.Connection;
import java.util.Date;
import java.util.Timer;

/**
 * Created by 18435 on 2017/12/18.
 * PoolConn�಻����ܱ�����ConnectionPool���������ʹ�õ���������ΪConnectionPool��˽���ڲ��಻��Ӱ�쵽�����ࡣ
 * ͬʱ�����ǿ��Կ�����ʹ�����ڲ��࣬ʹ�����ǿ��Խ����ڲ�������ݹ�����ConnectionPool�����ֱ�Ӳ���PoolConn������ݳ�Ա����������set��get�����������鷳��
 * ��������ӣ���ʹ���ڲ���ʹ�ô���õ��򻯺ͷ��㡣
 * ����Щ����£������Ҫ��������౻���������ⲿ���������ʹ�õ�����ʱ����ȴ���ò�ʹ���ڲ�����������⡣
 */
public class ConnectionPool implements Pool{
    //����Connection������
    private PoolConn[] poolConns;
    //���ӳص���С������
    private int min;
    //���ӳص����������
    private int max;
    //һ�����ӵ����ʹ�ô���
    private int maxUseCount;
    //һ�����ӵ�������ʱ��
    private long maxTimeout;
    //ͬһʱ���Connection���ʹ�ø���
    private int maxConns;
    //��ʱ��
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

    //�ڲ���  ��ȡһ��Connection
    private class PoolConn{
        public Connection conn;
        public boolean isUse;
        public long lastAccess;
        public int useCount;
    }
}
