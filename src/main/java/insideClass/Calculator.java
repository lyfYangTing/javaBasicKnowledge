package insideClass;

/**
 * Created by 18435 on 2018/7/20.
 */
public class Calculator {
    public enum Operation {
        PLUS, MINUS, TIMES, DIVIDE;

        double apply(double x, double y) {
            switch (this) {
                case PLUS:
                    return x + y;
                case MINUS:
                    return x - y;
                case TIMES:
                    return x * y;
                case DIVIDE:
                    return x / y;
            }
            throw new AssertionError("Unknow op:" + this);
        }
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        System.out.println(Operation.DIVIDE.apply(1,2));
    }
}
