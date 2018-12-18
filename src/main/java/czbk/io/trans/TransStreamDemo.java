package czbk.io.trans;

import java.io.*;

/**
 * Created by 18435 on 2018/11/13.
 *
 * 通过刚才的键盘录入一行数据并打印其大写，发现其实就是读一行数据的原理。也就是readLine方法
 * 能不能直接使用readLine方法来完成键盘录入的一行数据的读取？
 *
 * readLine方法是字符流BufferedReader类中的方法
 * 而键盘录入中的read方法是字节流InputStream的方法
 *
 * InputStreamReader是字节流通向字符流的桥梁，它使用指定的charset读取字节并将其解码位字符。（没有显式指定，则使用平台默认的字符集）
 * OutputStreamWriter是字符流通向字节流的桥梁，它使用指定的charset将要写入流中的字符编码成字节。
 * 将 字节流 -----》 字符流  ------》 通过字符缓存流中的readLine读取
 */
public class TransStreamDemo {

    public static void main(String[] args) {
        InputStream  in = System.in;//获取键盘录入对象
        InputStreamReader ir = new InputStreamReader(in);//通过转换流  将字节流对象转换成字符流对象
        BufferedReader br = new BufferedReader(ir);//通过缓存区 提高读取效率

        try {
            OutputStream os = System.out;
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
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
