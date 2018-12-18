package czbk.io;

import java.io.*;

/**
 * Created by 18435 on 2018/11/9.
 * 通过缓冲区复制一个.java文件
 *
 * readLine方法的原理：
 * 无论是读一行，或者是获取读取多个字符。其实最终都是在硬盘上一个一个读取。所以最终使用的还是read方法一次读一个的方法
 */
public class CopyTextByBuf {

    public static void main(String[] args) {
        BufferedReader br = null;
        BufferedWriter bw = null;

        try {
            FileReader fr = new FileReader("E:\\课程介绍.txt");
            br = new BufferedReader(fr);
            FileWriter fw = new FileWriter("F:\\课程介绍.txt");
            bw = new BufferedWriter(fw);

            String line = null;
            while ((line = br.readLine())!=null){//line  返回的是这一行的有效数据，不包括行终止符
                bw.write(line);
                bw.newLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(br!= null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bw!=null){
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
