package czbk.file;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by 18435 on 2018/11/20.
 */
public class FileDemo2 {
    public static void main(String[] args) {
        //listRootsDemo();
        //listDemo();
        //listFilterDemo();
        listFilesDemo();
    }

    public static void listRootsDemo(){
        File[] files = File.listRoots();
        for(File file : files){
            System.out.println(file);
        }
    }

    public static  void listDemo(){
        File file = new File("E:\\");
        for (String name : file.list()){//调用list方法的file对象必须是封装了一个目录。该目录还必须存在
            System.out.println(name);//当前目录下的文件夹名称或文件名
        }
    }

    public static void listFilterDemo(){
        FilenameFilter filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".txt");
            }
        };
        File file = new File("E:\\");
        for (String name:file.list(filenameFilter)){
            System.out.println(name);
        }
    }

    public static void listFilesDemo(){
        File file = new File("E:\\");
        File[] files = file.listFiles();
        for(File file1:files){
            System.out.println(file1.getName() + "::"+ file1.length());
        }
    }
}
