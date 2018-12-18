package czbk.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by 18435 on 2018/11/9.
 * 缓冲区的出现 是为了提高流的操作效率（所以创建缓冲区之前，必须先有流对象）  提高对数据的读写效率
 * 数据（水）  --------》   流（相当于水管） ---------》  缓冲区（相当于水杯，蓄水池）
 *
 * 缓冲区要结合流才可以使用，在流的基础上对流的功能进行了增强
 *
 * 该缓冲区提供了一个跨平台的换行符：
 * newLine();
 */
public class BufferWriterDemo {

    public static void main(String[] args) {

        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            //创建一个字符写入流对象 真正调用底层资源
            fw = new FileWriter("F:\\bufferWriter.txt");
            //为了提高字符写入流效率 加入了缓冲技术
            //只有将需要被提高效率的流对象作为参数传递给缓冲区的构造函数即可
            bw = new BufferedWriter(fw);

            for(int x=1;x<5;x++){
                bw.write("abcd"+x);
                bw.newLine();
                bw.flush();//防止过程中程序被恶意终止，数据全部丢失
            }
            bw.write("缓冲输出流");

            //记住，只要用到缓冲区，就有记得刷新
            bw.flush();

            bw.newLine();//换行
            bw.write("测试");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bw!=null){
                try {
                    //其实关闭缓冲区，就是在关闭缓冲区中的流对象
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//            if(fw!=null){  所以不需要特意关闭流对象
//                try {
//                    fw.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }
}
