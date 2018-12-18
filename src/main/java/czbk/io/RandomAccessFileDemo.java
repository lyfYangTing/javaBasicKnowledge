package czbk.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by 18435 on 2018/11/28.
 *
 * RandomAccessFile
 *
 * 该类不算是IO体系中的子类，而是直接基础自Object
 *
 * 但是它是IO包中的成员。因为它具备读和写功能。
 * 内部封装了一个byte数组，而且通过指针对数组的元素进行操作。
 * 可以通过getFilePointer获取指针位置，同时可以通过seek改变指针的位置。
 *
 * 其实完成读写的原理就是内部封装了字节输入流和输出流
 * 通过构造函数也可以看出，该类只能操作文件，而且操作文件还有模式（只读，读写...）
 *
 * 如果模式为只读r 不会创建文件，会去读取一个已存在的文件
 * 如果模式为rw 操作的文件不存在，会自动创建。如果存在则不会覆盖
 * 应用：可以用来多线程下载视频文件
 */
public class RandomAccessFileDemo {

    public static void main(String[] args) {
        //writeFile();
        readFile();
    }

    public static  void writeFile(){

        try {
            File file = new File("ran.txt");
            System.out.println(file.getAbsolutePath());
            RandomAccessFile raf = new RandomAccessFile("ran.txt","rw");


            //调整对象中的指针
            //raf.seek(8*2);
            //跳过指定的字节数
            //raf.skipBytes(8*2);
            raf.write("李四".getBytes());//系统默认UTF-8编码  两个字符六个字节
            raf.write(93);//只写最低八位,写一个字节
            raf.write("王五".getBytes());
            raf.writeInt(95);//写完整的4个字节

            raf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readFile(){
        try {
            RandomAccessFile raf = new RandomAccessFile("ran.txt","rw");

            //调整对象中的指针
            raf.seek(7);
            //跳过指定的字节数
            //raf.skipBytes(8*2);

            byte[] data = new byte[6];
            raf.read(data);
            System.out.println("name: " + new String(data));

            int age = raf.readInt();//读四个字节
            System.out.println("age: " + age);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
