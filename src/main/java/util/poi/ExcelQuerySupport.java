package util.poi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.poi.exception.PoiExtractException;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by 18435 on 2018/3/30.
 */
public class ExcelQuerySupport {

    private Logger logger = LoggerFactory.getLogger(ExcelQuerySupport.class);


    /**
     * 精准查询
     * @param originList  原始文件数据
     * @param t   查询条件
     * @param <T>
     * @return
     */
    public static <T> List<T> precisionQuery(List<T> originList,T t){
        List<T> resultList = new ArrayList<T>(); //查询结果
        for(T t1 :originList){//遍历原始数据
            if(comparisonObject(t1,t)){
                resultList.add(t1);
            }
        }
        return resultList;
    }

    /**
     * 区间查询  按日期类型找区间
     * @param originList     原始文件数据
     * @param queryFieldName   查询字段名
     * @param startDate   开始日期
     * @param endDate     结束日期
     * @param <T>     验证对象类型
     * @return
     */
    public static <T> List<T> intervalQuery(List<T> originList, String queryFieldName, Date startDate, Date endDate, Class<T> tClass) throws PoiExtractException{
        long start = startDate==null ? null :startDate.getTime();
        long end = endDate ==null ? null :endDate.getTime();
        return intervalQuery(originList,queryFieldName,(double)start,(double)end,tClass);
    }

    /**
     * 区间查询  按数值类型找区间
     * @param originList     原始文件数据
     * @param queryFieldName   查询字段名
     * @param start   区间起始值
     * @param end     区间结束值
     * @param <T>     验证对象类型
     * @return
     */
    public static <T> List<T> intervalQuery(List<T> originList,String queryFieldName,Double start,Double end,Class<T> tClass) throws PoiExtractException{
        List<T> resultList = new ArrayList<>();
        String[] FieldNames = getFieldNameList(tClass);
        boolean flag = Arrays.asList(FieldNames).contains(queryFieldName);
        if(!flag){
            throw new PoiExtractException("查询字段 queryField ："+queryFieldName+"与实体中的属性名 ："+ FieldNames.toString() +"匹配失败 ");
        }
        try {
            Field queryField = tClass.getDeclaredField(queryFieldName);
            queryField.setAccessible(true);
            for(T origin : originList){
                Object object = queryField.get(origin);//取到了属性值
                //判断这个值 在不在 start - end 的区间内
                if(interval(object,start,end)){
                    resultList.add(origin);
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    /**
     * 判断origin这个值 是否在 start - end 之间 start,end 为double类型
     * @param origin
     * @param start
     * @param end
     * @return
     */
    public static boolean interval(Object origin,Double start,Double end){
        boolean originType = dataType(origin);
        if(originType){
            Double originDouble = Double.parseDouble(String.valueOf(origin)) ;
            if(start!=null){
                if(end!=null){//end不为空  且是数值类型
                    return (originDouble <=  end) && (originDouble >=  start);
                }else{
                    return originDouble >=  start;
                }
            }else {
                return originDouble <=  end;
            }
        }else {
            return false;
        }
    }

    /**
     * 判断Object 是否为数值类型
     * @param object
     * @return
     */
    public static boolean dataType(Object object){
        if(object instanceof Integer){
            return true;
        }else if(object instanceof Long){
            return true;
        }else if(object instanceof Float){
            return true;
        }else if(object instanceof Double){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 获取类中所有属性名(包括private,public......)
     * @param clazz
     * @return
     */
    private static String[] getFieldNameList(Class clazz){
        Field[] fields = clazz.getDeclaredFields();
        String[] fieldNames = new  String[fields.length];
        for(int i=0;i<fields.length;i++){
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }

    /**
     * 比较两个对象中的非空属性值是否相同
     * @param originObject
     * @param paramsObject
     * @param <T>
     * @return
     */
    private static <T> boolean  comparisonObject(T originObject,T paramsObject){
        String[] fieldNames = getFieldNameList(paramsObject.getClass());
        try{
            for(int i=0;i<fieldNames.length;i++){
                Field paramsField = paramsObject.getClass().getDeclaredField(fieldNames[i]);
                paramsField.setAccessible(true);
                Object paramsFieldValue = paramsField.get(paramsObject);//查询条件对应的属性值不为空
                if(paramsFieldValue != null){//比较list集合中的数据 和  查询条件中的值
                    Field originField = paramsObject.getClass().getDeclaredField(fieldNames[i]);
                    originField.setAccessible(true);
                    Object originFieldValue = originField.get(originObject);//查询条件对应的属性值
                    if(! compareTwo(paramsFieldValue,originFieldValue)){
                        return false;
                    }
                }
            }
        }catch (NoSuchFieldException e) {
            e.printStackTrace();
        }catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 对比两个数据是否内容相同
     *
     * @param  object1,object2
     * @return boolean类型
     */
    public static boolean compareTwo(Object object1,Object object2){
        if(object1==null&&object2==null){
            return true;
        }
        if(object1==null&&object2!=null){
            return false;
        }
        if(object1.equals(object2)){
            return true;
        }
        return false;
    }

    public static void main(String[] args){
        File file = new File("F://gitCloneProject/javaBasicKnowledge/resources/logExcel/2018-03-29数据.xls");
        Person person = new Person();
        List<Person> originList = ExcelExtractForEntityUtil.getObjectData(file,0,1,Person.class);
        Person paramPerson = new Person();
        List<String> imgURL = new ArrayList<>();
        imgURL.add("urlOne");
        imgURL.add("urlTwo");
        paramPerson.setImgURl(imgURL);
//        String date = "2018-03-29 00:00:00";
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date1 = null;
//        try {
//            date1 = sdf.parse(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        List<Person> list = ExcelQuerySupport.intervalQuery(originList,"outTime",date1,new Date(),Person.class);
        List<Person> list = ExcelQuerySupport.precisionQuery(originList,paramPerson);
        for(Person person1 : list){
            System.out.println(person1);
        }
    }
}
