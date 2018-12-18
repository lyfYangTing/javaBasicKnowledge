package czbk.io.outputStream;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 18435 on 2018/11/9.
 */
public class FileOutputStreamDemo {

    public static void main(String[] args) {
        FileOutputStream fos = null;
        try {
            //输出  -----》   写
            fos = new FileOutputStream("E:\\fileOutputStream.txt");

            //不需要刷新 数据直接写入文件 不需要进行字节到字符的转换
            fos.write("你好".getBytes());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fos!=null){
                try {
                    fos.close();//关资源
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
