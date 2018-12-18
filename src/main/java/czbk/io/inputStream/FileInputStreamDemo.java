package czbk.io.outputStream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by 18435 on 2018/11/9.
 */
public class FileInputStreamDemo {

    public static void main(String[] args) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("E:\\fileOutputStream.txt");
            try {
                //文件数据的字节大小
//                System.out.println( fis.available());
//                byte[] data = new byte[fis.available()];//一次性读取出来   虚拟机启动默认内存64M  最好不要这样用
//                System.out.println(new String(data));
                byte[] data = new byte[1024];
                int num = 0;
                StringBuffer sb = new StringBuffer();
                while ((num = fis.read(data))!=-1){
                    System.out.println(new String(data,0,num,"UTF-8"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
