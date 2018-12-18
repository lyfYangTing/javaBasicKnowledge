package czbk.file;

import java.io.*;
import java.util.Properties;
import java.util.Set;

/**
 * Created by 18435 on 2018/11/21.
 * Properties是hashtable的子类
 * 具备map集合的特点，而且它里面存储的键值对都是字符串。
 *
 * 是集合中和IO技术相结合的集合容器
 *
 * 该对象的特点：可以用于键值对形式的配置文件
 *
 * 那么在加载数据时，需要数据有固定格式：键=值
 */
public class ProprotiesDemo {
    public static void main(String[] args) {
        storeDemo();
        System.exit(0);
    }

    //演示：如何将流中的数据存储到集合中
    //想要将info.txt中的键值数据存到集合中进行操作
    public static void method_1(){
        BufferedReader br = null;
        try {
            br  = new BufferedReader(new FileReader("F:\\info.txt"));
            Properties properties = new Properties();
            properties.load(br);
            System.out.println(properties);
            properties.list(System.out);
//            Set<String> names = properties.stringPropertyNames();
//            for(String s : names){
//                System.out.println("name="+ s +"::value="+properties.getProperty(s));
//            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //设置和获取元素
    public static  void  setAndGet(){
        Properties properties = new Properties();
        properties.setProperty("zhangsan","30");
        properties.setProperty("lisi","40");

        //取单个
        String value = properties.getProperty("zhangsan");
        System.out.println(value);

        //取全部
        Set<String> names = properties.stringPropertyNames();
        for(String s : names){
            System.out.println("name="+ s +"::value="+properties.getProperty(s));
        }
    }

    public static void storeDemo(){
        BufferedReader br = null;
        FileOutputStream fos = null;
        try {
            br  = new BufferedReader(new FileReader("F:\\info.txt"));
            Properties properties = new Properties();
            properties.load(br);

            properties.setProperty("lisi","60");
            properties.setProperty("ceshi","70");
            fos = new FileOutputStream("F:\\info.txt");
            properties.store(fos,"zhushi");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
