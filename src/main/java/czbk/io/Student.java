package czbk.io;

/**
 * Created by 18435 on 2018/12/4.
 */
public class Student implements Comparable<Student>{
    private String name;
    private int chinese;
    private int math;
    private int english;
    private int sum = this.chinese + this.math + this.english;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChinese() {
        return chinese;
    }

    public void setChinese(int chinese) {
        this.chinese = chinese;
    }

    public int getMath() {
        return math;
    }

    public void setMath(int math) {
        this.math = math;
    }

    public int getEnglish() {
        return english;
    }

    public void setEnglish(int english) {
        this.english = english;
    }

    public int getSum() {
        return this.chinese+this.english+this.math;
    }

    public void setSum() {
        this.sum = this.chinese+this.english+this.math;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", chinese=" + chinese +
                ", math=" + math +
                ", english=" + english +
                ", sum=" + sum +
                '}';
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Student){
            Student student = (Student)obj;
            return this.name.equals(student.name) && this.sum == student.sum ;
        }else {
            throw new ClassCastException("类型不匹配错误");
        }
    }

    @Override
    public int compareTo(Student s) {
        int number = new Integer(this.sum).compareTo(new Integer(s.sum));
        if (number==0){
            return this.name.compareTo(s.name);
        }
        return number;
    }
}
