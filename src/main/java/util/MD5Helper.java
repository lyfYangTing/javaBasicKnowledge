package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/**
 * Created by 18435 on 2018/3/12.
 */
public class MD5Helper {
    private static Logger logger = LoggerFactory.getLogger(MD5Helper.class);
    private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    public static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            logger.info("原始byte值："+ b[i]);
            resultSb.append(byteToHexString(b[i]));
            logger.info("转换为十六进制的值："+ byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }


    /**
     * MD5字符转换
     *
     * @param origin
     *
     * @param origin string
     *
     * @return
     */
    public static String MD5Encode(String origin) {
        String resultString = null;

        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString
                    .getBytes()));
        } catch (Exception ex) {

        }
        return resultString;
    }
}
