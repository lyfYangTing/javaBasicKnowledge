package czbk.io;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by 18435 on 2018/11/8.
 * Reader 类是 Java 的 I/O 中读字符的父类，而 InputStream 类是读字节的父类，InputStreamReader 类就是关联字节到字符的桥梁，
 * 它负责在 I/O 过程中处理读取字节到字符的转换，而具体字节到字符的解码实现它由 StreamDecoder 去实现，
 * 在 StreamDecoder 解码过程中必须由用户指定 Charset 编码格式。值得注意的是如果你没有指定 Charset，
 * 将使用本地环境中的默认字符集，例如在中文环境中将使用 GBK 编码。
 *
 * 总结：Java读取数据流的时候，一定要指定数据流的编码方式，否则将使用本地环境中的默认字符集。
 *
 */
public class FileReaderDemo {

    /**
     * 方式一：一次读一个字符   read方法   文件读取结束条件：fr.read()==-1
     */
    public static void fileReaderOne(){
        FileReader fr = null;
        try {
            //创建一个文件读取流对象，和指定名称的文件相关联
            //要保证文件是已经存在的，如果不存在，会发生异常FileNotFoundException
            fr = new FileReader("F:\\fileWriter.txt");

            //int a = fr.read();  一次读一个字符,而且会自动往下读
            int ch = 0;
            while ((ch=fr.read())!=-1){//fr.read()==-1  文件结束标识
                System.out.println((char)ch);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fr!=null){
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 第二种方式：通过字符数组进行读取
     */
    public static void fileReaderTwo(){
        FileReader fr = null;

        try {
            fr = new FileReader("F:\\fileWriter.txt");

            char[] ch = new char[1024];
            int index = 0;
            StringBuilder sb = new StringBuilder();
            while ((index = fr.read(ch))!=-1){//条件不可以是 (index = fr.read(ch))==ch.length 会漏调最后不足ch数组的数据
                sb.append(ch,0,index);
            }
            System.out.println(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FileReaderDemo.fileReaderTwo();
    }
}
