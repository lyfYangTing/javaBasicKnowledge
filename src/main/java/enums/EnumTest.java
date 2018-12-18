package enums;

import java.util.EnumSet;

/**
 * Created by 18435 on 2018/8/2.
 */
public class EnumTest {

    public static void main(String[] args){
        System.out.println(Ensemble.SOLO.getNumberOfMusicians());
        EnumSet.of(Ensemble.DUET,Ensemble.SOLO);
    }
}
