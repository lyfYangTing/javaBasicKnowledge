package czbk.io.objectStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by 18435 on 2018/11/26.
 */
public class ObjectOutputStreamdemo {

    public static void main(String[] args) {
        writeObj();
    }

    public static void writeObj(){
        try {
            ObjectOutputStream  oos = new ObjectOutputStream(new FileOutputStream("E:\\test\\person.object"));
            oos.writeObject(new Person());
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
