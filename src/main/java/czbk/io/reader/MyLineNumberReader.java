package czbk.io;

import java.io.*;

/**
 * Created by 18435 on 2018/11/9.
 * 模拟一个带行号的缓冲区对象
 */
public class MyLineNumberReader extends BufferedReader{
    private int lineNumber;

    public MyLineNumberReader(Reader in) {
        super(in);
    }

    public int getLineNumber(){
        return lineNumber;
    }

    public void setLineNumber(int lineNumber){
        this.lineNumber = lineNumber;
    }

    public String readLine() throws IOException {
        lineNumber++;
        return super.readLine();
    }

    public static void main(String[] args) {
        MyLineNumberReader reader = null;
        try {
            reader = new MyLineNumberReader(new FileReader("E:\\课程介绍.txt"));

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
