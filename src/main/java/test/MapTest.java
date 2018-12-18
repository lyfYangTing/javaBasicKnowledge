package test;

import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

/**
 * Created by 18435 on 2018/8/29.
 */
public class MapTest {
    public static void addMap(Map<String,String> map){
        map.put("1","测试1");
        map.put("2","测试2");
    }

    public static void main(String[] args) {
        Map<String,String> paramMap = new HashedMap();
        paramMap.put("0","参数");
        MapTest.addMap(paramMap);
        System.out.println(paramMap.size());
    }
}
