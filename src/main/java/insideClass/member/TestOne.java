package insideClass.member;

/**
 * Created by 18435 on 2017/12/14.
 */
public class TestOne {
    public static void main(String[] args) {
        //成员内部类是依附外部类而存在的，也就是说，如果要创建成员内部类的对象，前提是必须存在一个外部类的对象。
        //创建成员内部类对象 方式1
        Circle circle = new Circle();
        Circle.Draw draw = circle.new Draw();
        //创建成员内部类对象 方式2 前提：外部类必须提供一个方法，用于获取内部类对象
        Circle.Draw draw1 = circle.getDrawInstance();
    }
}
