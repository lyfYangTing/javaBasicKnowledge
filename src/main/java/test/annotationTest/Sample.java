package test.annotationTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by 18435 on 2018/8/9.
 */
public class Sample {

    @TestOne(value = RuntimeException.class)
    public static  void m1(){

    }

    public static  void m2(){

    }

    @TestOne(value = RuntimeException.class)
    public static void m3(){
        throw new RuntimeException("Boom");
    }

    public static void m4(){

    }

    @TestOne(value = RuntimeException.class)
    public void m5(){

    }

    public static void m6(){}

    @TestOne(value = RuntimeException.class)
    public static void m7(){
        throw new RuntimeException("Crash");
    }

    public static void m8(){

    }

    public static void main(String[] args) throws ClassNotFoundException{
        int tests = 0;
        int passed = 0;

        Class testClass = Class.forName("test.annotationTest.Sample");
        for(Method method : testClass.getDeclaredMethods()){
            if(method.isAnnotationPresent(TestOne.class)){
                tests++;
                try {
                    method.invoke(null);
                    passed++;
                }catch (InvocationTargetException wrappedExc){
                    Throwable exc = wrappedExc.getCause();
                    System.out.println(method + "failed: " + exc);
                }catch (Exception e){
                    System.out.println("INVALID @Test:" + method);
                }
            }
        }
        String s = "Passed:%s,Failed:%s";
        s = String.format(s,passed,tests -passed);
        System.out.println(s);
    }
}
