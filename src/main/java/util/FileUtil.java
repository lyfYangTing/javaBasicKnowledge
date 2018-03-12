package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.Random;

/**
 * Created by 18435 on 2018/3/12.
 */
public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 网络输入流(DataInputStream)  ----------->  输出到目标文件中
     * @param url  网络传输地址
     * @param target  目标文件
     * @return
     * @throws IOException
     */
    public static String saveFileFromUrl(URL url, File target) throws IOException {
        //打开网络输入流
        DataInputStream dis = new DataInputStream(url.openStream());
        //建立一个新的文件
        return saveFileFromDataInputStream(dis, target);
    }
    /**
     *
     * @param data  源网络输入流
     * @param target  目标文件
     * @return
     * @throws IOException
     */
    public static String saveFileFromDataInputStream(DataInputStream data, File target) throws IOException {
        File parent = target.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(target);
        byte[] buffer = new byte[1024];
        int length;
        //开始填充数据
        while ((length = data.read(buffer)) > 0) {//将输入流中数据-----》读取到 buffer 中
            fos.write(buffer, 0, length);//将buffer中的数据-----》写入fos文件输出流中-----》文件中
        }
        data.close();
        fos.close();
        return "SUCCESS";
    }



    /**
     *
     * @param source  源文件
     * @param path    目标文件路径
     * @param fileName  文件名
     * @return
     * @throws FileNotFoundException
     */
    public static String saveFileFromFile(File source, String path,
                                          String fileName) throws FileNotFoundException {
        try {
            InputStream is = new FileInputStream(source);

            return saveFileFromInputStream(is, new File(path + "/" + fileName));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "success";
    }
    /**
     * 把文件写到磁盘上
     *
     * @param source 原始文件
     * @param target 目标文件
     * @return String
     * @throws FileNotFoundException 异常
     */
    public static String saveFileFromFile(final File source, final File target) throws FileNotFoundException {
        try {
            InputStream is = new FileInputStream(source);

            return saveFileFromInputStream(is, target);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "success";
    }
    /**
     *
     * @param is  源输入流
     * @param target   目标文件
     * @return
     * @throws FileNotFoundException
     */
    public static String saveFileFromInputStream(InputStream is, File target)
            throws FileNotFoundException {
        try {
            File parent = target.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            // 创建一个输出流
            OutputStream os = new FileOutputStream(target);
            // 设置缓存
            byte[] buffer = new byte[1024];
            int length = 0;
            // 读取myFile文件输出到toFile文件中
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            // 关闭输入流
            is.close();
            // 关闭输出流
            os.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "success";
    }



    /**
     * 获取随机文件名
     *
     * @param length
     * @param fileName
     * @return
     */
    public static String getRandomFileName(int length, String fileName) {
        String random = getCharAndNumr(length);
        return random + "." + getExtension(fileName);
    }
    /**
     * 获取指定位数的字母数字混合随机码
     *
     * @param length
     * @return
     */
    public static String getCharAndNumr(int length) {
        String val = "";

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            if ("char".equalsIgnoreCase(charOrNum)) {
                //65-90 大写字母A-Z      97-122 小写字母a-z
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
    /**
     * 返回文件的文件后缀名
     *
     * @param fileName
     * @return
     */
    public static String getExtension(String fileName) {
        try {
            return fileName.split("\\.")[fileName.split("\\.").length - 1];
        } catch (Exception e) {
            return null;
        }
    }



    /**
     * 返回指定目录下的所有文件名
     *
     * @param directory
     * @return
     */
    public static String[] getAllFilesOfDirectory(String directory) {
        File df = new File(directory);
        if (df.exists()) {
            return df.list();
        }
        return null;
    }



    /**
     * 删除指定文件
     *
     * @param filePath
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }


    /**
     *将 byte 数组中的数据写入 目标文件中
     * @param data  源文件  byte数组
     * @param target 目标文件
     * @return
     * @throws IOException
     */
    public static String saveFileFromBytes(byte[] data, File target) throws IOException {
        File parent = target.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(target);
        BufferedOutputStream stream = new BufferedOutputStream(fos);
        stream.write(data);
        fos.flush();
        fos.close();
        return "SUCCESS";
    }

    /**
     *
     * @param file  获取指定文件的 byte数组
     * @return
     * @throws IOException
     */
    public static byte[] getBytes(File file) throws IOException {
        FileInputStream io = new FileInputStream(file);
        byte[] data = new byte[io.available()];
        io.read(data);
        io.close();
        return data;
    }





}
