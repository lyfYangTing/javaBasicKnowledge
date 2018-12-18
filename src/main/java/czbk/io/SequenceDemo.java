package czbk.io;

import java.io.*;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Created by 18435 on 2018/11/23.
 * SequenceInputStream表示其他输入流的逻辑级联。
 * 它从一个有序的输入流集合开始，从第一个输入流读取到文件的末尾，然后从第二个输入流读取，以此类推，直到包含的输入流的最后一个到达文件结束。
 */
public class SequenceDemo {
    public static void main(String[] args) {
        Vector<FileInputStream> v = new Vector<FileInputStream>();
        try {
            v.add(new FileInputStream("E:\\iotext\\1.txt"));
            v.add(new FileInputStream("E:\\iotext\\2.txt"));
            v.add(new FileInputStream("E:\\iotext\\3.txt"));

            Enumeration<FileInputStream> en = v.elements();

            SequenceInputStream si = new SequenceInputStream(en);

            FileOutputStream fos = new FileOutputStream("E:\\iotext\\4.txt");
            byte[] data = new byte[1024];
            int length = 0;
            while ((length=si.read(data))!=-1){
                fos.write(data,0,length);
            }

            si.close();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
