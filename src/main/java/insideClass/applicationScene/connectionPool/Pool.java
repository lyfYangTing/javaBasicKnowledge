package insideClass.applicationScene.connectionPool;

import java.sql.Connection;

/**
 * Created by 18435 on 2017/12/18.
 */
public interface Pool {
    //��ʼ�����ӳ�
    public boolean init();
    //�������ӳ�
    public void destory();
    //ȡ��һ������
    public Connection getConn();
}
