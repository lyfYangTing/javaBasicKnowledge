package util;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

/**
 * Created by ipaynow on 2017/8/21.
 */
public class SftpUtil {
    private ChannelSftp channel;
    private final Logger logger = LoggerFactory.getLogger(SftpUtil.class);
    private String host;
    private String username;
    private String password;
    private int port = 26;
    private ChannelSftp sftp = null;
    private Session sshSession = null;

    public SftpUtil() {
    }

    public SftpUtil(String host, String username, String password, int port) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.port = port;
    }

    public SftpUtil(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
    }

    public void connect() {
        try {
            JSch e = new JSch();
            e.getSession(this.username, this.host, this.port);
            this.sshSession = e.getSession(this.username, this.host, this.port);//根据用户名，主机ip和端口获取一个Session对象
            this.sshSession.setPassword(this.password);//设置密码
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            this.sshSession.setConfig(sshConfig);//为Session对象设置properties
            this.sshSession.connect();//通过Session建立连接
            Channel channel = this.sshSession.openChannel("sftp");
            channel.connect();
            this.sftp = (ChannelSftp) channel;
        } catch (Exception var4) {
            this.logger.error("SFTP连接时发生异常", var4);
        }
    }

    public void disconnect() {
        if (this.sftp != null && this.sftp.isConnected()) {
            this.sftp.disconnect();
        }
        if (this.sshSession != null && this.sshSession.isConnected()) {
            this.sshSession.disconnect();
        }
    }


    public InputStream downFile(String remotePath, String remoteFile) {
        try {
            this.sftp.cd(remotePath);
            return this.sftp.get(remoteFile);
        } catch (SftpException var4) {
            this.logger.error("文件下载失败或文件不存在！", var4.getMessage());
            return null;
        }
    }

    public boolean batchDownLoadFile(String remotPath, String localPath, String fileFormat, boolean del) {
        try {
            this.connect();
            Vector e = this.listFiles(remotPath);
            if (e.size() > 0) {
                Iterator it = e.iterator();

                while (true) {
                    while (true) {
                        String filename;
                        SftpATTRS attrs;
                        do {
                            if (!it.hasNext()) {
                                return false;
                            }

                            ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) it.next();
                            filename = entry.getFilename();
                            attrs = entry.getAttrs();
                        } while (attrs.isDir());

                        if (fileFormat != null && !"".equals(fileFormat.trim())) {
                            if (filename.startsWith(fileFormat) && this.downloadFile(remotPath, filename, localPath, filename) && del) {
                                this.deleteSFTP(remotPath, filename);
                            }
                        } else if (this.downloadFile(remotPath, filename, localPath, filename) && del) {
                            this.deleteSFTP(remotPath, filename);
                        }
                    }
                }
            }
        } catch (SftpException var13) {
            var13.printStackTrace();
        } finally {
            this.disconnect();
        }

        return false;
    }

    public boolean batchDownLoadFileFileNameContain(String remotPath, String localPath, String contain, boolean del) {
        try {
            int count = 0;
            Boolean flag = true;
            this.connect();
            Vector e = this.listFiles(remotPath);
            if (e.size() > 0) {
                Iterator it = e.iterator();
                while (it.hasNext()) {
                    String filename = "";
                    SftpATTRS attrs;
                    do {
                        if (!it.hasNext()) {
                            break;
                        }
                        ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) it.next();
                        filename = entry.getFilename();
                        attrs = entry.getAttrs();
                    } while (attrs.isDir());

                    if (contain != null && !"".equals(contain.trim())) {
                        if (filename.contains(contain)) {
                            if (this.downloadFile(remotPath, filename, localPath, filename)) {
                                count++;
                                if (del) {
                                    this.deleteSFTP(remotPath, filename);
                                }
                            } else {
                                logger.error("下载文件" + filename + "失败!");
                                flag = false;
                            }
                        }

                    } else if (this.downloadFile(remotPath, filename, localPath, filename)) {
                        count++;
                        if (del) {
                            this.deleteSFTP(remotPath, filename);
                        }
                    } else {
                        logger.error("下载文件" + filename + "失败!");
                        flag = false;
                    }
                }
            }

            if (flag && count > 0) {
                return true;
            } else {
                return false;
            }

        } catch (SftpException var13) {
            var13.printStackTrace();
        } finally {
            this.disconnect();
        }

        return false;
    }


    public boolean downloadFile(String remotePath, String remoteFileName, String localPath, String localFileName) {
        FileOutputStream fos = null;
        try {
            this.connect();
            this.sftp.cd(remotePath);
            fos = new FileOutputStream(localPath + localFileName);
            this.mkdirs(localPath + localFileName);
            this.sftp.get(remoteFileName, fos);
            return true;
        } catch (FileNotFoundException var6) {
            var6.printStackTrace();
        } catch (SftpException var7) {
            var7.printStackTrace();
        } finally {
            this.disconnect();
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public boolean isFileExist(String filePath, String fileName) throws SftpException {
        boolean isExitFlag = false;
        if (!this.isDirExist(filePath)) {
            return isExitFlag;
        } else {
            String fileFullName = null;
            char lastPathChar = filePath.charAt(filePath.length() - 1);
            if (File.separatorChar != lastPathChar && 47 != lastPathChar) {
                fileFullName = filePath + "/" + fileName;
            } else {
                fileFullName = filePath + fileName;
            }

            try {
                InputStream ex = this.sftp.get(fileFullName);
                return ex != null;
            } catch (SftpException var7) {
                if (var7.id == 2) {
                    return false;
                } else {
                    throw var7;
                }
            }
        }
    }

    public void uploadFile(String remotePath, String fileName, InputStream input) throws IOException, Exception {
        try {
            this.sftp.cd(remotePath);
            this.sftp.put(input, fileName);
        } catch (Exception var12) {
            this.logger.error("文件上传异常！", var12);
            var12.printStackTrace();
            throw new Exception("文件上传异常");
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (Exception var11) {
                    ;
                }
            }

            this.disconnect();
        }

    }

    public boolean uploadFile(String remotePath, String remoteFileName, String localPath, String localFileName) {
        FileInputStream in = null;

        try {
            this.createDir(remotePath);
            File e = new File(localPath + localFileName);
            in = new FileInputStream(e);
            this.sftp.put(in, remoteFileName);
            boolean var7 = true;
            return var7;
        } catch (FileNotFoundException var19) {
            var19.printStackTrace();
        } catch (SftpException var20) {
            var20.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException var18) {
                    var18.printStackTrace();
                }
            }

        }

        return false;
    }

    public boolean uploadFile(String remotePath, String remoteFileName, File fileInput) {
        FileInputStream in = null;

        try {
            this.createDir(remotePath);
            in = new FileInputStream(fileInput);
            this.sftp.put(in, remoteFileName);
            boolean e = true;
            return e;
        } catch (FileNotFoundException var17) {
            var17.printStackTrace();
        } catch (SftpException var18) {
            var18.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException var16) {
                    var16.printStackTrace();
                }
            }

        }

        return false;
    }

    public boolean bacthUploadFile(String remotePath, String localPath, boolean del) {
        try {
            this.connect();
            File e = new File(localPath);
            File[] files = e.listFiles();

            for (int i = 0; i < files.length; ++i) {
                if (files[i].isFile() && files[i].getName().indexOf("bak") == -1 && this.uploadFile(remotePath, files[i].getName(), localPath, files[i].getName()) && del) {
                    this.deleteFile(localPath + files[i].getName());
                }
            }

            boolean var12 = true;
            return var12;
        } catch (Exception var10) {
            var10.printStackTrace();
        } finally {
            this.disconnect();
        }

        return false;
    }

    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return !file.exists() ? false : (!file.isFile() ? false : file.delete());
    }

    public boolean createDir(String createpath) {
        try {
            if (this.isDirExist(createpath)) {
                this.sftp.cd(createpath);
                return true;
            } else {
                String[] e = createpath.split("/");
                StringBuffer filePath = new StringBuffer("/");
                String[] arr$ = e;
                int len$ = e.length;

                for (int i$ = 0; i$ < len$; ++i$) {
                    String path = arr$[i$];
                    if (!path.equals("")) {
                        filePath.append(path + "/");
                        if (this.isDirExist(filePath.toString())) {
                            this.sftp.cd(filePath.toString());
                        } else {
                            this.sftp.mkdir(filePath.toString());
                            this.sftp.cd(filePath.toString());
                        }
                    }
                }

                this.sftp.cd(createpath);
                return true;
            }
        } catch (SftpException var8) {
            var8.printStackTrace();
            return false;
        }
    }

    public boolean isDirExist(String directory) {
        boolean isDirExistFlag = false;

        try {
            SftpATTRS e = this.sftp.lstat(directory);
            isDirExistFlag = true;
            return e.isDir();
        } catch (Exception var4) {
            if (var4.getMessage().toLowerCase().equals("no such file")) {
                isDirExistFlag = false;
            }

            return isDirExistFlag;
        }
    }

    public void deleteSFTP(String directory, String deleteFile) {
        try {
            this.sftp.cd(directory);
            this.sftp.rm(deleteFile);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public void mkdirs(String path) {
        File f = new File(path);
        String fs = f.getParent();
        f = new File(fs);
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    public Vector listFiles(String directory) throws SftpException {
        return this.sftp.ls(directory);
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ChannelSftp getSftp() {
        return this.sftp;
    }

    public void setSftp(ChannelSftp sftp) {
        this.sftp = sftp;
    }

    public static void main(String[] args) {
        diaoyong();
    }

    private static void diaoyong() {
        SftpUtil ftp = new SftpUtil("202.106.235.34", "t1park", "t1park", 10024);
        String localPath = "F:\\check\\";
        String remotePath = "/CHECK";
        ftp.connect();
//        InputStream is = ftp.downloadFileToStream(remotePath,"999110101020024_20181102_VehplatePay.txt");
//        String data = "";
//        try {
//            byte[] getData = readInputStream(is);
//            data = new String(getData,"GBK");
//
//            FileWriter fw = new FileWriter(localPath+"999110101020024_20181102_VehplatePay.txt");
//            fw.write(data);
//            fw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //ftp.batchDownLoadFileFileNameContain(remotePath,localPath,"VehplatePay",false);
        ftp.downloadFile(remotePath, "999110101020024_20181102_VehplatePay.txt", localPath, "999110101020024_20181102_VehplatePay.txt");
        ftp.disconnect();
        System.exit(0);
    }

    public InputStream downloadFileToStream(String remotePath, String remoteFileName) {
        InputStream in = null;

        try {
            this.sftp.cd(remotePath);
            in = this.sftp.get(remoteFileName);
        } catch (SftpException var5) {
            var5.printStackTrace();
        }

        return in;
    }

    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    public boolean uploadFile(String localPath, InputStream in) {
        try {
            File e = new File(localPath);
            if (!e.exists()) {
                e.createNewFile();
            }

            this.sftp.put(in, localPath);
            boolean var4 = true;
            return var4;
        } catch (FileNotFoundException var18) {
            var18.printStackTrace();
        } catch (SftpException var19) {
            var19.printStackTrace();
        } catch (IOException var20) {
            var20.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException var17) {
                    var17.printStackTrace();
                }
            }
        }
        return false;
    }
}
