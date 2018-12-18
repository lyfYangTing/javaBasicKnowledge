package insideClass;

/**
 * Created by 18435 on 2018/7/20.
 */
public class Test {
    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        System.out.println(Calculator.Operation.DIVIDE.apply(1,2));
    }
}
