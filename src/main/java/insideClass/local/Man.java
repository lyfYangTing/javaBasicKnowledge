package insideClass.local;

/**
 * Created by 18435 on 2017/12/14.
 */
public class Man {

    private String name = "�ⲿ��";
    public Man(){

    }

    public People getWomen(){
        final int age = 1;
        //ע�⣬�ֲ��ڲ�������Ƿ��������һ���ֲ�����һ�����ǲ�����public��protected��private�Լ�static���η��ġ�
        class Woman extends People{
            private String name = "�ڲ���";
            public void work(){//��дwork����
                // �ڲ�������ⲿ�����ı�������Ҫ��final����
                System.out.println(age);
                // �ֲ��ڲ����ֱ�ӷ����ⲿ��ı�������ʹ��˽�е�
                System.out.println(name);
                // �ڲ�����ⲿ����ͬ�������򷽷�ʱ����Ҫͨ��Outer.this��ʽ�������ⲿ���Ա�����򷽷���
                System.out.println("�����ڲ����ͬ������-->"+name);
                System.out.println("�����ⲿ���ͬ������-->"+Man.this.name);
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
