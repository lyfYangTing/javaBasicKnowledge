package insideClass.anonymity;

import java.util.Collections;

/**
 * Created by 18435 on 2017/12/18.
 * 内部类测试
 */
public class Test {
    private int z = 10;

    public void eat(Animal animal) {
        animal.run();
    }

    public int getAge(final int x) {
        final int y = 10;
        return (new Animal() {

            public int run() {
                return z++;//内部类可以修改外部类中的属性值
            }

            public int getAge(int age) {
                return x + y + z + age;//无法修改外部环境中的自由变量x和y，所以需要将x和y设置为final类型的
            }
        }).run();
    }

    //返回匿名内部类的实例对象
    public Animal getDog() {

        return new Animal() {

            public int run() {
                return z++;//内部类可以修改外部类中的属性值
            }

            public int getAge(int age) {
                return age;
            }
        };
    }

    public static void main(String[] args) {
        Test test = new Test();
        test.eat(new Animal() {
            public int getAge(int age) {
                return age;
            }

            public int run() {
                System.out.println("我跑得快");
                return 1;
            }
        });

//        int age = test.getAge(10);
//        System.out.println(age);
        System.out.println(test.z);
        test.getAge(1);
        System.out.println(test.z);

        Collections.emptyList();

    }
}

