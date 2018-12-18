package test;

import java.util.Date;

/**
 * Created by 18435 on 2018/6/13.
 */
public class ParkingOrder {
    private String berthSn;  //泊位号
    private Date inTime; //入场时间
    private Date outTime; //出场时间
    private String parkingName; //停车场


    public ParkingOrder(String berthSn, Date inTime, Date outTime, String parkingName) {
        this.berthSn = berthSn;
        this.inTime = inTime;
        this.outTime = outTime;
        this.parkingName = parkingName;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public Date getOutTime() {
        return outTime;
    }

    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public String getBerthSn() {
        return berthSn;
    }

    public void setBerthSn(String berthSn) {
        this.berthSn = berthSn;
    }
}
