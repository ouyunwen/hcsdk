package cn.haday.device.hksdk.sdk;

/**
 * 车牌类型
 */
public enum LicensePlateType {
    /**
     * 标准民用车与军车
     */
    VCA_STANDARD92_PLATE((byte) 0),
    /**
     * 02式民用车牌
     */
    VCA_STANDARD02_PLATE((byte) 1),
    /**
     * 武警车
     */
    VCA_WJPOLICE_PLATE((byte) 2),
    /**
     * 警车
     */
    VCA_JINGCHE_PLATE((byte) 3),
    /**
     * 民用车双行尾牌
     */
    STANDARD92_BACK_PLATE((byte) 4),
    /**
     * 使馆车牌
     */
    VCA_SHIGUAN_PLATE((byte) 5),
    /**
     * 农用车
     */
    VCA_NONGYONG_PLATE((byte) 6),
    /**
     * 摩托车
     */
    VCA_MOTO_PLATE((byte) 7),
    /**
     * 新能源车牌
     */
    NEW_ENERGY_PLATE((byte) 8),
    /**
     * 运输车
     */
    TRANSPORT_PLATE((byte) 32),
    /**
     * 商用车
     */
    COMMERCIAL_PLATE((byte) 33),
    /**
     * 私家车
     */
    PRIVATE_PLATE((byte) 34),
    /**
     * 教练车
     */
    LEARNING_PLATE((byte) 35),
    /**
     * 使馆车
     */
    CD_PLATE((byte) 36),
    /**
     * 使馆车
     */
    CC_PLATE((byte) 37),
    /**
     * 军车
     */
    ARMY_PLATE((byte) 38),
    /**
     * PROTOCOL
     */
    PROTOCOL_PLATE((byte) 39),
    /**
     * 政府车
     */
    GOVERNMENT_PLATE((byte) 40),
    /**
     * EXPORT
     */
    EXPORT_PLATE((byte) 41),
    /**
     * 出租车
     */
    TAXI_PLATE((byte) 42),
    /**
     * TESTING
     */
    TESTING_PLATE((byte) 43),
    /**
     * TRANSFER
     */
    TRANSFER_PLATE((byte) 44),
    /**
     * 货车
     */
    TRUCK_PLATE((byte) 45),
    /**
     * 公交车
     */
    BUS_PLATE((byte) 46),
    /**
     * PUBLIC
     */
    PUBLIC_PLATE((byte) 47),
    /**
     * PUBLIC TRANSFER
     */
    PUB_TRANS_PLATE((byte) 48),
    /**
     * PRIVATE TRANSPORT
     */
    PRI_TRANS_PLATE((byte) 49),
    /**
     * 未知（未识别）
     */
    UNKNOWN_PLATE((byte) -1);

    private byte value;

    LicensePlateType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
