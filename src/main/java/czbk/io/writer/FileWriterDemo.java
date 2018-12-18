package czbk.io.writer;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by 18435 on 2018/11/8.
 * 字符流两个基类：
 * Reader Writer
 *
 * 字节流两个基类：
 * InputStream  OutputStream
 *
 * IO流是用于操作数据的，那么数据最常见的体现形式是：文件
 *
 * 需求：在硬盘上，创建一个文件并写入一些文字数据
 *
 * FileWriter : 后缀名是父类名  前缀名是该流对象的功能
 */
public class FileWriterDemo {

    public static void main(String[] args) {
        FileWriter fw = null;
        try {
            //创建一个FileWriter对象。该对象一被初始化就必须要明确被操作的文件
            //而且该文件会被创建到指定目录下，如果该目录下已有同名文件，将被覆盖
            //其实该步骤就是在明确数据要存放的目的地
            // fw = new FileWriter("F:\\fileWriter.txt");

            //续写文件 传递一个true参数，如果文件不存在，会创建文件，如果存在，则不覆盖已有的文件，并在已有文件的末尾处进行数据续写
            fw = new FileWriter("F:\\fileWriter.txt",true);

             //将字符串写入到流中  linux: 换行：\n    windows:换行：\r\n  windows系统不能识别 \n
             fw.write("aabb\r\nccdd");

             //刷新流对象中的缓冲中的数据
            //将数据刷到目的地中
             fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fw!=null){
                try {
                    //关闭流资源，但是关闭之前会刷新一次缓冲中的数据
                    //将数据刷到目的地中
                    //和flush区别：flush刷新后，流可以继续使用  close刷新后，会将流关闭
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
