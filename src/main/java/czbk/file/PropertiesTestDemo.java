package czbk.file;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by 18435 on 2018/11/22.
 *
 * 用于记录应用程序运行次数，如果次数已经达到，给出注册提醒
 *
 * 计数器：定义在程序中 随着程序的运行在内存中存在  随着程序结束而消失  再次重启程序会从初始值0开始计数  不满足需求
 *
 * 将次数保存在一个文件中：程序结束，该计数器的值也存在
 * 下次程序启动会先加载该计数器的值并加1后再重新存储起来
 *
 * dom4j 处理xml文件
 *
 * */
public class PropertiesTestDemo {

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        File file = new File("properties","count.properties");
        if(!file.exists()){
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
        System.out.println("文件绝对路径："+ file.getAbsolutePath());
        FileInputStream fis = new FileInputStream(file);
        int count = 0;
        properties.load(fis);
        String value = properties.getProperty("count");
        if(StringUtils.isNotEmpty(value)){
            count = Integer.parseInt(value);
            if(count>=5){
                System.out.println("您好，使用次数已到，请注册");
            }
            return;
        }
        properties.setProperty("count",String.valueOf(count+1));
        FileOutputStream fos = new FileOutputStream(file);
        properties.store(fos,"count");
        System.out.println("count:"+(count+1));
    }


}
