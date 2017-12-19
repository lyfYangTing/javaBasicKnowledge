package insideClass.local;

/**
 * Created by 18435 on 2017/12/14.
 * 局部内部类是定义在一个方法或者一个作用域里面的类，
 * 它和成员内部类的区别在于局部内部类的访问仅限于方法内或者该作用域内。
 */
public class People {

    private String name;

    public People(){}

    public void work(){
        System.out.println("吃饭，睡觉，打豆豆");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
