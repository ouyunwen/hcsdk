package cn.haday.device.hksdk;

import cn.haday.device.hksdk.exception.InstructionExecuteException;
import cn.haday.device.hksdk.sdk.HCNetSDK;
import cn.haday.device.hksdk.sdk.LicensePlateColor;
import cn.haday.device.hksdk.sdk.LicensePlateType;
import cn.haday.device.hksdk.sdk.OperateCondition;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.UUID;

import static cn.haday.device.hksdk.sdk.HCNetSDK.NET_DVR_VEHICLE_DELINFO_CTRL;

/**
 * 用于管理和控制海康威视的道闸的SDK，此SDK基于海康威视提供的SDK实现<br/>
 * thread-safe class
 *
 * @author Owen
 * createTime 2019年4月19日 15点12分
 */
public class BarrierSDK extends HCSDK {

    /**
     * 允许某车牌通过某个道闸，包括进和出。<br/>
     * 例如：<br/>
     * 对于进，发送命令给负责放行进入场内的道闸；对于出，发送命令给负责放行离开停车场的道闸。<br/>
     * 道闸的代号就是在SDK初始化后，注册设备时赋予的一个别名。
     *
     * @param deviceName         道闸的设代号（在{@link HCSDK#login(String, int, String, String, String)}中声明）
     * @param licensePlateNumber 车牌号码
     * @param startTime          准入开始时间
     * @param endTime            准入结束时间
     */
    public static void addPass(String deviceName, String licensePlateNumber, LocalDateTime startTime, LocalDateTime endTime) throws InstructionExecuteException {
        int lHandle = openConnection(deviceName);
        HCNetSDK.NET_DVR_VEHICLE_CONTROL_LIST_INFO struVehicleControl = new HCNetSDK.NET_DVR_VEHICLE_CONTROL_LIST_INFO();
        struVehicleControl.read();
        struVehicleControl.dwSize = struVehicleControl.size();
        struVehicleControl.dwChannel = 1;
        try {
            struVehicleControl.sLicense = licensePlateNumber.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //名单属性（黑白名单）0-白名单，1-黑名单，0xff-全部
        struVehicleControl.byListType = 0;
        //车牌类型
        struVehicleControl.byPlateType = LicensePlateType.TRUCK_PLATE.getValue();
        //VCA_YELLOW_PLATE 黄色车牌
        struVehicleControl.byPlateColor = LicensePlateColor.YELLOW.getValue();

        //卡号，应该是指如果可以刷卡进门的话，这个卡对应的号码。现在没有卡，先用uuid代替。
        String cardNo = UUID.randomUUID().toString();
        struVehicleControl.sCardNo = cardNo.getBytes();

        //有效开始时间
        struVehicleControl.struStartTime.wYear = (short) startTime.getYear();
        struVehicleControl.struStartTime.byMonth = (byte) startTime.getMonthValue();
        struVehicleControl.struStartTime.byDay = (byte) startTime.getDayOfMonth();
        struVehicleControl.struStartTime.byHour = (byte) startTime.getHour();
        struVehicleControl.struStartTime.byMinute = (byte) startTime.getMinute();
        struVehicleControl.struStartTime.bySecond = (byte) startTime.getSecond();

        //有效结束时间
        struVehicleControl.struStopTime.wYear = (short) endTime.getYear();
        struVehicleControl.struStopTime.byMonth = (byte) endTime.getMonthValue();
        struVehicleControl.struStopTime.byDay = (byte) endTime.getDayOfMonth();
        struVehicleControl.struStopTime.byHour = (byte) endTime.getHour();
        struVehicleControl.struStopTime.byMinute = (byte) endTime.getMinute();
        struVehicleControl.struStopTime.bySecond = (byte) endTime.getSecond();
        struVehicleControl.write();
        synchronized (lock) {
            boolean bSend = hcNetSDK.NET_DVR_SendRemoteConfig(lHandle, hcNetSDK.ENUM_SENDDATA, struVehicleControl.getPointer(), struVehicleControl.size());
            if (!bSend) {
                throw new InstructionExecuteException("调用NET_DVR_SendRemoteConfig下发车牌时异常，错误码：" + hcNetSDK.NET_DVR_GetLastError());
            }
        }
        //关闭连接，释放资源
        closeConnection(lHandle);

    }

    /**
     * 删除某个道闸的车牌数据，不再允许其通过
     *
     * @param deviceName         设备别名
     * @param licensePlateNumber 车牌号码
     */
    public static void deletePass(String deviceName, String licensePlateNumber) throws InstructionExecuteException {
        Integer id = getIdByName(deviceName);
        if (null == id) {
            throw new InstructionExecuteException("找不到名称为：" + deviceName + "的设备");
        }
        HCNetSDK.NET_DVR_VEHICLE_CONTROL_DELINFO net_dvr_vehicle_control_delinfo = new HCNetSDK.NET_DVR_VEHICLE_CONTROL_DELINFO();
        net_dvr_vehicle_control_delinfo.read();
        net_dvr_vehicle_control_delinfo.dwSize = net_dvr_vehicle_control_delinfo.size();
        net_dvr_vehicle_control_delinfo.dwDelType = OperateCondition.LICENSE.getValue();
        try {
            net_dvr_vehicle_control_delinfo.sLicense = licensePlateNumber.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //条件删除，这里就是根据车牌号删除
        net_dvr_vehicle_control_delinfo.byOperateType = 0;
        net_dvr_vehicle_control_delinfo.byListType = 0;
        synchronized (lock) {
            boolean b = hcNetSDK.NET_DVR_RemoteControl(id, NET_DVR_VEHICLE_DELINFO_CTRL, net_dvr_vehicle_control_delinfo.getPointer(), net_dvr_vehicle_control_delinfo.size());
            if (!b) {
                throw new InstructionExecuteException("调用NET_DVR_RemoteControl删除车牌时异常，错误码：" + hcNetSDK.NET_DVR_GetLastError());
            }
        }

    }


}
