package com.aiyolo.common;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmsNewApi {

    // 下发地址
    private static final String URI_SEND_SMS = "http://120.55.116.6:7822/sms";

    // 编码方式
    private static final String ENCODING = "UTF-8";

    /**
     * 短信发送接口
     *
     * @param account  发送用户帐号
     * @param password 发送帐号密码
     * @param mobile   全部被叫号码
     * @param content  发送内容
     * @param extno    接入号
     * @return json格式字符串
     * @throws IOException
     */
    public static String sendSms(String account, String password, String mobile, String content, String extno) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "send");
        params.put("account", account);
        params.put("password", password);
        params.put("mobile", mobile);
        params.put("content", content);
        params.put("extno", extno);
        params.put("rt", "json");
        return post(URI_SEND_SMS, params);
    }

    /**
     * 基于HttpClient 4.3的通用POST方法
     *
     * @param url       提交的URL
     * @param paramsMap 提交<参数，值>Map
     * @return 提交响应
     */
    public static String post(String url, Map<String, String> paramsMap) {
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);
            if (paramsMap != null) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> param : paramsMap.entrySet()) {
                    NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
                    paramList.add(pair);
                }
                method.setEntity(new UrlEncodedFormEntity(paramList, ENCODING));
            }
            response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseText;
    }

}
