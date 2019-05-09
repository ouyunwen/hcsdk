package cn.haday.device.hksdk;

import cn.haday.device.hksdk.sdk.HCNetSDK;
import com.sun.jna.Pointer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class AlramCallBack implements HCNetSDK.FMSGCallBack {
    private AlarmCallbackHandler alarmCallbackHandler;

    public AlramCallBack(AlarmCallbackHandler alarmCallbackHandler) {
        this.alarmCallbackHandler = alarmCallbackHandler;
    }

    @Override
    public void invoke(int lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen, Pointer pUser) {
        String sAlarmType;
        sAlarmType = "lCommand=" + lCommand;
        //lCommand是传的报警类型
        switch (lCommand) {
            case HCNetSDK.COMM_ALARM_V40:
                HCNetSDK.NET_DVR_ALARMINFO_V40 struAlarmInfoV40 = new HCNetSDK.NET_DVR_ALARMINFO_V40();
                struAlarmInfoV40.write();
                Pointer pInfoV40 = struAlarmInfoV40.getPointer();
                pInfoV40.write(0, pAlarmInfo.getByteArray(0, struAlarmInfoV40.size()), 0, struAlarmInfoV40.size());
                struAlarmInfoV40.read();

                switch (struAlarmInfoV40.struAlarmFixedHeader.dwAlarmType) {
                    case 0:
                        struAlarmInfoV40.struAlarmFixedHeader.ustruAlarm.setType(HCNetSDK.struIOAlarm.class);
                        struAlarmInfoV40.read();
                        sAlarmType = sAlarmType + "：信号量报警" + "，" + "报警输入口：" + struAlarmInfoV40.struAlarmFixedHeader.ustruAlarm.struioAlarm.dwAlarmInputNo;
                        alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, sAlarmType));
                        break;
                    case 1:
                        sAlarmType = sAlarmType + "：硬盘满";
                        alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, sAlarmType));
                        break;
                    case 2:
                        sAlarmType = sAlarmType + "：信号丢失";
                        alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, sAlarmType));
                        break;
                    case 3:
                        struAlarmInfoV40.struAlarmFixedHeader.ustruAlarm.setType(HCNetSDK.struAlarmChannel.class);
                        struAlarmInfoV40.read();
                        int iChanNum = struAlarmInfoV40.struAlarmFixedHeader.ustruAlarm.sstrualarmChannel.dwAlarmChanNum;
                        sAlarmType = sAlarmType + "：移动侦测" + "，" + "报警通道个数：" + iChanNum + "，" + "报警通道号：";

                        for (int i = 0; i < iChanNum; i++) {
                            byte[] byChannel = struAlarmInfoV40.pAlarmData.getByteArray(i * 4, 4);

                            int iChanneNo = 0;
                            for (int j = 0; j < 4; j++) {
                                int ioffset = j * 8;
                                int iByte = byChannel[j] & 0xff;
                                iChanneNo = iChanneNo + (iByte << ioffset);
                            }

                            sAlarmType = sAlarmType + "+ch[" + iChanneNo + "]";
                        }
                        alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, sAlarmType));
                        break;
                    case 4:
                        sAlarmType = sAlarmType + "：硬盘未格式化";
                        alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, sAlarmType));
                        break;
                    case 5:
                        sAlarmType = sAlarmType + "：读写硬盘出错";
                        alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, sAlarmType));
                        break;
                    case 6:
                        sAlarmType = sAlarmType + "：遮挡报警";
                        alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, sAlarmType));
                        break;
                    case 7:
                        sAlarmType = sAlarmType + "：制式不匹配";
                        alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, sAlarmType));
                        break;
                    case 8:
                        sAlarmType = sAlarmType + "：非法访问";
                        alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, sAlarmType));
                        break;
                }
                break;
            case HCNetSDK.COMM_ALARM_V30:
                HCNetSDK.NET_DVR_ALARMINFO_V30 strAlarmInfoV30 = new HCNetSDK.NET_DVR_ALARMINFO_V30();
                strAlarmInfoV30.write();
                Pointer pInfoV30 = strAlarmInfoV30.getPointer();
                pInfoV30.write(0, pAlarmInfo.getByteArray(0, strAlarmInfoV30.size()), 0, strAlarmInfoV30.size());
                strAlarmInfoV30.read();
                switch (strAlarmInfoV30.dwAlarmType) {
                    case 0:
                        sAlarmType = sAlarmType + "：信号量报警" + "，" + "报警输入口：" + (strAlarmInfoV30.dwAlarmInputNumber + 1);
                        alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, sAlarmType));
                        break;
                    case 1:
                        sAlarmType = sAlarmType + "：硬盘满";
                        alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, sAlarmType));
                        break;
                    case 2:
                        sAlarmType = sAlarmType + "：信号丢失";
                        alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, sAlarmType));
                        break;
                    case 3:
                        sAlarmType = sAlarmType + "：移动侦测" + "，" + "报警通道：";
                        for (int i = 0; i < 64; i++) {
                            if (strAlarmInfoV30.byChannel[i] == 1) {
                                sAlarmType = sAlarmType + "ch" + (i + 1) + " ";
                            }
                        }
                        alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, sAlarmType));
                        break;
                    case 4:
                        sAlarmType = sAlarmType + "：硬盘未格式化";
                        alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, sAlarmType));
                        break;
                    case 5:
                        sAlarmType = sAlarmType + "：读写硬盘出错";
                        alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, sAlarmType));
                        break;
                    case 6:
                        sAlarmType = sAlarmType + "：遮挡报警";
                        alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, sAlarmType));
                        break;
                    case 7:
                        sAlarmType = sAlarmType + "：制式不匹配";
                        alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, sAlarmType));
                        break;
                    case 8:
                        sAlarmType = sAlarmType + "：非法访问";
                        alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, sAlarmType));
                        break;
                }
                break;
            case HCNetSDK.COMM_ALARM_RULE:
                HCNetSDK.NET_VCA_RULE_ALARM strVcaAlarm = new HCNetSDK.NET_VCA_RULE_ALARM();
                strVcaAlarm.write();
                Pointer pVcaInfo = strVcaAlarm.getPointer();
                pVcaInfo.write(0, pAlarmInfo.getByteArray(0, strVcaAlarm.size()), 0, strVcaAlarm.size());
                strVcaAlarm.read();

                switch (strVcaAlarm.struRuleInfo.wEventTypeEx) {
                    case 1:
                        sAlarmType = sAlarmType + "：穿越警戒面" + "，" +
                                "_wPort:" + strVcaAlarm.struDevInfo.wPort +
                                "_byChannel:" + strVcaAlarm.struDevInfo.byChannel +
                                "_byIvmsChannel:" + strVcaAlarm.struDevInfo.byIvmsChannel +
                                "_Dev IP：" + new String(strVcaAlarm.struDevInfo.struDevIP.sIpV4);
                        alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, sAlarmType));
                        break;
                    case 2:
                        sAlarmType = sAlarmType + "：目标进入区域" + "，" +
                                "_wPort:" + strVcaAlarm.struDevInfo.wPort +
                                "_byChannel:" + strVcaAlarm.struDevInfo.byChannel +
                                "_byIvmsChannel:" + strVcaAlarm.struDevInfo.byIvmsChannel +
                                "_Dev IP：" + new String(strVcaAlarm.struDevInfo.struDevIP.sIpV4);
                        alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, sAlarmType));
                        break;
                    case 3:
                        sAlarmType = sAlarmType + "：目标离开区域" + "，" +
                                "_wPort:" + strVcaAlarm.struDevInfo.wPort +
                                "_byChannel:" + strVcaAlarm.struDevInfo.byChannel +
                                "_byIvmsChannel:" + strVcaAlarm.struDevInfo.byIvmsChannel +
                                "_Dev IP：" + new String(strVcaAlarm.struDevInfo.struDevIP.sIpV4);
                        alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, sAlarmType));
                        break;
                    default:
                        sAlarmType = sAlarmType + "：其他行为分析报警，事件类型："
                                + strVcaAlarm.struRuleInfo.wEventTypeEx +
                                "_wPort:" + strVcaAlarm.struDevInfo.wPort +
                                "_byChannel:" + strVcaAlarm.struDevInfo.byChannel +
                                "_byIvmsChannel:" + strVcaAlarm.struDevInfo.byIvmsChannel +
                                "_Dev IP：" + new String(strVcaAlarm.struDevInfo.struDevIP.sIpV4);
                        alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, sAlarmType));
                        break;
                }


                if (strVcaAlarm.dwPicDataLen > 0) {
                    //保存图片
                }
                break;
            case HCNetSDK.COMM_UPLOAD_PLATE_RESULT:
                HCNetSDK.NET_DVR_PLATE_RESULT strPlateResult = new HCNetSDK.NET_DVR_PLATE_RESULT();
                strPlateResult.write();
                Pointer pPlateInfo = strPlateResult.getPointer();
                pPlateInfo.write(0, pAlarmInfo.getByteArray(0, strPlateResult.size()), 0, strPlateResult.size());
                strPlateResult.read();
                try {
                    String srt3 = new String(strPlateResult.struPlateInfo.sLicense, "GBK");
                    sAlarmType = sAlarmType + "：交通抓拍上传，车牌：" + srt3;
                    alarmCallbackHandler.handle(new AlramResult(AlramType.CAR_PASS, sAlarmType, srt3));
                } catch (UnsupportedEncodingException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (strPlateResult.dwPicLen > 0) {
                    //保存图片
                }
                break;
            case HCNetSDK.COMM_ITS_PLATE_RESULT:
                HCNetSDK.NET_ITS_PLATE_RESULT strItsPlateResult = new HCNetSDK.NET_ITS_PLATE_RESULT();
                strItsPlateResult.write();
                Pointer pItsPlateInfo = strItsPlateResult.getPointer();
                pItsPlateInfo.write(0, pAlarmInfo.getByteArray(0, strItsPlateResult.size()), 0, strItsPlateResult.size());
                strItsPlateResult.read();
                try {
                    String srt3 = new String(strItsPlateResult.struPlateInfo.sLicense, "GBK");
                    sAlarmType = sAlarmType + ",车辆类型：" + strItsPlateResult.byVehicleType + ",交通抓拍上传，车牌：" + srt3;
                    alarmCallbackHandler.handle(new AlramResult(AlramType.CAR_PASS, sAlarmType, srt3));
                } catch (UnsupportedEncodingException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case HCNetSDK.COMM_ALARM_PDC:
                //客流量统计
                alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, "客流量统计"));
                break;

            case HCNetSDK.COMM_ITS_PARK_VEHICLE:
                //停车场数据
                alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, "停车场数据"));
                break;
            case HCNetSDK.COMM_ALARM_TFS:
                //违章拍照
                alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, "违章拍照"));
                break;
            case HCNetSDK.COMM_ALARM_AID_V41:
                //交通事件报警
                alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, "交通事件报警"));
                break;
            case HCNetSDK.COMM_ALARM_TPS_V41:
                //交通统计信息报警
                alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, "交通统计信息报警"));
                break;
            case HCNetSDK.COMM_UPLOAD_FACESNAP_RESULT:
                //人脸识别
                alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, "人脸识别"));
                break;
            case HCNetSDK.COMM_SNAP_MATCH_ALARM:
                //人脸黑名单识别
                alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, "人脸黑名单识别"));
                break;
            case HCNetSDK.COMM_ALARM_ACS: //门禁主机报警信息
                //门禁报警
                alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, "门禁报警"));
                break;
            case HCNetSDK.COMM_ID_INFO_ALARM: //身份证信息
                //身份证刷卡报警
                alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, "身份证刷卡报警"));
                break;
            case HCNetSDK.COMM_ISAPI_ALARM: //ISAPI协议报警信息
                //ISAPI协议报警
                alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, "ISAPI协议报警"));
                break;
            case HCNetSDK.COMM_VEHICLE_CONTROL_ALARM:
                HCNetSDK.NET_ITS_PLATE_RESULT whiteCarResult = new HCNetSDK.NET_ITS_PLATE_RESULT();
                whiteCarResult.write();
                Pointer whiteCarResultPointer = whiteCarResult.getPointer();
                whiteCarResultPointer.write(0, pAlarmInfo.getByteArray(0, whiteCarResult.size()), 0, whiteCarResult.size());
                whiteCarResult.read();
                try {
                    String srt3 = new String(whiteCarResult.struPlateInfo.sLicense, "GBK");
                    sAlarmType = sAlarmType + "：交通抓拍上传，车牌：" + srt3;
                    alarmCallbackHandler.handle(new AlramResult(AlramType.CAR_PASS, sAlarmType, srt3,pAlarmer.lUserID));
                } catch (UnsupportedEncodingException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            default:
                //其他报警
                alarmCallbackHandler.handle(new AlramResult(AlramType.OTHER, sAlarmType,"",pAlarmer.lUserID));
                break;
        }
    }
}
