package util;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 压缩解压ZIP文件
 *
 * @author Administrator
 */
public class AntZip {
    private ZipFile zipFile;
    private static int bufSize;    //size of bytes  
    private byte[] buf;
    private int readedBytes;

    /**
     * @param bufSize 缓存大小
     */
    public AntZip(int bufSize) {
        this.bufSize = bufSize;
        this.buf = new byte[this.bufSize];
    }

    public AntZip() {
        this(1024);
    }

    /**
     * 生存目录
     *
     * @param directory    解压文件存放目录
     * @param subDirectory 子目录（没有时可传入空字符串）
     */
    private void createDirectory(String directory, String subDirectory) {
        String dir[];
        File fl = new File(directory);
        try {
            if (subDirectory == "" && fl.exists() != true)
                fl.mkdirs();
            else if (subDirectory != "") {
                dir = subDirectory.replace('\\', '/').split("/");
                for (int i = 0; i < dir.length; i++) {
                    File subFile = new File(directory + File.separator + dir[i]);
                    if (subFile.exists() == false)
                        subFile.mkdirs();
                    directory += File.separator + dir[i];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解压指定的ZIP文件
     *
     * @param unZipFileName   文件名字符串（包含路径）
     * @param outputDirectory 解压后存放目录
     * @return 解压后的文件名字
     */
    public List<String> unZip(String unZipFileName, String outputDirectory) {
        FileOutputStream fileOut;
        //File file;     
        InputStream inputStream;
        List<String> list = new ArrayList<String>();

        try {
            createDirectory(outputDirectory, "");
            this.zipFile = new ZipFile(unZipFileName, "GBK");
            for (Enumeration entries = this.zipFile.getEntries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (entry.isDirectory()) {
                    //是目录，则创建之  
                    String name = entry.getName().substring(0, entry.getName().length() - 1);
                    File f = new File(outputDirectory + File.separator + name);
                    f.mkdirs();
                    //file.mkdirs();  
                } else {
                    //是文件  
                    String fileName = entry.getName().replace('\\', '/');
                    list.add(outputDirectory + fileName);
                    //System.out.println(fileName);
                    if (fileName.indexOf("/") != -1) {
                        createDirectory(outputDirectory, fileName.substring(0, fileName.lastIndexOf("/")));
                        fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());

                    }
                    File f = new File(outputDirectory + File.separator + entry.getName());
                    f.createNewFile();
                    inputStream = zipFile.getInputStream(entry);
                    fileOut = new FileOutputStream(f);
                    while ((this.readedBytes = inputStream.read(this.buf)) > 0) {
                        fileOut.write(this.buf, 0, this.readedBytes);
                    }
                    fileOut.close();
                    inputStream.close();
                }
            }
            this.zipFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 解压指定ZIP文件
     *
     * @param unZipFile 需要解压的ZIP文件
     */
    public void unZip(File unZipFile) {
        String outputDirectory = new String("E:\\2016-11-18"); //解压后存放目录
        unZip(unZipFile.toString(), outputDirectory);
    }

}  