package czbk.file;

import java.io.*;

/**
 * Created by 18435 on 2018/11/20.
 * 将一个指定目录下的java文件的绝对路径，存储到一个文本文件中。
 * 建立一个Java文件列表文件
 *
 * 1.对指定的目录进行递归
 * 2.获取递归过程所有java文件的路径
 * 3.将这些路径存储到集合中
 * 4.将集合中的数据写入到一个文件中
 *
 */
public class FileDemo5 {
    public static void main(String[] args) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("E:\\fileAbPath.log"));
            File file = new File("E:\\workspace2\\ATM");
            javaFileAbPath(file,bw);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void javaFileAbPath(File file, BufferedWriter bufferedWriter) throws IOException {
        File[] files = file.listFiles();
        for (File file1:files){
            if(!file1.isHidden() && file1.isDirectory()){
                javaFileAbPath(file1,bufferedWriter);
            }else {
                bufferedWriter.write(file1.getAbsolutePath());
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }
    }
}
