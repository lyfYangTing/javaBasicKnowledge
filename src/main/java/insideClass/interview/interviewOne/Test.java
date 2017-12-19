package insideClass.interview.interviewOne;

/**
 * Created by 18435 on 2017/12/19.
 */
public class Test {
    class Bean1{
        public int I = 0;
    }

    static class Bean2{
        public int J = 0;
    }

    public static void main(String[] args) {
        // 初始化Bean1 成员内部类，必须先产生外部类的实例化对象，才能产生内部类的实例化对象。
        Test.Bean1 bean1 = new Test().new Bean1();
        bean1.I++;
        // 初始化Bean2 静态内部类不用产生外部类的实例化对象即可产生内部类的实例化对象。
        Test.Bean2 bean2 = new Test.Bean2();
        bean2.J++;
        //初始化Bean3 成员内部类，必须先产生外部类的实例化对象，才能产生内部类的实例化对象。
        Bean.Bean3 bean3 = new Bean().new Bean3();
        bean3.k++;
    }
}
