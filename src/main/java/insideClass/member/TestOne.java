package insideClass.member;

/**
 * Created by 18435 on 2017/12/14.
 */
public class TestOne {
    public static void main(String[] args) {
        //��Ա�ڲ����������ⲿ������ڵģ�Ҳ����˵�����Ҫ������Ա�ڲ���Ķ���ǰ���Ǳ������һ���ⲿ��Ķ���
        //������Ա�ڲ������ ��ʽ1
        Circle circle = new Circle();
        Circle.Draw draw = circle.new Draw();
        //������Ա�ڲ������ ��ʽ2 ǰ�᣺�ⲿ������ṩһ�����������ڻ�ȡ�ڲ������
        Circle.Draw draw1 = circle.getDrawInstance();
    }
}
