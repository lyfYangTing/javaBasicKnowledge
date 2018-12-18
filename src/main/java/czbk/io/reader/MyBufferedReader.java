package czbk.io.reader;

import java.io.*;

/**
 * Created by 18435 on 2018/11/9.
 * 自定义一个类中包含一个功能和readLine一致的方法。模拟一下BufferedReader
 *
 * 装饰设计模式：
 * 当相要对已有的对象进行功能增强时，
 * 可以定义类，将已有对象传入，基于已有的功能，并提供加强功能。
 * 那么自定义的该类称为装饰类
 *
 * 装饰类通常会通过构造方法接收被装饰的对象。
 * 并基于被装饰的对象的功能，提供更强的功能
 *
 * 通过继承实现对各种曲序数据子类读写功能的加强：继承体系
 * MyReader 专门用于读取数据的类
 *     \——MyTextReader
 *          \——MyBufferTextReader
 *     \——MyMediaReader
 *          \——MyBufferMediaReader
 *     \——MyDataReader
 *          \——MyBufferDataReader
 *
 * class MyBufferReader{
 *     MyBufferReader(MyTextReader text){}
 *     MyBufferReader(MyMediaReader media){}
 *     MyBufferReader(MyDataReader data)
 * }
 *
 *上面这个类扩展性很差，找到其共同类型，通过多态的形式，可以提高扩展性
 *
 * class MyBufferReader extend MyReader
 * {
 *     private MyReader r;
 *     MyBufferReader(MyReader r){}
 * }
 *
 * 实现对各种曲序数据子类读写功能的加强：通过装饰设计模式优化后的组合结构体系
 * MyReader 专门用于读取数据的类
 *     \——MyTextReader
 *     \——MyMediaReader
 *     \——MyDataReader
 *     \——MyBufferReader  增强功能
 *
 * 装饰模式比继承要灵活，避免了继承体系臃肿。
 * 而且降低了类与类之间的关系。
 *
 * 装饰类因为增强已有对象，具备的功能和已有的是相同的，只不过提供了更强的功能。
 * 所以装饰类和被装饰类通常是都属于一个体系中的。
 */
public class MyBufferedReader extends Reader{//装饰类

    private Reader reader;

    public MyBufferedReader(Reader reader) {
        this.reader = reader;
    }

    //可以一次读一行数据的方法
    public String myReadLine() throws IOException{
        //定义一个临时容量，原BufferReader封装的是字符数组
        StringBuilder sb = new StringBuilder();
        int ch = 0;
        while ((ch=reader.read())!=-1){
            if(ch=='\t'){
                continue;
            }else if (ch=='\n'){
                return sb.toString();
            }else {
                sb.append((char) ch);//一定记得转换成字符类型
            }
        }
        if(sb.length()>0){//防止最后一行没有换行符
            return sb.toString();
        }
        return null;
    }

    public void myClose() throws IOException{
        if(reader!=null){
            reader.close();
        }
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        return reader.read(cbuf,off,len);
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    public static void main(String[] args) {
        BufferedWriter bw = null;
        MyBufferedReader mbr = null;

        try {
            bw = new BufferedWriter(new FileWriter("E:\\课程介绍.txt"));
            mbr = new MyBufferedReader(new FileReader("F:\\课程介绍.txt"));

            String line = "";
            while ((line = mbr.myReadLine())!=null){
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(mbr!=null){
                try {
                    mbr.myClose();
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
