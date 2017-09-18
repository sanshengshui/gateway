package com.aiyolo.service;

import com.aiyolo.channel.data.request.AppMessagePushRequest;
import com.aiyolo.queue.Sender;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MessagePushService {

    private static final Log errorLogger = LogFactory.getLog("errorLog");

    @Autowired Sender sender;

    @Autowired GatewayService gatewayService;

    public void pushMessage(String glImei, String glId, String title, String text) {
        String[] mobileIds = gatewayService.getGatewayUserMobileIds(glImei);
        if (mobileIds != null && mobileIds.length > 0) {
            pushMessage(mobileIds, glId, title, text);
        }
    }

    public void pushMessage(String[] mobileIds, String glId, String title, String text) {
        if (mobileIds == null || mobileIds.length == 0 || StringUtils.isEmpty(text)) {
            return;
        }

        try {
            // 推送给个推
            Map<String, Object> headerMap = AppMessagePushRequest.getInstance().requestHeader(mobileIds);

            Map<String, Object> queryParamMap = new HashMap<String, Object>();
            queryParamMap.put("glId", glId);
            queryParamMap.put("title", title);
            queryParamMap.put("text", text);

            Map<String, Object> bodyMap = AppMessagePushRequest.getInstance().requestBody(queryParamMap);

            sender.sendMessage(headerMap, bodyMap);
        } catch (Exception e) {
            errorLogger.error("pushMessage异常！glId:" + glId + ", title:" + title + ", text:" + text, e);
        }
    }

}
