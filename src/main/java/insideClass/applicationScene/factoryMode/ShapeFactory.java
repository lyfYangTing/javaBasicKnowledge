package insideClass.applicationScene.factoryMode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 18435 on 2017/12/18.
 * ģ�巽��ģʽ������
 */
public abstract class ShapeFactory {

    protected abstract Shape create();//��������������Ķ������������幤��ʵ��ȥʵ�֡�

    private static Map factories = new HashMap();//������ž��幤����ʵ���Լ����ǵ�ID�š�

    public static void addFactory(String id, ShapeFactory f) {//ʹ��������һ�����幤����ʵ��
        factories.put(id, f);
    }

    public static final Shape createShape(String id) {//������ȡ�������������Ǹ�Class.forName�����������ǵ�����ID��Ϊ���������һЩ��̬�Ķ�����
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
