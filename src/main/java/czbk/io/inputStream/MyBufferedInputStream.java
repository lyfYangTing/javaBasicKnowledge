package czbk.io.inputStream;

import java.io.*;

/**
 * Created by 18435 on 2018/11/12.
 *
 * 自己构造一个缓冲字节流
 *
 * 3个关键点：
 * 1.缓冲数组
 * 2.指针（read方法读取的就是指针所指的位置）
 * 3.计数器（计数器表示已从缓冲区读取了多少字节  读取字节数等于缓冲数组中的存储数据长度的时候，读取结束）
 *
 * 媒体文件中存储的是01二进制编码
 * 补充：1 字节 读取的是8位二进制编码
 * 媒体文件中的存储：11111111-111111111-000010100010100101000101001001010010101010
 *
 * byte:-1    ------> int : -1  4个字节
 * byte:-1                                  11111111
 * int :-1    11111111  11111111  11111111  11111111
 * int :255   00000000  00000000  00000000  11111111
 *
 * 11111111  ---》 提升了一个int类型，那不还是-1 吗?是-1 的原因是因为在8个1 前面补的是1 导致的。
 * 那么只有在前面补上 0，既可以保留原字节数据不变，又可以避免 -1 的出现。
 *
 * 怎么补0：
 *   11111111  11111111  11111111  11111111
 * & 00000000  00000000  00000000  11111111
 * ------------------------------------------
 *   00000000  00000000  00000000  11111111
 *
 *   -1 的二进制编码 ： 1 的二进制编码取反加1
 */
public class MyBufferedInputStream {
    private InputStream is;

    private byte[] bufferList = new byte[1024];
    private int index = 0;
    private int count = 0;

    public MyBufferedInputStream(InputStream is){
        this.is = is;
    }

    //一次读一个字节,从缓冲数组中获取
    public int myRead() throws IOException {
        if(count == 0){//本次缓冲数组中的数据已经被读取完毕
            count = is.read(bufferList);//通过in对象读取硬盘上数据，并存在buf中
            index = 0;
        }
        if(count == -1){//文件已经读取完毕
            return -1;
        }
        count--;
        return bufferList[index++] & 255;
    }

    public void myClose() throws IOException {
        if(is!=null){
            is.close();
        }
    }

    public static void main(String[] args) {
        MyBufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new MyBufferedInputStream(new FileInputStream("F:\\1.mp3"));
            bos = new BufferedOutputStream(new FileOutputStream("F:\\2.mp3"));

            int data = 0;
            while ((data = bis.myRead())!=-1){
                bos.write(data);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bis!=null ){
                try {
                    bis.myClose();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bos!=null){
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
