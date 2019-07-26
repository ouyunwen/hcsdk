package cn.haday.device.hksdk;

import java.util.Date;

public class AlarmResult {
    private AlarmType alarmType;
    private String msg;
    private String licensePlateNumber;
    private Date date;
    @Deprecated
    private int deviceId;
    private String deviceName;

    public AlarmResult(AlarmType alarmType, String msg) {
        this.alarmType = alarmType;
        this.msg = msg;
        this.date = new Date();
    }

    public AlarmResult(AlarmType alarmType, String msg, String licensePlateNumber) {
        this.alarmType = alarmType;
        this.msg = msg;
        this.licensePlateNumber = licensePlateNumber;
        this.date = new Date();
    }

    public AlarmResult(AlarmType alarmType, String msg, String licensePlateNumber, int deviceId) {
        this.alarmType = alarmType;
        this.msg = msg;
        this.licensePlateNumber = licensePlateNumber;
        this.deviceId = deviceId;
        this.date = new Date();
    }

    public AlarmResult(AlarmType alarmType, String msg, String licensePlateNumber, String deviceName) {
        this.alarmType = alarmType;
        this.msg = msg;
        this.licensePlateNumber = licensePlateNumber;
        this.deviceName = deviceName;
        this.date = new Date();
    }

    public AlarmType getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(AlarmType alarmType) {
        this.alarmType = alarmType;
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

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
