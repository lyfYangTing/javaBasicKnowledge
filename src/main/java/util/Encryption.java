package util;

import java.util.Random;

/**
 * Created by 18435 on 2018/3/13.
 * 数据加密 解密
 */
public class Encryption {
    /**
     * 获取MD5加密后的用户密码
     *
     * @param password  用户原始密码
     * @return
     */
    public static String getUserPasswordWithMD5(String password) {
        return MD5Helper.MD5Encode("Infofuse" + password + "WeiMsg");
    }

    /**
     * 随机生成length位数字手机验证码
     * @param length length最大值为10
     * @return
     */
    public static String generateValidateCode(int length) {
        int[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Random random = new Random();
        for (int i = 10; i > 1; i--) {//意在打乱数组中元素的顺序
            int index = random.nextInt(i);
            int tmp = array[index];
            array[index] = array[i - 1];
            array[i - 1] = tmp;
        }
        StringBuffer sb = new StringBuffer();
        length = length > 10 ? 10 : length;
        for (int i = 0; i < length; i++) {
            sb.append(array[i]);
        }
        return sb.toString();
    }

    /**
     * 随机生成六位数字手机验证码
     *
     * @return
     */
    public static String generate6ValidateCode() {
        int[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Random random = new Random();
        for (int i = 10; i > 1; i--) {//交换 array[index] 和 array[i-1] 的值  这样做可以保证数字的位置发生变化，但是不会产生相同的数字
            int index = random.nextInt(i);//直接用随机产生的数字可能会有重复
            int tmp = array[index];
            array[index] = array[i - 1];
            array[i - 1] = tmp;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            sb.append(array[i]);
        }
        return sb.toString();
    }

    public static void main(String[] args){
        System.out.println(Encryption.generateValidateCode(11));
    }
}
