package insideClass.anonymity;

import java.util.Collections;

/**
 * Created by 18435 on 2017/12/18.
 * �ڲ������
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
                return z++;//�ڲ�������޸��ⲿ���е�����ֵ
            }

            public int getAge(int age) {
                return x + y + z + age;//�޷��޸��ⲿ�����е����ɱ���x��y��������Ҫ��x��y����Ϊfinal���͵�
            }
        }).run();
    }

    //���������ڲ����ʵ������
    public Animal getDog() {

        return new Animal() {

            public int run() {
                return z++;//�ڲ�������޸��ⲿ���е�����ֵ
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
                System.out.println("���ܵÿ�");
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

