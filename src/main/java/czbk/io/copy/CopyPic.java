package czbk.io.copy;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 18435 on 2018/11/9. 不要拿字符流处理媒体文件
 * 1.用字节读入流对象关联图片文件
 * 2.用字节写入流创建一个图片文件，用于存储获取的到的图片数据
 * 3.循环读写
 * 4.关闭流资源
 */
public class CopyPic {
    public static void main(String[] args) {
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            fis = new FileInputStream("E:\\OpenResty技能图谱.jpg");
            fos = new FileOutputStream("F:\\OpenResty技能图谱.jpg");

            byte[] data = new byte[1024];
            int length = 0;
            while ((length = fis.read(data))!= -1){
                fos.write(data,0,length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
