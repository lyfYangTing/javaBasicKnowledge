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
        // ��ʼ��Bean1 ��Ա�ڲ��࣬�����Ȳ����ⲿ���ʵ�������󣬲��ܲ����ڲ����ʵ��������
        Test.Bean1 bean1 = new Test().new Bean1();
        bean1.I++;
        // ��ʼ��Bean2 ��̬�ڲ��಻�ò����ⲿ���ʵ�������󼴿ɲ����ڲ����ʵ��������
        Test.Bean2 bean2 = new Test.Bean2();
        bean2.J++;
        //��ʼ��Bean3 ��Ա�ڲ��࣬�����Ȳ����ⲿ���ʵ�������󣬲��ܲ����ڲ����ʵ��������
        Bean.Bean3 bean3 = new Bean().new Bean3();
        bean3.k++;
    }
}
