package com.aiyolo.channel.data.request;

import java.util.Map;
import java.util.Random;

import com.aiyolo.constant.ChannelConsts;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class Request {

    protected static Log errorLogger = LogFactory.getLog("errorLog");

    public abstract Map<String, Object> requestHeader(String[] target) throws Exception;

    public abstract Map<String, Object> requestBody(Map<String, Object> data) throws Exception;

    public String generateMessageId() {
        Random random = new Random();
        return String.format(ChannelConsts.MESSAGE_ID, random.nextInt(999999), System.currentTimeMillis() / 1000);
    }

}
