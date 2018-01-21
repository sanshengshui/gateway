package com.aiyolo.queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.aiyolo.channel.data.processor.ProcessorFactory;
import com.aiyolo.common.InputDataHelper;
import com.aiyolo.constant.ProtocolCodeConsts;
import com.aiyolo.constant.ProtocolFieldConsts;

import net.sf.json.JSONObject;

public class Receiver {

    private static Log mqLogger = LogFactory.getLog("mqLog");
    private static Log errorLogger = LogFactory.getLog("errorLog");

    @Autowired
    private ProcessorFactory processorFactory;

    public void receiveMessage(byte[] message) {
        try {
            String messageString = new String(message, "UTF-8");
            mqLogger.info("读出队列:" + messageString);

            JSONObject messageJson = JSONObject.fromObject(messageString);
            JSONObject messageHeaderJson = messageJson.getJSONObject("header");
            JSONObject messageBodyJson = messageJson.getJSONObject("body");

            int code = messageHeaderJson.getInt(ProtocolFieldConsts.CODE);


            switch (code) {
                case ProtocolCodeConsts.RECEIVE_FROM_GATEWAY:

                    String action = InputDataHelper.getAction(messageBodyJson);
                    byte[] actionByte = action.getBytes();
                    actionByte[0] = (byte) Character.toUpperCase(actionByte[0]);
                    for (int i = 0; i < (actionByte.length - 1); i++) {
                        if (actionByte[i] == '_') {
                            actionByte[i + 1] = (byte) Character
                                    .toUpperCase(actionByte[i + 1]);
                        }
                    }

                    String className = "";
                    if (new Integer(ProtocolCodeConsts.RECEIVE_FROM_GATEWAY)
                            .equals(code)) {
                        className = new StringBuilder().append("Gateway").append(new String(actionByte)
                                .replace("_", "")).append("Processor").toString();
                    }
//                    else if (new Integer(603).equals(code)) {
//                        className = new StringBuilder().append("App").append(new String(actionByte)
//                                .replace("_", "")).append("Processor").toString();
//                    }

                    processorFactory.getProcessor(className).run(messageString);

                    break;
                case ProtocolCodeConsts.SEND_TO_GATEWAY:
                    break;
                case ProtocolCodeConsts.GATEWAY_BEAT:
                    processorFactory.getProcessor("GatewayBeatProcessor")
                            .run(messageString); // 网关心跳
                    break;
                case ProtocolCodeConsts.PUSH_TO_APP:
                    break;
                case ProtocolCodeConsts.DEVICE_INFO:
                    processorFactory.getProcessor("GatewayInfoProcessor")
                            .run(messageString);
                    break;
                case ProtocolCodeConsts.APP_LOGIN:
                    processorFactory.getProcessor("AppUserLoginProcessor")
                            .run(messageString);
                    break;
            }

            //
            //            if (new Integer(702).equals(code)) {
            //                processorFactory.getProcessor("GatewayBeatProcessor").run(messageString); // 网关心跳
            //            } else if ("info".equals(messageHeaderJson.get("cmd"))) {
            //                processorFactory.getProcessor("GatewayInfoProcessor").run(messageString);
            //            } else if ("user_login".equals(messageHeaderJson.get("cmd"))) {
            //                processorFactory.getProcessor("AppUserLoginProcessor").run(messageString);
            //            } else {
            //                String action = messageBodyJson.get("action") != null ? (String) messageBodyJson.get("action") : (String) messageBodyJson.get("act");
            //                byte[] actionByte = action.getBytes();
            //                actionByte[0] = (byte) Character.toUpperCase(actionByte[0]);
            //                for (int i = 0; i < (actionByte.length - 1); i++) {
            //                    if (actionByte[i] == '_') {
            //                        actionByte[i + 1] = (byte) Character.toUpperCase(actionByte[i + 1]);
            //                    }
            //                }
            //
            //                String className = "";
            //                if (new Integer(601).equals(code)) {
            //                    className = new StringBuilder().append("Gateway").append(new String(actionByte).replace("_", "")).append("Processor").toString();
            //                } else if (new Integer(603).equals(code)) {
            //                    className = new StringBuilder().append("App").append(new String(actionByte).replace("_", "")).append("Processor").toString();
            //                }
            //
            //                processorFactory.getProcessor(className).run(messageString);
            //            }
        } catch (Exception e) {
            errorLogger.error("ReceiveMessage异常！", e);
        }
    }

}
