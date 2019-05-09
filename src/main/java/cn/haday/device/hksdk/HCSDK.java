package cn.haday.device.hksdk;

import cn.haday.device.hksdk.exception.InstructionExecuteException;
import cn.haday.device.hksdk.sdk.HCNetSDK;
import com.sun.jna.Pointer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用于管理和控制海康威视设备的SDK，此SDK基于海康威视提供的SDK实现<br/>
 * 此类为SDK的基础类，具体控制某种设备则使用对应的子类。<br/>
 * thread-safe class
 *
 * @author Owen
 * createTime 2019年4月19日 15点12分
 */
public class HCSDK {
    //已连接设备的数量
    private static AtomicInteger connectedCount = new AtomicInteger();
    //已连接设备的名称和id的对应关系
    private static ConcurrentHashMap<String, Integer> deviceIdMap = new ConcurrentHashMap<>();
    //已连接设备的名称和info的对应关系
    private static ConcurrentHashMap<String, HCNetSDK.NET_DVR_DEVICEINFO_V30> deviceInfoMap = new ConcurrentHashMap<>();
    //锁
    protected static final Object lock = new Object();
    //sdk是否已经初始化
    private static volatile boolean initStatus = false;

    //海康官方SDK（java版的不包含所有的功能，如遇到没有的功能，需要参考文档添加到里面）
    protected static HCNetSDK hcNetSDK = HCNetSDK.INSTANCE;

    //监听回调函数
    private static AlramCallBack alramCallBack;

    //监听端口
    private static final int port = 8000;
    //监听开启后得到的句柄
    private static int lListenHandle;

    static {
        //设置连接时间和重连时间
        hcNetSDK.NET_DVR_SetConnectTime(2000, 3);
        hcNetSDK.NET_DVR_SetReconnect(5000, true);
    }


    /**
     * 初始化SDK，要使用本SDK的其他功能前，要先初始化。<br/>
     * 如果监测到已经初始化过，就不再重复初始化。
     */
    public static void initSDK() throws InstructionExecuteException {
        synchronized (lock) {
            if (!initStatus) {
                //SDK初始化
                if (hcNetSDK.NET_DVR_Init()) {
                    initStatus = true;
                } else {
                    throw new InstructionExecuteException("初始化SDK出现异常，异常代号：" + hcNetSDK.NET_DVR_GetLastError(), 1);
                }
            }
        }
    }

    /**
     * 结束应用前调用此方法进行资源释放
     *
     * @return 是否释放成功
     */
    public static void cleanUp() throws InstructionExecuteException {
        synchronized (lock) {
            if (initStatus == true) {
                if (hcNetSDK.NET_DVR_Cleanup()) {
                    initStatus = false;
                } else {
                    throw new InstructionExecuteException("资源释放出现异常，异常代号：" + hcNetSDK.NET_DVR_GetLastError(), 1);
                }
            }
        }
    }


    /**
     * 登录设备。控制设备前，要先登录设备。
     *
     * @param ip         设备的IP地址
     * @param port       设备的端口
     * @param userName   设备连接时的用户
     * @param password   设备连接时的密码
     * @param deviceName 给设备一个别名，便于以后根据代号来进行控制
     * @return 是否连接成功
     */
    public static void login(String ip, int port, String userName, String password, String deviceName) throws InstructionExecuteException {
        synchronized (lock) {
            if (deviceIdMap.containsKey(deviceName)) {
                throw new InstructionExecuteException(deviceName + " 已经登录过.", 3);
            }
            HCNetSDK.NET_DVR_DEVICEINFO_V30 net_dvr_deviceinfo_v30 = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
            System.out.println(ip + "," + (short) port + "," + userName + "," + password);
            int id = hcNetSDK.NET_DVR_Login_V30(ip, (short) port, userName, password, net_dvr_deviceinfo_v30);
            System.out.println("--------id:" + id);
            if (id < 0) {
                throw new InstructionExecuteException("登录设备出现异常，异常代号：" + hcNetSDK.NET_DVR_GetLastError(), 1);
            } else {
                deviceIdMap.put(deviceName, id);
                deviceInfoMap.put(deviceName, net_dvr_deviceinfo_v30);
            }
        }
    }

