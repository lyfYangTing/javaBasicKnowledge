package insideClass.member;

/**
 * Created by 18435 on 2017/12/14.
 * ��Ա�ڲ���:��Ա�ڲ���������ͨ���ڲ��࣬���Ķ���Ϊλ����һ������ڲ���
 * ��Ա�ڲ�����������������ⲿ������г�Ա���Ժͳ�Ա����������private��Ա�;�̬��Ա����
 */
public class Circle {//�ⲿ��
    private Draw draw = null;
    private double radius = 0;
    private String name = "�ⲿ��";
    static int count = 1;

    public Circle(){}

    public Circle(double radius){
        this.radius = radius;
    }

    /**
     * �ⲿ����ʳ�Ա�ڲ����Ա:
     * �����ȴ���һ����Ա�ڲ���Ķ�����ͨ��ָ��������������������
     */
    public void accessDraw(){
        getDrawInstance().drawSahpe();
    }

    /**
     * �����ڲ������
     * @return
     */
    public Draw getDrawInstance(){
        if(draw==null){
            return new Draw();
        }
        return draw;
    }


    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    class Draw{//�ڲ���
        private String name = "�ڲ���";

        /**
         * ��Ա�ڲ�����ʳ�Ա�ⲿ��ʱ:
         * 1.��Ա�ڲ�����������������ⲿ������г�Ա���Ժͳ�Ա����(����private��Ա�;�̬��Ա)
         * 2.����Ա�ڲ���ӵ�к��ⲿ��ͬ���ĳ�Ա�������߷���ʱ���ᷢ���������󣬼�Ĭ������·��ʵ��ǳ�Ա�ڲ���ĳ�Ա��
         *   ���Ҫ�����ⲿ���ͬ����Ա����Ҫ���������ʽ���з��ʣ��ⲿ��.this.��Ա����   �ⲿ��.this.��Ա����
         */
        public void drawSahpe() {
            System.out.println("drawshape");
            System.out.println("�����ⲿ���˽�г�Ա-------->"+radius);
            System.out.println("�����ⲿ��ľ�̬��Ա-------->"+count);
            System.out.println("�����ڲ������ⲿ��ͬ���� �ڲ����Ա---->"+name);
            System.out.println("�����ڲ������ⲿ��ͬ���� �ⲿ���Ա---->"+Circle.this.name);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
