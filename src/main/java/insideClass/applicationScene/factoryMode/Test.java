package insideClass.applicationScene.factoryMode;

/**
 * Created by 18435 on 2017/12/18.
 * 这个方法是巧妙地使用了内部类，将具体类的实现和它的具体工厂类绑定起来，由具体类的实现者在这个内部类的具体工厂里去产生一个具体类的对象，这当然容易得多。
 * 虽然需要每一个具体类都创建一个具体工厂类，但由于具体工厂类是一个内部类，
 * 这样也不会随着具体类的增加而不断增加新的工厂类，使得代码看起来很臃肿，这也是本方法不得不使用内部类的一个原因吧。
 */
public class Test {
    public static void main(String[] args) {
        String[] ids = new String[]{"Circle", "Square", "Square", "Circle"};
        for (int i = 0; i < ids.length; i++) {
            Shape shape = ShapeFactory.createShape(ids[i]);
            shape.draw();
            shape.erase();
        }
    }
}
