package util.fileUpload;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lc on 2017/6/1.
 */
public class HttpURLFileDownloadUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpURLFileDownloadUtil.class);

    public static boolean saveURLPic(String url, String rootPath, String fileName){
        boolean flag = false;
        try {
            if(StringUtils.isNotEmpty(url)){
                File file = new File(rootPath, fileName);
                for (int i = 0; i < 5; i++){
                    if(HttpURLFileDownloadUtil.getInputStreamByGet(url,file)){
                        flag = true;
                        break;
                    }
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage() , e);
        }
        return flag;
    }

    public static boolean getInputStreamByGet(String url,File file) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url)
                    .openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");

            logger.info("ConnResponseCode：" + conn.getResponseCode());

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = conn.getInputStream();
                boolean b = HttpURLFileDownloadUtil.saveData(inputStream,file);
                inputStream.close();
                return b;
            }
            else {
                logger.error("图片下载conn连接失败！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 将服务器响应的数据流存到本地文件
    public static boolean saveData(InputStream is, File file) {
        try (BufferedInputStream bis = new BufferedInputStream(is);
             BufferedOutputStream bos = new BufferedOutputStream(
                     new FileOutputStream(file));) {
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
                bos.flush();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}
