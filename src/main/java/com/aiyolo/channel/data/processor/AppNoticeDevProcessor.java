package com.aiyolo.channel.data.processor;

public class AppNoticeDevProcessor extends Processor {

    @Override
    public void run(String message) {
        try {
            // APP响应，不作处理

        } catch (Exception e) {
            errorLogger.error("AppNoticeDevProcessor异常！message:" + message, e);
        }
    }

}
