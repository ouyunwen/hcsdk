package cn.haday.device.hksdk;

import java.util.Date;

public class AlramResult {
    private AlramType alramType;
    private String msg;
    private String licensePlateNumber;
    private Date date;
    private int deviceId;

    public AlramResult(AlramType alramType, String msg) {
        this.alramType = alramType;
        this.msg = msg;
        this.date = new Date();
    }

    public AlramResult(AlramType alramType, String msg, String licensePlateNumber) {
        this.alramType = alramType;
        this.msg = msg;
        this.licensePlateNumber = licensePlateNumber;
        this.date = new Date();
    }

    public AlramResult(AlramType alramType, String msg, String licensePlateNumber, int deviceId) {
        this.alramType = alramType;
        this.msg = msg;
        this.licensePlateNumber = licensePlateNumber;
        this.deviceId = deviceId;
        this.date = new Date();
    }

    public AlramType getAlramType() {
        return alramType;
    }

    public void setAlramType(AlramType alramType) {
        this.alramType = alramType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }
}
