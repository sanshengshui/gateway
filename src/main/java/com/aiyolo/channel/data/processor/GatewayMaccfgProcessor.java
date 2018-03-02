package com.aiyolo.channel.data.processor;

public class GatewayMaccfgProcessor extends Processor {

    @Override
    public void run(String message) {
        try {
            // 网关响应，不作处理

        } catch (Exception e) {
            errorLogger.error("GatewayMaccfgProcessor异常！message:" + message, e);
        }
    }

}
