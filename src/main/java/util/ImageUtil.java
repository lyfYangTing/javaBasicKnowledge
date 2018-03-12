package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;

/**
 * Created by 18435 on 2018/3/12.
 */
public class ImageUtil {
    private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    /**
     * 将字符串转换为图片
     *
     * @param dataStr
     * @param fileUrl
     * @return
     * @throws java.text.ParseException
     */
    public static String saveStrToFile(String dataStr, String fileUrl)
            throws ParseException {
        if (dataStr != null && !"".equals(dataStr) && fileUrl != null
                && !"".equals(fileUrl)) {

            // Base64解码
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] bytes;
            try {
                bytes = decoder.decodeBuffer(dataStr);
                if (bytes != null && bytes.length > 0) {
                    for (int i = 0; i < bytes.length; ++i) {
                        if (bytes[i] < 0) {// 调整异常数据
                            bytes[i] += 256;
                        }
                    }
                    OutputStream out = new FileOutputStream(fileUrl); // 生成图片
                    out.write(bytes);
                    out.flush();
                    out.close();
                    return "";
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            return null;
        }
        return "";
    }

    /**
     * 把图片写到磁盘上
     *
     * @param im       图片输入流
     * @param path     图片写入的目标文件夹地址
     * @param fileName 写入图片的名字
     * @return
     */
    public static boolean saveFileFromImage(BufferedImage im, String path,
                                            String fileName) {
        File f = new File(path + fileName);
        String fileType = FileUtil.getExtension(fileName);
        try {
            ImageIO.write(im, fileType, f);
            im.flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 生成指定宽高的图片
     *
     * @param originalImage 原始图片
     * @param width         高
     * @param height        宽
     * @return BufferedImage
     */
    public static BufferedImage getImage(final BufferedImage originalImage,
                                         final int width, final int height) {
        BufferedImage newImage = new BufferedImage(width, height,
                originalImage.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return newImage;
    }

    /**
     * 生成缩放图片
     *
     * @param originalImage 原始图片
     * @param width         宽度
     * @param height        高度
     * @return BufferedImage
     */
    public static BufferedImage zoomOutImage(final BufferedImage originalImage,
                                             final Integer width, final Integer height) {
        float originalWidth = (float) originalImage.getWidth();
        float originalHeight = (float) originalImage.getHeight();
        float newWidth = (float) width;
        float newHeight = (float) height;

        float wTimes = 1, hTimes = 1;
        if (newWidth < originalWidth) {
            wTimes = originalWidth / width;
        }

        if (newHeight < originalHeight) {
            hTimes = originalHeight / height;
        }

        if (wTimes < hTimes && hTimes > 1) {
            return getImage(originalImage, (int) (originalWidth / hTimes),
                    height);
        }

        if (hTimes < wTimes && wTimes > 1) {
            return getImage(originalImage, width,
                    (int) (originalHeight / wTimes));
        }

        return originalImage;
    }

    /**
     * 生成缩放图片
     *
     * @param originalImage 原始图片
     * @param newImage      生成图片
     * @param width         宽度
     * @param height        高度
     * @return boolean
     * @throws IOException 异常
     */
    public static boolean zoomOutImage(final File originalImage,
                                       final File newImage, final Integer width, final Integer height)
            throws IOException {
        try {
            BufferedImage bufferedImage = null;
            if (originalImage.canRead()) {
                bufferedImage = ImageIO.read(originalImage);
            }
            if (bufferedImage != null) {
                bufferedImage = zoomOutImage(bufferedImage, width, height);
                ImageIO.write(bufferedImage, "JPG", newImage);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 保存原始图片
     *
     * @param originalImage 原始图片
     * @param newImage      生成图片
     * @return boolean
     * @throws IOException 异常
     */
    public static boolean saveImage(final File originalImage,
                                    final File newImage, final String convertType)
            throws IOException {
        try {
            BufferedImage bufferedImage = null;
            if (originalImage.canRead()) {
                bufferedImage = ImageIO.read(originalImage);
            }
            if (bufferedImage != null) {
                ImageIO.write(bufferedImage, convertType, newImage);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
