package czbk.io.reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by 18435 on 2018/11/9.
 * 字符读取流缓冲区
 *
 * 该缓冲区提供了一个一次读一行的方法readLine,方便于对文本数据的获取  返回的是这一行的有效数据，不包括行终止符
 * 当返回null时，表示读到文件末尾
 */
public class BufferedReaderDemo {

    public static void main(String[] args) {
        FileReader fr= null;
        BufferedReader br = null;

        try {
            //创建一个文件读取流对象和文件相关联
            fr = new FileReader("F:\\fileWriter.txt");

            //为了提高效率，加入缓冲技术，将字符读取流对象作为参数传递给缓冲对象的构造函数
            br = new BufferedReader(fr);

            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line=br.readLine())!=null){
                sb.append(line);
            }
            System.out.println(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
        }
    }


}
