package insideClass.interview.interviewTwo;

/**
 * Created by 18435 on 2017/12/19.
 */
public class Outter {
    private int a = 1;
    class Inner {
        private int a = 2;
        public void print() {
            int a = 3;
            System.out.println("�ֲ�������" + a);
            System.out.println("�ڲ��������" + this.a);
            System.out.println("�ⲿ�������" + Outter.this.a);
        }
    }
}
