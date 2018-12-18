package czbk.io;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 18435 on 2018/11/15.
 * 将异常信息保存在一个文件中
 */
public class ExceptionInfo {

    public static void main(String[] args) {
        try {
            int[] arr = new int[2];
            int a = arr[2];
        }catch (Exception e){
            //新需求：将异常信息打印在文件里
            try {
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                PrintStream ps = new PrintStream("E:\\exception.log");
                ps.println(sdf.format(date));
                e.printStackTrace(ps);//将异常信息打印在控制台
            } catch (FileNotFoundException e1) {
                throw new RuntimeException("日志文件创建失败");
            }
        }
    }

}
