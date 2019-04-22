package cn.haday.device.hksdk;

import cn.haday.device.hksdk.sdk.HCNetSDK;
import com.sun.jna.Pointer;

/**
 * 设备通讯回调函数类
 *
 * */
public class DevInfoCallBack implements HCNetSDK.FRemoteConfigCallback {
    /**
     * 流ID信息
     */
    private HCNetSDK.NET_DVR_STREAM_INFO strStreamInfo;

    /**
     * 回调得到的信息
     */
    private String info;

    /**
     * 运行状态：0-审讯开始，
     * 1-审讯过程中刻录，2-审讯停止，
     * 3-刻录审讯文件,
     * 4-备份(事后备份和本地备份)
     * 5-空闲
     * 6-初始化硬盘
     * 7-恢复审讯
     */
    private int dwType;

    @Override
    public void invoke(int dwType, Pointer lpBuffer, int dwBufLen, Pointer pUserData) {
        switch (dwType) {
            case 2:
                strStreamInfo = new HCNetSDK.NET_DVR_STREAM_INFO();
                strStreamInfo.write();
                Pointer pInfoV30 = strStreamInfo.getPointer();
                pInfoV30.write(0, lpBuffer.getByteArray(0, strStreamInfo.size()), 0, strStreamInfo.size());
                strStreamInfo.read();
                String str = new String(strStreamInfo.byID);
                this.info = str;
                this.dwType = dwType;
                break;
            default:
                this.info = "";
                this.dwType = dwType;
                break;
        }
    }

    public String getInfo() {
        return info;
    }

    public int getDwType() {
        return dwType;
    }
}
