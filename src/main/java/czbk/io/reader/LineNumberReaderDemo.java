package czbk.io.reader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 * Created by 18435 on 2018/11/9.
 */
public class LineNumberReaderDemo {

    public static void main(String[] args) {

        LineNumberReader reader = null;

        try {
            reader = new LineNumberReader(new FileReader("E:\\课程介绍.txt"));

            String line = "";
            reader.setLineNumber(100);//设置行号起始值
            while ((line=reader.readLine())!=null){
                System.out.println(reader.getLineNumber() + ":"+line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
