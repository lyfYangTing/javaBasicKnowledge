package insideClass.applicationScene.connectionPool;

import java.sql.Connection;

/**
 * Created by 18435 on 2017/12/18.
 */
public interface Pool {
    //初始化连接池
    public boolean init();
    //销毁连接池
    public void destory();
    //取得一个连接
    public Connection getConn();
}
