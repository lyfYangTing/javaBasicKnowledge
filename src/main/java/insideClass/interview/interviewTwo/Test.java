package insideClass.interview.interviewTwo;

/**
 * Created by 18435 on 2017/12/19.
 */
public class Test {
    public static void main(String[] args)  {
//        Outter outter = new Outter();
//        outter.new Inner().print();
        double[] arr = {11.5,20,10,100};
        double sum = 0;
        for(double a:arr){
            sum+=a;
        }
        System.out.println(sum);
    }
}
