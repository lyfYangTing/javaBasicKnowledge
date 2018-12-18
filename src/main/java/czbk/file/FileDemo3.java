package czbk.file;

import java.io.File;

/**
 * Created by 18435 on 2018/11/20.
 * 列出指定目录下文件或者文件夹，包含子目录中的内容。
 * 也就是列出指定目录下的所有内容
 *
 * 因为目录中还有目录，只要使用同一个列出目录功能的函数完成即可。
 * 在列出过程中出现的还是目录的话，还可以再次调用本功能。
 * 也就是函数自身调用自身，这种表现形式，或者编程手法，称为递归。
 *
 * 递归要注意：
 * 1.限定条件
 * 2.注意递归的次数，尽量避免栈内存溢出
 */
public class FileDemo3 {

    public static void main(String[] args) {
        File file = new File("E:\\学习\\Java学习");
        showDir(file,1);
        //int sum = getSum(100);
        //System.out.println(sum);
        //toBin(11);
    }

    public static String getLevel(int level){
        StringBuffer sb = new StringBuffer();
        sb.append("|--");
        for (int i=0;i<level;i++){
            sb.insert(0,"  ");
        }
        return sb.toString();
    }

    /**
     * 列出指定目录下的所有内容
     * @param dir
     */
    public static void showDir(File dir,int level){
        File[] files = dir.listFiles();
        System.out.println(getLevel(level) + dir.getName());
        level++;
        for(File file:files){
            if(file.isDirectory()){
                showDir(file,level);
            }else {
                System.out.println(getLevel(level) + file.getName());
            }
        }
    }



    /**
     * 累加 n .... 1
     * @param n
     */
    public static int getSum(int n){
        if(n==1){
            return 1;
        }else {
            return n + getSum(n-1);
        }
    }

    /**
     * 十进制转换成二进制
     *
     * @param num
     */
    public static  void toBin(int num){
        if(num > 0){
            toBin(num/2);
            System.out.println(num%2);
        }
    }
}
