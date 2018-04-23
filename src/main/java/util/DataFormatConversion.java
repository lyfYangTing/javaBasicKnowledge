package util;

import org.apache.commons.collections.map.HashedMap;
import util.poi.Person;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 18435 on 2018/4/2.
 * 数据格式转换工具
 */
public class DataFormatConversion {


    public static  <T> T mapToObject(Map<String, Object> map, Class<T> beanClass) throws Exception {
        if (map == null)
            return null;
        T obj = beanClass.newInstance();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){
                continue;
            }
            field.setAccessible(true);
            field.set(obj, map.get(field.getName()));
        }
        return obj;
    }

    public static Map<String, Object> objectToMap(Object obj) throws Exception {
        if(obj == null){
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }
        return map;
    }

    public static void main(String[] args){
        Map<String,Object> map = new HashedMap();
        map.put("name","张三");
        map.put("age",12);
        map.put("inTime",1212332121l);
        try {
            Person person = DataFormatConversion.mapToObject(map,Person.class);
            System.out.println(person);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
