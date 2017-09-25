package com.aiyolo.channel.data.processor;

public class GatewayBroaProcessor extends Processor {

    @Override
    public void run(String message) {
        try {
            // 网关响应，不作处理

        } catch (Exception e) {
            errorLogger.error("GatewayBroaProcessor异常！message:" + message, e);
        }
    }

}
