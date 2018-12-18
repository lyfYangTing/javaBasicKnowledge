package enums;

/**
 * Created by 18435 on 2018/8/2.
 */
public enum Ensemble {
//    SOLO,
//    DUET,
//    TRIO;
//
//    public int numberOfMusicians(){
//        return ordinal() + 1;
//    }
    SOLO(1),
    DUET(2),
    TRIO(3);

    private final int numberOfMusicians;

    Ensemble(int numberOfMusicians){
        this.numberOfMusicians = numberOfMusicians;
    }

    public int getNumberOfMusicians(){
        return this.numberOfMusicians;
    }
}
