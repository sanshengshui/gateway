package com.aiyolo.queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.aiyolo.channel.data.processor.ProcessorFactory;
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

            if (new Integer(702).equals(messageHeaderJson.get("code"))) {
                processorFactory.getProcessor("GatewayBeatProcessor").run(messageString); // 网关心跳
            } else if ("info".equals(messageHeaderJson.get("cmd"))) {
                processorFactory.getProcessor("GatewayInfoProcessor").run(messageString);
            } else if ("user_login".equals(messageHeaderJson.get("cmd"))) {
                processorFactory.getProcessor("AppUserLoginProcessor").run(messageString);
            } else {
                String action = messageBodyJson.get("action") != null ? (String) messageBodyJson.get("action") : (String) messageBodyJson.get("act");
                byte[] actionByte = action.getBytes();
                actionByte[0] = (byte) Character.toUpperCase(actionByte[0]);
                for (int i = 0; i < (actionByte.length - 1); i++) {
                    if (actionByte[i] == '_') {
                        actionByte[i + 1] = (byte) Character.toUpperCase(actionByte[i + 1]);
                    }
                }

                String className = "";
                if (new Integer(601).equals(messageHeaderJson.get("code"))) {
                    className = new StringBuilder().append("Gateway").append(new String(actionByte).replace("_", "")).append("Processor").toString();
                } else if (new Integer(603).equals(messageHeaderJson.get("code"))) {
                    className = new StringBuilder().append("App").append(new String(actionByte).replace("_", "")).append("Processor").toString();
                }

                processorFactory.getProcessor(className).run(messageString);
            }
        } catch (Exception e) {
            errorLogger.error("ReceiveMessage异常！", e);
        }
    }

}
