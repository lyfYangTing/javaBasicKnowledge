package czbk.io;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Properties;

/**
 * Created by 18435 on 2018/11/15.
 */
public class SystemInfo {

    public static void main(String[] args) {
        Properties properties = System.getProperties();//获取系统属性 是一个map集合

        //System.out.println(properties);
        properties.list(System.out);//打印在控制台
        PrintStream ps = null;
        try {
            ps = new PrintStream("E:\\system.log");
            properties.list(ps);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            ps.close();
        }
        properties.list(ps);
    }
}
