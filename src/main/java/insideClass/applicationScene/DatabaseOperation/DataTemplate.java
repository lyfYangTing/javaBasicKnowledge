package insideClass.applicationScene.DatabaseOperation;

/**
 * Created by 18435 on 2017/12/18.
 * ʹ��һ��ģ������ʵ�����е�try��catch��finally�����Ĺ��ܣ�
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
