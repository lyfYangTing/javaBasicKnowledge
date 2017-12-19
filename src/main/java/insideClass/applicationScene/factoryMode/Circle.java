package insideClass.applicationScene.factoryMode;

/**
 * Created by 18435 on 2017/12/18.
 */
public class Circle implements Shape {
    public void draw() {
        System.out.println("the circle is drawing...");
    }

    public void erase() {
        System.out.println("the circle is erasing...");
    }

    /**
     * �ڲ���Factory�õúá���һ�أ������ֻ��һ���£����ǲ���һ��Circle�������������޹أ�����һ����Ҳ��������ʹ���ڲ����������
     * �ڶ��أ����Factory����Ҫ�Ǿ�̬�ģ���Ҳ��Ҫ������ʹ���ڲ��࣬��Ȼ�������ShapeFacotry.addFactory��û�취add�ˡ�
     * �������Ǹ���̬������������������Ĺ�������ӵ�����Ĺ�������ȥ��
     * �ڳ��󹤳������Class.forName�ͻ�ִ�������̬�������ˡ�
     */
    private static class Factory extends ShapeFactory {
        protected Shape create() {
            return new Circle();
        }
    }

    static {
        ShapeFactory.addFactory("Circle", new Factory());
    }


}
