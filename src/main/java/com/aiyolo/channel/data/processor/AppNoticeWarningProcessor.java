package com.aiyolo.channel.data.processor;

public class AppNoticeWarningProcessor extends Processor {

    @Override
    public void run(String message) {
        try {
            // APP响应预报警通知，不作处理

        } catch (Exception e) {
            errorLogger.error("AppNoticeWarningProcessor异常！message:" + message, e);
        }
    }

}
