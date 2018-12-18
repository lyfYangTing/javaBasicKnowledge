package czbk.io.objectStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by 18435 on 2018/11/26.
 * ObjectInputStream  ObjectOutputStream
 * 被操作的对象需要实现Serializable(标记接口)
 *
 * 对象的持久化存储（对象的序列化，对象的串行性）：把对象存放在硬盘上  把一个介质能长期保存数据的  把堆里面序列化
 *
 * 不能序列化static静态属性   和transient
 *
 */
public class ObjectInputStreamDemo {

    public static void main(String[] args) {
        readObj();
    }

    public static void readObj(){
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("E:\\test\\person.object"));
            Person person = (Person) ois.readObject();
            System.out.println(person);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