    /**
     * 从设备注销
     *
     * @param deviceName 设备别名
     */
    public static void logout(String deviceName) throws InstructionExecuteException {
        Integer id = getIdByName(deviceName);
        if (null == id) {
            throw new InstructionExecuteException("找不到名称为：" + deviceName + "的设备", 2);
        }
        synchronized (lock) {
            if (!hcNetSDK.NET_DVR_Logout_V30(id)) {
                throw new InstructionExecuteException("从设备注销时出现异常，异常代号：" + hcNetSDK.NET_DVR_GetLastError(), 1);
            }
            deviceIdMap.remove(deviceName);
            deviceInfoMap.remove(deviceName);
        }
    }

    /**
     * 获取当前设备连接数
     *
     * @return 当前设备连接数
     */
    public static int getConnectedCount() {
        return connectedCount.get();
    }

    /**
     * 根据设备别名获取其ID
     *
     * @return 设备在SDK中的ID，如果该别名还没注册，则返回null
     */
    protected static Integer getIdByName(String name) {
        return deviceIdMap.get(name);
    }

    /**
     * 根据设备名称来发起一个连接，用来发送指令
     *
     * @param deviceName 设备别名
     * @return 连接句柄id
     */
    protected static int openConnection(String deviceName) throws InstructionExecuteException {
        //根据名称获取存留在sdk的设备id
        Integer id = getIdByName(deviceName);
        if (null == id) {
            throw new InstructionExecuteException("找不到名称为：" + deviceName + "的设备", 2);
        }
        DevInfoCallBack fVehicleCrtlCB = new DevInfoCallBack();
        //发起远程连接
        synchronized (lock) {
            int i = hcNetSDK.NET_DVR_StartRemoteConfig(id, HCNetSDK.NET_DVR_VEHICLELIST_CTRL_START, null, 0, fVehicleCrtlCB, Pointer.NULL);
            if (i < 0) {
                throw new InstructionExecuteException("打开连接出现异常，异常代号：" + hcNetSDK.NET_DVR_GetLastError(), 1);
            }
            return i;
        }
    }

    /**
     * 关闭连接，释放连接资源
     *
     * @param connectionId 连接句柄id
     */
    protected static void closeConnection(int connectionId) throws InstructionExecuteException {
        synchronized (lock) {
            if (!hcNetSDK.NET_DVR_StopRemoteConfig(connectionId)) {
                throw new InstructionExecuteException("关闭连接出现异常，异常代号：" + hcNetSDK.NET_DVR_GetLastError(), 1);
            }
        }
    }

    /**
     * 开启监听
     */
    public static void startAlarmListen(AlarmCallbackHandler alarmCallbackHandler) throws InstructionExecuteException {
        Pointer pUser = null;

        if (alramCallBack == null) {
            alramCallBack = new AlramCallBack(alarmCallbackHandler);
        }
        try {
            lListenHandle = hcNetSDK.NET_DVR_StartListen_V30(InetAddress.getLocalHost().getHostAddress(), (short) port, alramCallBack, pUser);
        } catch (UnknownHostException e) {
            throw new InstructionExecuteException("开启监听出现异常，获取本地IP出现异常：" + e.getMessage(), 1);
        }
        if (lListenHandle < 0) {
            throw new InstructionExecuteException("开启监听出现异常，异常代号：" + hcNetSDK.NET_DVR_GetLastError(), 1);
        }
    }

    /**
     * 停止监听
     */
    public static void stopAlarmListen() throws InstructionExecuteException {
        if (lListenHandle < 0) {
            return;
        }
        if (!hcNetSDK.NET_DVR_StopListen_V30(lListenHandle)) {
            throw new InstructionExecuteException("停止监听出现异常，异常代号：" + hcNetSDK.NET_DVR_GetLastError(), 1);
        }
    }
}
