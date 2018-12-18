package test;

import insideClass.member.Circle;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by 18435 on 2017/12/14.
 */
public class CircleTest extends Circle{

    public static void main(String[] args) {
//        Person person = new PersonFactory(PeoPleType.other).getInstance().get();
        Optional<Person> optional = new PersonFactory(PeoPleType.woman).getInstance();
        optional.map(value -> value.getAge()).filter(age -> age < 10);

        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
//        Iterator<String> iterator = list.iterator();
//        while(iterator.hasNext()){
//            String str = iterator.next();
//            System.out.println(str);
//        }
        try{
            int i = 0;
            while(true){
                System.out.println(list.get(i));
                i++;
            }
        }catch (IndexOutOfBoundsException e){
            System.out.println("异常跳出循环");
        }

    }
}
