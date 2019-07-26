package cn.haday.device.hksdk;

public enum AlarmType {
    OTHER("其他报警"),
    ANY_CAR("交通抓拍结果（所有车辆）"),
    NEW_ANY_CAT("新版交通抓拍结果（所有车辆）"),
    WHITE_LIST("白名单"),
    BLACK_LIST("黑名单"),
    TEMP_LIST("临时车辆");


    private String comment;

    AlarmType(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }
}
