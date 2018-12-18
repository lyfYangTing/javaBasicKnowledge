package czbk.io.trans;

import java.io.*;

/**
 * Created by 18435 on 2018/11/14.
 * 1.
 * 源：键盘   BufferedReader br = new BufferedReader(new InputStreamReader(System.in))
 * 目的：控制台 BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
 *
 * 2.需求：想把键盘录入的数据存储到一个文件中
 * 源：键盘 BufferedReader br = new BufferedReader(new InputStreamReader(System.in))
 * 目的：文件(硬盘)  BufferedReader br = new BufferedReader(new FileWriter("fileName"))
 *
 * 3.需求：想要将一个文件的数据打在控制台上
 * 源：文件(硬盘) BufferedReader br = new BufferedReader(new FileReader("fileName"))
 * 目的：控制台 BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
 *
 * 通过3个明确来完成：
 * 1.明确源和目的
 *   源：输入流 InputStream Reader
 *   目的：输出流  OutputStream  Writer
 * 2.操作的数据是否是纯文本
 *   是：字符流  不是：字节流
 * 3.当体系明确后，在明确使用哪个具体的对象。
 *   通过设备来进行区分：
 *   源设备：内存，硬盘，键盘（键盘录入是纯文本）
 *   目的设备：内存，硬盘，控制台
 *
 * 1.将一个文本文件中的数据存储到另一个文件中。（复制文件）
 *   源：因为是源，所以使用读取流。InputStream Reader
 *   是不是操作文本文件： 是，这个时候就选择 Reader体系
 *   接下来明确使用改体系中的哪个对象：
 *   明确设备：硬盘（一个文件）
 *   Reader体系中可以操作文件的对象是FileReader
 *   是否需要提高效率：是，加入Reader体系中缓存区BufferedReader
 *   BufferedReader bw = new BufferedReader(new FileReader("fileName"));
 *
 *   目的：OutputStream Writer
 *   是否是纯文本：是 Writer
 *   设备：硬盘，一个文件
 *   Writer系统中可以操作文件的对象是FileWriter
 *   是否需要提高效率：是，加入Writer体系中缓存区BufferedWriter
 *   BufferedWriter bw = new BufferedWriter(new FileWriter("fileName"));
 *
 * 2.需求:将键盘录入的数据保存到一个文件中
 *
 *   源：因为是源，所以使用读取流。InputStream Reader
 *   是不是操作文本文件： 是，这个时候就选择 Reader体系
 *   接下来明确使用改体系中的哪个对象：
 *   明确设备：键盘（对应的对象是  System.in 字节流）
 *   既然明确了Reader体系，那就将System.in 转换成Reader
 *   Reader体系中可以将字节流转换成字符流的对象是InputStreamReader
 *   是否需要提高效率：是，加入Reader体系中缓存区BufferedReader
 *   BufferedReader bw = new BufferedReader(new InputStreamReader(System.in));
 *
 *   目的：OutputStream Writer
 *   是否是纯文本：是 Writer
 *   设备：硬盘，一个文件
 *   Writer系统中可以操作文件的对象是FileWriter
 *   是否需要提高效率：是，加入Writer体系中缓存区BufferedWriter
 *   BufferedWriter bw = new BufferedWriter(new FileWriter("fileName"));
 *
 *
 *   *************************************************
 *   扩展：想要把录入的数据按照指定的编码表（gbk）,将数据存到文件中
 *
 *   目的：OutputStream Writer
 *   是否是纯文本：是 Writer
 *   设备：硬盘，一个文件
 *   Writer系统中可以操作文件的对象是FileWriter，但是FileWriter使用的是系统默认编码（utf-8）
 *
 *   但是存储时，需要加入指定的编码表gbk。而指定的编码表只有转换流才可以指定，所以要使用OutputStreamWriter,而
 *   该转换流对象要接收一个字节输出流。而且还可以操作的文件的字节输出流。FileOutputStream
 *
 *   是否需要提高效率：是，加入Writer体系中缓存区BufferedWriter
 *   BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("fileName"),"GBK"));
 *
 *   转换流什么时候使用：
 *   字符和字节之间的桥梁，通常涉及到字符编码转换时，需要用到转换流
 *
 *   改变标准输入输出设备
 *
 *   练习：将一个文本数据打印在控制台上
 */
public class TransStreamDemo2 {
    public static void main(String[] args) {

        try {
            System.setIn(new FileInputStream("E:\\fileOutputStream.txt"));
            System.setOut(new PrintStream("E:\\22.txt"));
            //键盘的最常见写法
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));//通过缓存区 提高读取效率

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
            String line = null;
            while ((line = br.readLine())!=null){
                if(line.equals("over")){//自定义结束标记爱试
                    break;
                }
                bw.write(line);
                bw.newLine();//换行
                bw.flush();//字符输出流内部有缓冲区  需要刷新
                //System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
