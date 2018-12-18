package test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 18435 on 2018/6/13.
 */
public class DateTest {

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat format  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = "2018-03-09 12:34:56";
        String endTime = "2018-03-12 18:00:00";
        ParkingOrder parkingOrder = new ParkingOrder("1",format.parse(startTime),format.parse(endTime),"共享停车场");
        String content = "尊敬的客户，您好，欢迎使用共享停车业务，您的车辆已成功预订：%s%s泊位" + "%s-%s的共享泊位,"+
                "请您在预约时间开始前进场！";
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy年MM月dd日HH点mm分");
        content = String.format(content,parkingOrder.getParkingName(),parkingOrder.getBerthSn(),format1.format(parkingOrder.getInTime()),format1.format(parkingOrder.getOutTime()));
        System.out.println(content);


        List<String> cities = Arrays.asList("Milan",
                "London",
                "New York",
                "San Francisco");
        String citiesCommaSeparated = String.join(",", cities);
        System.out.println(citiesCommaSeparated);
    }

}
