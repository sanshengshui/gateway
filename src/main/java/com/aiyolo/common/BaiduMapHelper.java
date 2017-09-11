package com.aiyolo.common;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.aiyolo.service.AreaCodeService;

import net.sf.json.JSONObject;

public class BaiduMapHelper {

    public static Log errorLogger = LogFactory.getLog("errorLog");

    public static Map<String, String> getAddressByLocation(String longitude, String latitude) {
        try {
            StringBuilder url = new StringBuilder();
            url.append("http://api.map.baidu.com/geocoder/v2/?ak=M3aKsgyrsHsi2TtMPYMwVilabm6XkN60&output=json&location=");
            url.append(latitude);
            url.append(",");
            url.append(longitude);

            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(url.toString());

            CloseableHttpResponse response = httpclient.execute(httpget);
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                JSONObject contentJson = JSONObject.fromObject(content);
                if (contentJson.getInt("status") == 0) {
                    Map<String, String> result = new HashMap<String, String>();
                    result.put("areaCode", contentJson.getJSONObject("result").getJSONObject("addressComponent").getString("adcode"));
                    result.put("address", contentJson.getJSONObject("result").getString("formatted_address"));
                    return result;
                }
            }
        } catch (Exception e) {
            errorLogger.error("getAddressByLocation异常！location:" + longitude + "," + latitude, e);
        }

        return null;
    }

    public static Map<String, String> getLocationByAddress(String address) {
        try {
            StringBuilder url = new StringBuilder();
            url.append("http://api.map.baidu.com/geocoder/v2/?ak=M3aKsgyrsHsi2TtMPYMwVilabm6XkN60&output=json&address=");
            url.append(URLEncoder.encode(address, "UTF-8"));

            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(url.toString());

            CloseableHttpResponse response = httpclient.execute(httpget);
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                JSONObject contentJson = JSONObject.fromObject(content);
                if (contentJson.getInt("status") == 0) {
                    Map<String, String> result = new HashMap<String, String>();
                    result.put("longitude", contentJson.getJSONObject("result").getJSONObject("location").getString("lng")); // TODO:丢失精度，待解决
                    result.put("latitude", contentJson.getJSONObject("result").getJSONObject("location").getString("lat")); // TODO:丢失精度，待解决
                    return result;
                }
            }
        } catch (Exception e) {
            errorLogger.error("getLocationByAddress异常！address:" + address, e);
        }

        return null;
    }

    public static Map<String, String> getLocationByAddress(String address, String areaCode) {
        AreaCodeService areaCodeService = (AreaCodeService) SpringUtil.getBean("areaCodeService");

        String areaName = areaCodeService.getAreaName(areaCode);
        String _address = StringUtils.isNotEmpty(areaName) ? (areaName + address) : address;

        return getLocationByAddress(_address);
    }

    /**
     * 查询基站位置数据
     *
     * 百度无此接口，从第三方接口实现
     * 文档地址：http://www.cellocation.com/interfac/
     *
     * @param mcc
     * @param mnc
     * @param lac
     * @param ci
     * @return
     */
    public static Map<String, String> getLocationBySta(int mcc, int mnc, int lac, int ci) {
        try {
            StringBuilder url = new StringBuilder();
            url.append("http://api.cellocation.com/cell/?output=json");
            url.append("&mcc=");
            url.append(mcc);
            url.append("&mnc=");
            url.append(mnc);
            url.append("&lac=");
            url.append(lac);
            url.append("&ci=");
            url.append(ci);

            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(url.toString());

            CloseableHttpResponse response = httpclient.execute(httpget);
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                JSONObject contentJson = JSONObject.fromObject(content);
                if (contentJson.getInt("errcode") == 0) {
                    Map<String, String> result = new HashMap<String, String>();
                    result.put("longitude", contentJson.getString("lon"));
                    result.put("latitude", contentJson.getString("lat"));
                    return result;
                }
            }
        } catch (Exception e) {
            errorLogger.error("getLocationBySta异常！sta:" + mcc + "," + mnc + "," + lac + "," + ci, e);
        }

        return null;
    }

    public static Map<String, String> getAddressBySta(int mcc, int mnc, int lac, int ci) {
        Map<String, String> location = getLocationBySta(mcc, mnc, lac, ci);
        if (location != null) {
            return getAddressByLocation(location.get("longitude"), location.get("latitude"));
        }

        return null;
    }

}
