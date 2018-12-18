package czbk.file.printDemo;

import java.io.*;

/**
 * Created by 18435 on 2018/11/22.
 * 打印流：
 * 该流提供了打印方法，可以将各种数据类型的数据都原样打包
 *
 * 字节打印流：PrintStream
 * 构造函数可以接收的参数类型：
 * 1.file对象。File
 * 2.字符串路径
 * 3.字节输出流(OutputStream)
 *
 * 字符打印流：PrintWriter
 * 构造函数可以接收的参数类型：
 * 1.file对象。File
 * 2.字符串路径
 * 3.字节输出流(OutputStream)
 * 4.字符输出流：Writer
 *
 */
public class PrintStreamDemo {
    public static void main(String[] args) throws IOException {
        //printWriterMethod();
        printStreamDemo();
    }

    public static void printWriterMethod(){
        //读取键盘
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        PrintWriter pw = new PrintWriter(System.out,true);

        String line = null;
        try {
            while ((line = br.readLine())!=null){
                if("over".equals(line)){
                    return;
                }
                pw.println(line);//字符流需要刷新  才能将缓冲区内的数据刷到目的地
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(pw!=null){
                pw.close();
            }
        }
    }

    public static void printStreamDemo(){
        //读取键盘
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        PrintStream ps = null;
        try {
            ps = new PrintStream("F:\\print.txt");
            while ((line = br.readLine())!=null){
                if(line.equals("over")){
                    return;
                }
                ps.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(ps!=null){
                ps.close();
            }
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
