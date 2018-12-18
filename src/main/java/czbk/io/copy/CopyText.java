package czbk.io.copy;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by 18435 on 2018/11/8.
 * 将F盘的一个文件复制到E盘
 *
 * 复制原理：
 * 其实就是将F盘下的文件数据存储到E盘的一个文件中
 *
 * 步骤：
 * 1.创建文件
 * 2.读取流与被读文件关联
 * 3.不断读写
 * 4.关闭资源
 */
public class CopyText {

    /**
     * FileReader属于字符流，是读取字符文件的便捷类。其继承自InputStreamReader，后者是将字节流转换为字符流的的桥梁，即将字节信息转换为字符信息。
     * 实际上， FileReader在类内部实现过程中也是利用了InputStreamReader完成字节流到字符流的转化，只不过转化时采用的字符集为系统默认的字符集。
     * 如果文件保存时的编码设定为UTF-8， 那么在中文操作系统使用 FileReader时就会发生乱码，因为中文操作系统平台的默认字符集为GBK。
     *
     * 解决该问题的办法是，放弃使用FileReader，改用InputStreamReader，在获取InputStreamReader对象时，显示指定合适的字符集。
     * @param args
     */
    public static void main(String[] args) {
        FileReader fr = null;
        FileWriter fw = null;

        try {
            //与已有文件关联
            fr = new FileReader("E:\\课程介绍.txt");
            //创建目的地
            fw = new FileWriter("F:\\课程介绍.txt");
            char[] data = new char[1024];
            int len = 0;
            while ((len=fr.read(data))!=-1){
                fw.write(data,0,len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fr!=null){
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fw!=null){
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
