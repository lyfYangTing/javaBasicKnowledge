package insideClass.interview.interviewTwo;

/**
 * Created by 18435 on 2017/12/19.
 */
public class Test {
    public static void main(String[] args)  {
        Outter outter = new Outter();
        outter.new Inner().print();
    }
}
