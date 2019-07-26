package cn.haday.device.hksdk;

/**
 * 监听回调函数被处罚后，先分析事件的来源，然后把事件封装成{@link AlarmResult}后传递给此类进行处理
 * */
public interface AlarmCallbackHandler {
    /**
     * 对事件进行具体的处理
     * */
    void handle(AlarmResult alarmResult);
}
