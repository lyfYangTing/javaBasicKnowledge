package czbk.io;

import java.io.*;

/**
 * Created by 18435 on 2018/11/29.
 *
 * DataInputStream DataOutputStream
 *
 * 可以用于操作基本数据类型的数据的流对象
 *
 */
public class DataStreamDemo {

    public static void main(String[] args) {
        //writeData();
        //readData();
        //writeUTFDemo();
        readUTFDemo();
    }

    /**
     * byte     1 字节
     * boolean  false/true(理论上占用1bit,1/8字节，实际处理按1 byte处理)
     * short    2 字节
     * char     2 字节（C语言中是1字节）可以存储一个汉字
     * int      4 字节
     * float    4 字节
     * long     8 字节
     * double   8 字节
     * JAVA是采用Unicode编码。每一个字节占 8 位。
     */
    public static void readData(){
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream("data.txt"));
            int num = dis.readInt();//读4个字节
            boolean b = dis.readBoolean();
            double d = dis.readDouble();//读8个字节
            System.out.println("num:"+num);
            System.out.println("b:"+b);
            System.out.println("d:"+d);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeData(){
        try {
            DataOutputStream dos = new DataOutputStream(new FileOutputStream("data.txt"));
            dos.writeInt(234); //在文本中打开是乱码，因为文本文件打开的时候会将字节查表将其转换成字符，而不是基本数据类型
            dos.writeBoolean(true);
            dos.writeDouble(9887.543);
            dos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改版的UTF-8  一个字符4 个字节
     */
    public static void writeUTFDemo(){
        try {
            DataOutputStream dos = new DataOutputStream(new FileOutputStream("utfdata,txt"));
            dos.writeUTF("你好");//用这个方法写 只能用readUTF读出来
            dos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readUTFDemo(){
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream("utfdata,txt"));
            String data = dis.readUTF();
            System.out.println("data:"+data);
            dis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
