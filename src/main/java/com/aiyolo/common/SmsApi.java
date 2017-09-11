package com.aiyolo.common;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class SmsApi {

    //查账户信息的http地址
    private static final String URI_GET_USER_INFO = "http://sms.haotingyun.com/v2/user/get.json";

    //修改账户信息的http地址
    private static final String URI_SET_USER_INFO = "http://sms.haotingyun.com/v2/user/set.json";

    //取默认模板
    private static final String URI_GET_USER_TPL = "http://sms.haotingyun.com/v2/tpl/get_default.json";

    //添加模板
    private static final String URI_SET_USER_TPL = "http://sms.haotingyun.com/v2/tpl/add.json";

    //智能匹配模版发送接口的http地址
    private static final String URI_SEND_SMS = "http://sms.haotingyun.com/v2/sms/single_send.json";

    //智能匹配模版批量发送接口的http地址
    private static final String URI_BATCH_SEND_SMS = "http://sms.haotingyun.com/v2/sms/batch_send.json";

    //模板发送接口的http地址
    private static final String URI_TPL_SEND_SMS = "http://sms.haotingyun.com/v2/sms/tpl_single_send.json";

    //个性短信接口的http地址
    private static final String Multi_SEND_SMS = "http://sms.haotingyun.com/v2/sms/multi_send.json";

    //编码格式。发送编码格式统一用UTF-8
    private static final String ENCODING = "UTF-8";

    /**
     * 取账户信息
     *
     * @return json格式字符串
     * @throws java.io.IOException
     */
    public static String getUserInfo(String apikey) throws IOException, URISyntaxException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", apikey);
        return post(URI_GET_USER_INFO, params);
    }

    /**
     * 修改账户信息
     *
     * @return json格式字符串
     * @throws java.io.IOException
     */
    public static String setUserInfo(String apikey) throws IOException, URISyntaxException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", apikey);
        params.put("emergency_contact", "zhansan");
        params.put("emergency_mobile", "187021****3");
        params.put("alarm_balance", "10");
        return post(URI_SET_USER_INFO, params);
    }

    /**
     * 调取模板信息
     *
     * @return json格式字符串
     * @throws java.io.IOException
     */
    public static String getUserTpl(String apikey) throws IOException, URISyntaxException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", apikey);
        params.put("tpl_id", "2");
        return post(URI_GET_USER_TPL, params);
    }

    /**
     * 添加模板
     *
     * @return json格式字符串
     * @throws java.io.IOException
     */
    public static String setUserTpl(String apikey, String tpl_content) throws IOException, URISyntaxException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", apikey);
        params.put("tpl_content", tpl_content);
        params.put("notify_type", "0");
        return post(URI_SET_USER_TPL, params);
    }

    /**
     * 智能匹配模版接口发短信
     *
     * @param apikey apikey
     * @param text   短信内容
     * @param mobile 接受的手机号
     * @return json格式字符串
     * @throws IOException
     */
    public static String sendSms(String apikey, String text, String mobile) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", apikey);
        params.put("text", text);
        params.put("mobile", mobile);
        return post(URI_SEND_SMS, params);
    }

    /**
     * 智能匹配模版接口批量发短信
     *
     * @param apikey apikey
     * @param text   短信内容
     * @param mobile 接受的手机号
     * @return json格式字符串
     * @throws IOException
     */
    public static String sendBatchSms(String apikey, String text, String mobile) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", apikey);
        params.put("text", text);
        params.put("mobile", mobile);
        return post(URI_BATCH_SEND_SMS, params);
    }

    /**
     * 发送个性短信
     *
     * @param apikey    apikey
     * @param textary   短信内容数组
     * @param phoneary  接受的手机号数组
     * @return json格式字符串
     * @throws IOException
     */
    public static String multisendSms(String apikey, String[] phoneary, String[] textary) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", apikey);
        StringBuffer pb = new StringBuffer();
        StringBuffer tb = new StringBuffer();
        //对号码数组进行处理
        for (int i = 0; i < phoneary.length; i++) {
            pb.append(phoneary[i].trim() + ",");
        }
        String mobile = pb.toString();
        mobile = mobile.substring(0, mobile.length() - 1);
        //对内容数组进行处理
        for (int i = 0; i < textary.length; i++) {
            //短信内容做url转码
            String t = URLEncoder.encode(textary[i], "UTF-8");
            tb.append(t.trim() + ",");
        }
        String text = tb.toString();
        text = text.substring(0, text.length() - 1);

        params.put("text", text);
        params.put("mobile", mobile);
        return post(Multi_SEND_SMS, params);
    }

    /**
     * 通过模板发送短信(不推荐)
     *
     * @param apikey    apikey
     * @param tpl_id    模板id
     * @param tpl_value 模板变量值
     * @param mobile    接受的手机号
     * @return json格式字符串
     * @throws IOException
     */
    public static String tplSendSms(String apikey, long tpl_id, String tpl_value, String mobile) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", apikey);
        params.put("tpl_id", String.valueOf(tpl_id));
        params.put("tpl_value", tpl_value);
        params.put("mobile", mobile);
        return post(URI_TPL_SEND_SMS, params);
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
