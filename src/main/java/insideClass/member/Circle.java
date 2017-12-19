package insideClass.member;

/**
 * Created by 18435 on 2017/12/14.
 * 成员内部类:成员内部类是最普通的内部类，它的定义为位于另一个类的内部，
 * 成员内部类可以无条件访问外部类的所有成员属性和成员方法（包括private成员和静态成员）。
 */
public class Circle {//外部类
    private Draw draw = null;
    private double radius = 0;
    private String name = "外部类";
    static int count = 1;

    public Circle(){}

    public Circle(double radius){
        this.radius = radius;
    }

    /**
     * 外部类访问成员内部类成员:
     * 必须先创建一个成员内部类的对象，再通过指向这个对象的引用来访问
     */
    public void accessDraw(){
        getDrawInstance().drawSahpe();
    }

    /**
     * 创建内部类对象
     * @return
     */
    public Draw getDrawInstance(){
        if(draw==null){
            return new Draw();
        }
        return draw;
    }


    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    class Draw{//内部类
        private String name = "内部类";

        /**
         * 成员内部类访问成员外部类时:
         * 1.成员内部类可以无条件访问外部类的所有成员属性和成员方法(包括private成员和静态成员)
         * 2.当成员内部类拥有和外部类同名的成员变量或者方法时，会发生隐藏现象，即默认情况下访问的是成员内部类的成员。
         *   如果要访问外部类的同名成员，需要以下面的形式进行访问：外部类.this.成员变量   外部类.this.成员方法
         */
        public void drawSahpe() {
            System.out.println("drawshape");
            System.out.println("访问外部类的私有成员-------->"+radius);
            System.out.println("访问外部类的静态成员-------->"+count);
            System.out.println("访问内部类与外部类同名的 内部类成员---->"+name);
            System.out.println("访问内部类与外部类同名的 外部类成员---->"+Circle.this.name);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
