package cn.haday.device.hksdk;

import cn.haday.device.hksdk.sdk.HCNetSDK;

/**
 * Hello world!
 *
 */
public class App 
{

    public static void main( String[] args )
    {
        HCNetSDK hcNetSDK = HCNetSDK.INSTANCE;

        //初始化设备
        hcNetSDK.NET_DVR_Init();

        //设置连接时间和重连时间
        hcNetSDK.NET_DVR_SetConnectTime(2000,1);
        hcNetSDK.NET_DVR_SetReconnect(10000,true);

        //注册设备
        HCNetSDK.NET_DVR_DEVICEINFO_V30 net_dvr_deviceinfo_v30 = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
        int nativeLong = hcNetSDK.NET_DVR_Login_V30("192.168.1.111", Short.valueOf("8000"), "admin", "admin", net_dvr_deviceinfo_v30);
        if(nativeLong<0){
            System.out.println("注册失败");
            hcNetSDK.NET_DVR_Cleanup();
            return;
        }


        System.out.println( "Hello World!"  );


    }
}
