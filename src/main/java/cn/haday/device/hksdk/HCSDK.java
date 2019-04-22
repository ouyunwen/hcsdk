package cn.haday.device.hksdk;

import cn.haday.device.hksdk.exception.DuplicateDeviceNameException;
import cn.haday.device.hksdk.sdk.HCNetSDK;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用于管理和控制海康威视设备的SDK，此SDK基于海康威视提供的SDK实现<br/>
 * 此类为SDK的基础类，具体控制某种设备则使用对应的子类。<br/>
 * thread-safe class
 * @author Owen
 * @createTime 2019年4月19日 15点12分
 *
 */
public class HCSDK {
    //已连接设备的数量
    protected static AtomicInteger connectedCount = new AtomicInteger();
    //已连接设备的名称和id的对应关系
    protected static ConcurrentHashMap<String, Integer> deviceIdMap = new ConcurrentHashMap<>();
    //已连接设备的名称和info的对应关系
    protected static ConcurrentHashMap<String, HCNetSDK.NET_DVR_DEVICEINFO_V30> deviceInfoMap = new ConcurrentHashMap<>();
    //锁
    protected static Object lock = new Object();
    //sdk是否已经初始化
    protected static volatile boolean initStatus = false;
    protected static HCNetSDK hcNetSDK = HCNetSDK.INSTANCE;

    static {
        //设置连接时间和重连时间
        hcNetSDK.NET_DVR_SetConnectTime(2000, 3);
        hcNetSDK.NET_DVR_SetReconnect(5000, true);
    }



    /**
     * 初始化SDK，要使用本SDK的其他功能前，要先初始化。<br/>
     * 如果监测到已经初始化过，就不再重复初始化。
     *
     * @return 是否初始化成功。
     */
    public static boolean initSDK() {
        synchronized (lock) {
            if (initStatus == false) {
                //SDK初始化
                if (hcNetSDK.NET_DVR_Init()) {
                    initStatus = true;
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }


    /**
     * 连接设备。控制设备前，要先连接设备。
     *
     * @param ip          设备的IP地址
     * @param port        设备的端口
     * @param userName    设备连接时的用户
     * @param password    设备连接时的密码
     * @param deviceName 给设备一个代号，便于以后根据代号来进行控制
     * @return 是否连接成功
     */
    public static boolean connect(String ip, int port, String userName, String password, String deviceName) {
        synchronized (lock) {
            if(deviceIdMap.containsKey(deviceName)){
                throw new DuplicateDeviceNameException(deviceName+" has existed.");
            }
            HCNetSDK.NET_DVR_DEVICEINFO_V30 net_dvr_deviceinfo_v30 = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
            int id = hcNetSDK.NET_DVR_Login_V30(ip, (short)port, userName, password, net_dvr_deviceinfo_v30);
            if (id < 0) {
                return false;
            }else {
                deviceIdMap.put(deviceName, id);
                deviceInfoMap.put(deviceName,net_dvr_deviceinfo_v30);
                return true;
            }
        }
    }
    /**
     * 获取当前设备连接数
     * @return 当前设备连接数
     * */
    public static int getConnectedCount(){
        return connectedCount.get();
    }

    /**
     * 根据设备名称获取其ID
     * @return 设备在SDK中的ID
     * */
    protected static Integer getIdByName(String name){
        return deviceIdMap.get(name);
    }
}
