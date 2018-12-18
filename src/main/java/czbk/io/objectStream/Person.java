package czbk.io.objectStream;

import java.io.Serializable;

/**
 * Created by 18435 on 2018/11/26.
 */
public class Person implements Serializable{

    static final long serialVersionUID = 42L;
    private String name;
    private int age;
    //private String country;
    static String version = "1.0";

//    public Person(String name,int age,String country){
//        this.age = age;
//        this.name = name;
//        this.country = country;
//    }
//
//    public Person(String name,int age,String country,String version){
//        this.age = age;
//        this.name = name;
//        this.country = country;
//        Person.version = version;
//    }


    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
               // ", country='" + country + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
