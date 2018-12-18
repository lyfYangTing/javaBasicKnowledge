package czbk.file;

import java.io.File;
import java.io.IOException;

/**
 * Created by 18435 on 2018/11/15.
 * File类：
 * 1.用来将文件或者文件夹封装成对象
 * 2.方便对文件与文件夹的属性信息进行操作
 * 3.File对象可以作为参数传递给流的构造函数
 *
 * File类是java.io 包下代表与平台无关的文件和目录。
 * File不能访问文件内容本身。如果需要访问文件内容本身，则需要使用输入/输出流   流只能操作数据
 *
 * File类常见方法：
 * 1.创建文件 boolean createNewFile():在指定位置创建文件，如果该文件已经存在，则不创建，返回false
 *                              和输出流不一样，输出流对象一建立创建文件，而文件已经存在，会覆盖
 *            boolean mkdir():创建一个文件夹
 *            boolean mkdirs():创建多级目录
 *
 * 2.删除
 *   boolean delete()  删除失败返回false
 *   void deleteOnExit():在程序退出时删除指定文件
 *
 * 3.判断
 *    boolean exists(): 文件是否存在
 *    boolean isFile()：是否是文件              -------》记住，在判断文件对象是否是文件或者目录时，必须先判断该文件对象封装的内容是否存在，通过exists判断
 *    boolean isDirectory()：是否是目录       -------》
 *    boolean isHidden():是否隐藏
 *    boolean isAbsolute();是否是绝对路径（带盘符的）
 * 4.获取信息
 *
 *   getParent(): 该方法返回的是绝对路径中的父目录。如果获取的是相对路径，返回null，如果相对路径中有上一层目录，那么该目录就是返回结果
 */
public class FileTest {

    public static void main(String[] args) {
        //创建文件
        File file = new File("E:\\file\\a.txt");
        System.out.println("是否是绝对路径："+ file.isAbsolute());
        System.out.println(file);
        try {
            if(!file.exists()){
                boolean dirResult = file.getParentFile().mkdirs();
                System.out.println("创建目录结果：" + dirResult);
            }
            boolean result = file.createNewFile();
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //consMethod();
    }

    //创建File对象
    public static void consMethod(){
        //将a.txt封装成File对象。可以将已有的和未出现的文件或者文件夹封装成对象
        File f = new File("a.txt");//该类的实例可以表示也可以不表示实际的文件系统对象，例如文件或目录。

        File f2 = new File("E:\\","b.txt");

        File d = new File("E:\\");
        File f3 = new File(d,"c.txt");

        aop("f1:"+f);
        aop("f2:"+f2);
        aop("f3:"+f3);

        String fenge = File.separator;//系统分隔符
        File f4 = new File("E:"+fenge,"b.txt");
        aop("f4:"+f4);
    }

    public  static void method(){

    }
    public static  void  aop(Object object){
        System.out.println(object);
    }
}

