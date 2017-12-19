package insideClass.applicationScene.factoryMode;

/**
 * Created by 18435 on 2017/12/18.
 * ��������������ʹ�����ڲ��࣬���������ʵ�ֺ����ľ��幤������������ɾ������ʵ����������ڲ���ľ��幤����ȥ����һ��������Ķ����⵱Ȼ���׵öࡣ
 * ��Ȼ��Ҫÿһ�������඼����һ�����幤���࣬�����ھ��幤������һ���ڲ��࣬
 * ����Ҳ�������ž���������Ӷ����������µĹ����࣬ʹ�ô��뿴������ӷ�ף���Ҳ�Ǳ��������ò�ʹ���ڲ����һ��ԭ��ɡ�
 */
public class Test {
    public static void main(String[] args) {
        String[] ids = new String[]{"Circle", "Square", "Square", "Circle"};
        for (int i = 0; i < ids.length; i++) {
            Shape shape = ShapeFactory.createShape(ids[i]);
            shape.draw();
            shape.erase();
        }
    }
}
