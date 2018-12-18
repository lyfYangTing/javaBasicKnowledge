package enums;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by 18435 on 2018/8/6.
 */
public class Herb {
    public enum Type{
        ANNUAL, PERENNTAL, BIENNIAL
    }

    public final String name;
    public final Type type;

    Herb(String name,Type type){
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static void main(String[] args) {
        // 将集合放到一个按照类型的序数进行索引的数组中来实现  替换
        Herb[] herbs = {new Herb("一年生1", Type.ANNUAL), new Herb("一年生2", Type.ANNUAL),
                new Herb("两年生1", Type.BIENNIAL), new Herb("两年生2", Type.BIENNIAL),
                new Herb("多年生1", Type.PERENNTAL), new Herb("多年生2", Type.PERENNTAL)};

        //方案一   序数索引
        Set<Herb>[] herbsByType = (Set<Herb>[])new Set[Type.values().length];
        for(Type type : Type.values()){
            herbsByType[type.ordinal()] = new HashSet<Herb>();
        }
        for(Herb herb : herbs){
            herbsByType[herb.type.ordinal()].add(herb);
        }
        for (int i = 0; i < herbsByType.length; i++) {
            System.out.printf("%s: %s%n", Herb.Type.values(), herbsByType[i]);
        }

        //方案二   EnumMap实现
        Map<Herb.Type,Set<Herb>> herbsByTypeMap = new EnumMap(Herb.Type.class);

        for(Type type : Type.values()){
            herbsByTypeMap.put(type,new HashSet<>());
        }
        for(Herb herb : herbs){
            herbsByTypeMap.get(herb.type).add(herb);
        }
        System.out.println(herbsByTypeMap);
    }
}
