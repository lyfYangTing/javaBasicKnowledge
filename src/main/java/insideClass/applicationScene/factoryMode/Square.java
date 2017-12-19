package insideClass.applicationScene.factoryMode;

/**
 * Created by 18435 on 2017/12/18.
 */
public class Square implements Shape {
    public void draw() {
        System.out.println("the square is drawing...");
    }

    public void erase() {
        System.out.println("the square is erasing...");
    }

    private static class Factory extends ShapeFactory {
        protected Shape create() {
            return new Square();
        }
    }

    static {
        ShapeFactory.addFactory("Square", new Factory());
    }
}
