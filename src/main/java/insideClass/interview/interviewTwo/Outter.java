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
            System.out.println("局部变量：" + a);
            System.out.println("内部类变量：" + this.a);
            System.out.println("外部类变量：" + Outter.this.a);
        }
    }
}
