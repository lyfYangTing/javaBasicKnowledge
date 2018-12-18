package czbk.io;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

/**
 * Created by 18435 on 2018/11/23.
 */
public class SplitFile {

    public static void main(String[] args) {
        //splitFile();
        //merge();
        mergeTwo();
    }

    public static void splitFile(){
        //E:\splitFil
        FileInputStream fis = null;
        try {
             fis = new FileInputStream("E:\\splitFile.avi");
             byte[] data = new byte[2048];
             int count = 1;
             int length = 0;
             while ((length=fis.read(data))!=-1){
                 FileOutputStream fos = new FileOutputStream("E:\\splitFiles\\"+(count++)+".part");
                 fos.write(data,0,length);
                 fos.close();
             }
             fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void merge(){
        Vector<FileInputStream> ve = new Vector<>();

        File file = new File("E:\\splitFiles");
        File[] files = file.listFiles();
        try {
//            for(File file1 : files){
//                ve.add(new FileInputStream(file1));
//                System.out.println(file1.getName());
//            }
            for(int i=0;i<files.length;i++){
                ve.add(new FileInputStream("E:\\splitFiles\\"+(i+1)+".part"));
            }
            Enumeration en = ve.elements();
            SequenceInputStream si = new SequenceInputStream(en);
            FileOutputStream fos = new FileOutputStream("E:\\splitFiles.avi");
            byte[] data = new byte[1024];
            int length = 0;
            while ((length=si.read(data))!=-1){
                    fos.write(data,0,length);
            }
            fos.close();
            si.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void mergeTwo(){
        File file = new File("E:\\splitFiles");
        File[] files = file.listFiles();
        ArrayList<FileInputStream> arrayList = new ArrayList<>();
        try {
            for(int i=0;i<files.length;i++) {
                arrayList.add(new FileInputStream("E:\\splitFiles\\" + (i + 1) + ".part"));
            }
            Iterator<FileInputStream> iterator = arrayList.iterator();
            Enumeration<FileInputStream> enumeration = new Enumeration<FileInputStream>() {
                @Override
                public boolean hasMoreElements() {
                    return iterator.hasNext();
                }

                @Override
                public FileInputStream nextElement() {
                    return iterator.next();
                }
            };

            SequenceInputStream si = new SequenceInputStream(enumeration);
            FileOutputStream fos = new FileOutputStream("E:\\splitFiles.avi");
            byte[] datas = new byte[1024];
            int length = 0;
            while ((length=si.read(datas))!=-1){
                fos.write(datas,0,length);
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
