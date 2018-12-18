package czbk.io.copy;

import java.io.*;

/**
 * Created by 18435 on 2018/11/12.
 * 提供字节缓冲流复制mp3文件
 *
 * 缓冲是为了
 */
public class CopyMp3 {

    public static void copyByStream() throws Exception{
        FileOutputStream fos = new FileOutputStream("F:\\李易峰角色.mp3");
        FileInputStream fis = new FileInputStream("F:\\KuGou\\李易峰角色.mp3");
        byte[] data = new byte[1024];
        int length = 0;
        while ((length=fis.read(data))!=-1){
            fos.write(data,0,length);
        }
        fis.close();
        fos.close();
    }

    public static void copyByBuffer(){
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream("F:\\KuGou\\李易峰角色.mp3"));
            bos = new BufferedOutputStream(new FileOutputStream("F:\\李易峰角色.mp3"));

            byte[] data = new byte[1024];
            int length = 0;
            while ((length=bis.read(data))!=-1){
                bos.write(data,0,length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bis!=null ){
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bos!=null){
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        CopyMp3.copyByStream();
        long end = System.currentTimeMillis();
        System.out.println("复制时间："+ (end - start));//流复制 3  缓冲流复制 3
    }
}
