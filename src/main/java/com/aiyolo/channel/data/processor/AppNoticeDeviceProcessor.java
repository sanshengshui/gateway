package com.aiyolo.channel.data.processor;

public class AppNoticeDeviceProcessor extends Processor {

    @Override
    public void run(String message) {
        try {
            // APP响应，不作处理

        } catch (Exception e) {
            errorLogger.error("AppNoticeDeviceProcessor异常！message:" + message, e);
        }
    }

}
