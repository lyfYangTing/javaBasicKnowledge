package insideClass.local;

/**
 * Created by 18435 on 2017/12/14.
 */
public class Man {

    private String name = "外部类";
    public Man(){

    }

    public People getWomen(){
        final int age = 1;
        //注意，局部内部类就像是方法里面的一个局部变量一样，是不能有public、protected、private以及static修饰符的。
        class Woman extends People{
            private String name = "内部类";
            public void work(){//重写work方法
                // 内部类访问外部方法的变量，需要有final修饰
                System.out.println(age);
                // 局部内部类可直接访问外部类的变量，即使是私有的
                System.out.println(name);
                // 内部类和外部类有同名变量或方法时，需要通过Outer.this方式来访问外部类成员变量或方法。
                System.out.println("访问内部类的同名变量-->"+name);
                System.out.println("访问外部类的同名变量-->"+Man.this.name);
            }
        }
        return new Woman();
    }

    public static void main(String[] args) {
        Man man = new Man();
        People people = man.getWomen();
        people.work();
    }
}
