package cn.haday.device.hksdk;

public enum  AlramType {
    OTHER("其他报警"),
    CAR_PASS("车辆通过");

    private String comment;

    AlramType(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }
}
