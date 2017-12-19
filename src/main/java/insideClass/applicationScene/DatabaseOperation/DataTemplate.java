package insideClass.applicationScene.DatabaseOperation;

/**
 * Created by 18435 on 2017/12/18.
 * 使用一个模板类来实现所有的try…catch…finally…语句的功能，
 */
public class DataTemplate {
    public void execute(DataManager dataManager){
        try{
            dataManager.manageData();
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
    }
}
