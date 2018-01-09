package com.aiyolo.queue;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.aiyolo.constant.QueueConsts;

import net.sf.json.JSONObject;

public class Sender {

    private static Log mqLogger = LogFactory.getLog("mqLog");
    private static Log errorLogger = LogFactory.getLog("errorLog");

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(Map<String, Object> headerMap, Map<String, Object> bodyMap) {
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("header", headerMap);
            data.put("body", bodyMap);
            data.put("time", System.currentTimeMillis());
            String messageString = JSONObject.fromObject(data).toString();

            rabbitTemplate.convertAndSend(QueueConsts.outputQueueName, messageString);

            mqLogger.info("写入队列:" + messageString);
        } catch (Exception e) {
            errorLogger.error("SendMessage异常！", e);
        }
    }

}
