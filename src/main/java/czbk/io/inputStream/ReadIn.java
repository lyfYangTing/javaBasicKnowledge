package czbk.io.inputStream;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 18435 on 2018/11/13.
 * 读取键盘录入：
 * System.out:对应的标准输出设备  控制台
 * System.in:对应的标准输入设备   键盘
 *
 * '\r' ---> 13  '\n'--->10
 *
 * 通过键盘录入数据。
 * 当录入一行数据后，就将该行数据进行打印，
 * 如果录入的数据是over,那么停止录入
 */
public class ReadIn {

    public static void main(String[] args) throws IOException {
        InputStream is = System.in;
        int by = 0;
        StringBuilder sb = new StringBuilder();
        while ((by = is.read())!= -1){
            if(by == '\r'){
                continue;
            }else if (by == '\n'){
                if(sb.toString().equals("over")){
                    break;
                }
                System.out.println(sb.toString());//输出该行数据
                sb.delete(0,sb.length()); //清除缓存中数据
            }else {
                sb.append((char)by);
            }
        }
    }
}
