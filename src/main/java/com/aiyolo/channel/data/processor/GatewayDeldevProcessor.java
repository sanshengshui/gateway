package com.aiyolo.channel.data.processor;

public class GatewayDeldevProcessor extends Processor {

    @Override
    public void run(String message) {
        try {
            // 网关响应，不作处理

        } catch (Exception e) {
            errorLogger.error("GatewayDeldevProcessor异常！message:" + message, e);
        }
    }

}
