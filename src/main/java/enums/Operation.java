package enums;

/**
 * Created by 18435 on 2018/8/2.
 */
public enum  Operation {
    PLUS("+") {
        @Override
        double apply(double x, double y) {
            return x + y;
        }
    },
    MINUS("-") {
        @Override
        double apply(double x, double y) {
            return x - y;
        }
    },
    TIMES("*") {
        @Override
        double apply(double x, double y) {
            return x * y;
        }
    },
    DIVIDE("/") {
        @Override
        double apply(double x, double y) {
            return x / y;
        }
    };

    private final String operation;
    Operation(String operation){
        this.operation = operation;
    }

    @Override
    public String toString() {
        return this.operation;
    }

    abstract double apply(double x,double y);

    public static void main(String[] args){
        double x = 2.0;
        double y = 4.0;

        for (Operation operation : Operation.values()){
            System.out.println(""+x + operation + y + "=" + operation.apply(x,y));
        }
    }
}
