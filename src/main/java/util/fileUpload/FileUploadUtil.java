package util.fileUpload;


import util.CommonUtils;
import util.FileUtil;
import util.ImageUtil;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

/**
 * 文件上传
 *
 * @author liujiegang
 */
public class FileUploadUtil {

    /**
     * 保存原始图片以及压缩后的图片
     *
     * @param originalImage
     * @param path
     * @param width
     * @param height
     * @return
     * @throws Exception
     */
    public static UploadedFile saveImageAndZoom(File originalImage, String path,
                                                Integer width, Integer height) throws Exception {
        String fileName = FileUtil.getCharAndNumr(11) + ".JPG";
        String zoomFileName = "zoom_" + fileName;
        FileUtil.saveFileFromFile(originalImage, path, fileName);
        ImageUtil.zoomOutImage(originalImage, new File(path + File.separator + zoomFileName), width, height);
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setOriginalFile(fileName);
        uploadedFile.setZoomFile(zoomFileName);
        return uploadedFile;
    }

    /**
     * 保存图片
     *
     * @param originalImage
     * @param path
     * @return
     * @throws Exception
     */
    public static UploadedFile saveImage(File originalImage, String path) throws Exception {
        String fileName = UUID.randomUUID().toString() + ".JPG";
        FileUtil.saveFileFromFile(originalImage, path, fileName);
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setOriginalFile(fileName);
        return uploadedFile;
    }

    /**
     * 保存图片
     *
     * @param inputStream
     * @param path
     * @return
     * @throws Exception
     */
    public static String saveImageFromInputStram(InputStream inputStream, String path) throws Exception {
        String fileName = UUID.randomUUID().toString() + ".JPG";
        try {
            String filePath = path + fileName;
            File outputFile = new File(filePath);
            FileUtil.saveFileFromInputStream(inputStream, outputFile);
            return fileName;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * 保存图片
     *
     * @param base64
     * @param path
     * @return
     * @throws Exception
     */
    public static String saveImageFromBase64(String base64, String path) throws Exception {
        String fileName = UUID.randomUUID().toString() + ".JPG";
        try {
            String filePath = path + fileName;
            File file = new File(filePath);
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            String cameraPhoto = base64;
            cameraPhoto = CommonUtils.DelFormat(cameraPhoto);  //消除换行和空格
            ImageUtil.saveStrToFile(cameraPhoto, filePath);
            return fileName;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

}
