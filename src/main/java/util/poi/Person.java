package util.poi;

import java.util.List;

/**
 * Created by 18435 on 2018/3/23.
 */
public class Person {
    private String name;
    private Integer age;
    private String sex;
    private Long inTime; //保存的是进场时间的时间戳
    private Long outTime;//保存的是出场时间的时间戳
    private List<String> imgURl;
    private Float amount;

    public Person(){}

    public Person(String name, int age, String sex,long outTime) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.outTime = outTime;
    }

    public Person(int age, String sex, long outTime) {
        this.age = age;
        this.sex = sex;
        this.outTime = outTime;
    }

    public Person(String name, long inTime) {
        this.name = name;
        this.outTime = outTime;
    }

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Long getInTime() {
        return inTime;
    }

    public void setInTime(Long inTime) {
        this.inTime = inTime;
    }

    public Long getOutTime() {
        return outTime;
    }

    public void setOutTime(Long outTime) {
        this.outTime = outTime;
    }

    public List<String> getImgURl() {
        return imgURl;
    }

    public void setImgURl(List<String> imgURl) {
        this.imgURl = imgURl;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", inTime=" + inTime +
                ", outTime=" + outTime +
                ", imgURl=" + imgURl +
                ", amount=" + amount +
                '}';
    }
}
