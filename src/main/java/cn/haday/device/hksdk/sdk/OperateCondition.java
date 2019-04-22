package cn.haday.device.hksdk.sdk;

/**
 * 操作条件
 */
public enum OperateCondition {
    /**车牌号码*/
    LICENSE(0x1),
    /**车牌颜色*/
    PLATECOLOR(0x2),
    /**卡号*/
    CARDNO(0x4),
    /**车牌类型*/
    PLATETYPE(0x8),
    /**车辆名单类型*/
    LISTTYPE(0x10),
    /**数据流水号*/
    INDEX(0x20),
    /**操作数*/
    OPERATE_INDEX(0x40);

    private int value;

    OperateCondition(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
