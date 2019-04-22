package cn.haday.device.hksdk.sdk;

/**
 * 车牌颜色
 */
public enum LicensePlateColor {
    /**
     * 蓝色车牌
     */
    BLUE((byte)0),
    /**
     * 黄色车牌
     */
    YELLOW((byte)1),
    /**
     * 白色车牌
     */
    WHITE((byte)2),
    /**
     * 黑色车牌
     */
    BLACK((byte)3),
    /**
     * 绿色车牌
     */
    GREEN((byte)4),
    /**
     * 民航黑色车牌
     */
    BKAIR((byte)5),
    /**
     * 红色车牌
     */
    RED((byte)6),
    /**
     * 橙色车牌
     */
    ORANGE((byte)7),
    /**
     * 其他
     */
    OTHER((byte)-1);

    private byte value;

    LicensePlateColor(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
