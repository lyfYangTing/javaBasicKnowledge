package insideClass.applicationScene.factoryMode;

/**
 * Created by 18435 on 2017/12/18.
 */
public class Circle implements Shape {
    public void draw() {
        System.out.println("the circle is drawing...");
    }

    public void erase() {
        System.out.println("the circle is erasing...");
    }

    /**
     * 内部类Factory用得好。第一呢，这个类只做一件事，就是产生一个Circle对象，与其他类无关，就这一个条也就满足了使用内部类的条件。
     * 第二呢，这个Factory类需要是静态的，这也得要求它被使用内部类，不然，下面的ShapeFacotry.addFactory就没办法add了。
     * 而最后的那个静态的语句块是用来将具体的工厂类添加到抽象的工厂里面去。
     * 在抽象工厂里调用Class.forName就会执行这个静态的语句块了。
     */
    private static class Factory extends ShapeFactory {
        protected Shape create() {
            return new Circle();
        }
    }

    static {
        ShapeFactory.addFactory("Circle", new Factory());
    }


}
