package com.aiyolo.channel.data.response;

import java.util.Map;

import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class Response {

    protected static Log errorLogger = LogFactory.getLog("errorLog");

    public abstract Map<String, Object> responseHeader(String[] target) throws Exception;

    public abstract Map<String, Object> responseBody(JSONObject request, Object data) throws Exception;

}
