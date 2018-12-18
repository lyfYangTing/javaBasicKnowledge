package czbk.io;

import java.io.*;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * Created by 18435 on 2018/12/4.
 */
public class StudentsUtil {

    public static TreeSet<Student> getStudents(Comparator<Student> comparator){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            String line = "";
            TreeSet<Student> students = null;
            if(comparator==null){
                students = new TreeSet<>();
            }else {
                students = new TreeSet<>(comparator);
            }
            while ((line=br.readLine())!=null) {
                if ("over".equals(line)) {
                    break;
                } else {
                    String[] datas = line.split(",");
                    Student student = new Student();
                    student.setName(datas[0]);
                    student.setChinese(Integer.parseInt(datas[1]));
                    student.setMath(Integer.parseInt(datas[2]));
                    student.setEnglish(Integer.parseInt(datas[3]));
                    student.setSum();
                    students.add(student);
                }
            }
            return students;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static TreeSet<Student> getStudents(){
        return getStudents(null);
    }

    public static void saveStudentdInfo(TreeSet<Student> students){
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter("stud.txt"));
            for (Student student : students){
                bw.write(student.getName()+ "," + student.getChinese()+","+student.getMath()+","+student.getEnglish()+","+ student.getSum());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bw!=null){
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
