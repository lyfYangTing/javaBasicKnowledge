package redisTest.search;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 18435 on 2018/7/18.
 */
public class SearchTest {


    /**
     *
     * @param prefix    需要转换的前缀 最多6个字符    因为有序集合集分值是以IEEE 754双精度浮点格式存储，所以转换操作最大只能64位
     * @param ignoreCase   是否以大小写无关的
     * @return
     */
    public double stringToScore(String prefix,boolean ignoreCase){
        if(ignoreCase){
            prefix = prefix.toLowerCase();
        }
        //把字符串的前6个字符转换成相应的数字值，比如把空字节转换成0、制表符（tab）转换成9 等等
        List<Integer> asciiList = new ArrayList<>();
        char[] prefixChars = prefix.toCharArray();
        int length = prefixChars.length > 6 ? 6 : prefixChars.length;
        for (int i = 0 ;i < length ; i++){
            asciiList.add((int) prefixChars[i]);
        }
        while (prefixChars.length < 6){
            asciiList.add(-1);//长度不足6个字符串添加占位符，以此来表示这是一个短字符串
        }

        //计算分值
        int score = 0;
        for(int value : asciiList){
            score = score * 257 + value + 1;
        }
        return score * 2 ;
    }


    public static void main(String[] args){
       SearchTest searchTest = new SearchTest();
       searchTest.stringToScore("aAAcDD",true);
    }
}
