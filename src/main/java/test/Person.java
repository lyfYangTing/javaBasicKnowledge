package test;

/**
 * Created by 18435 on 2018/5/8.
 */
public class Person {
    private String name;
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void show(){
        System.out.println("显示出来了");
    }

    public String getMessage(){
        return "我今年12岁";
    }
}
