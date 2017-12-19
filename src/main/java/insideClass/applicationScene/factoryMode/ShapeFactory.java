package insideClass.applicationScene.factoryMode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 18435 on 2017/12/18.
 * 模板方法模式的运用
 */
public abstract class ShapeFactory {

    protected abstract Shape create();//用来产生具体类的对象，留交各具体工厂实现去实现。

    private static Map factories = new HashMap();//用来存放具体工厂的实现以及他们的ID号。

    public static void addFactory(String id, ShapeFactory f) {//使用来增加一个具体工厂的实现
        factories.put(id, f);
    }

    public static final Shape createShape(String id) {//用来获取具体对象，里面的那个Class.forName……的作用是调用以ID号为类名的类的一些静态的东西。
        if (!factories.containsKey(id)) {
            try {
                Class.forName("insideClass.applicationScene.factoryMode." + id);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Bad shape creation : " + id);
            }
        }
        return ((ShapeFactory) factories.get(id)).create();
    }

}
