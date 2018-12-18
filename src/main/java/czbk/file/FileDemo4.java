package czbk.file;

import java.io.File;

/**
 * Created by 18435 on 2018/11/20.
 * 删除一个带内容的目录
 *
 * 删除原理：
 * 在window中，删除目录从里面往外删除的
 */
public class FileDemo4 {

    public static void main(String[] args) {
        File file = new File("E:\\file");
        deleteDir(file);
    }

    public static void deleteDir(File dir){
        File[] files = dir.listFiles();//java无法访问windows系统隐藏目录,，所以dir是隐藏目录时，则获取到的 files 为 null
        for(File file : files){
            if(!file.isHidden() && file.isDirectory()){
                deleteDir(file);
            }else {
                file.delete();//删除文件
            }
        }
        dir.delete();//删除当前目录
    }
}
