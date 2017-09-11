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

public class GaodeMapHelper {

    public static Log errorLogger = LogFactory.getLog("errorLog");

    public static Map<String, String> getAddressByLocation(String longitude, String latitude) {
        try {
            StringBuilder url = new StringBuilder();
            url.append("http://restapi.amap.com/v3/geocode/regeo?key=bc2341ff70604e75192b86cad6c35360&location=");
            url.append(longitude);
            url.append(",");
            url.append(latitude);

            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(url.toString());

            CloseableHttpResponse response = httpclient.execute(httpget);
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                JSONObject contentJson = JSONObject.fromObject(content);
                if ("1".equals(contentJson.getString("status"))) {
                    Map<String, String> result = new HashMap<String, String>();
                    result.put("areaCode", contentJson.getJSONObject("regeocode").getJSONObject("addressComponent").getString("adcode"));
                    result.put("address", contentJson.getJSONObject("regeocode").getString("formatted_address"));
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
            url.append("http://restapi.amap.com/v3/geocode/geo?key=bc2341ff70604e75192b86cad6c35360&address=");
            url.append(URLEncoder.encode(address, "UTF-8"));

            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(url.toString());

            CloseableHttpResponse response = httpclient.execute(httpget);
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                JSONObject contentJson = JSONObject.fromObject(content);
                if ("1".equals(contentJson.getString("status"))) {
                    String location = contentJson.getJSONArray("geocodes").getJSONObject(0).getString("location");
                    String[] _location = location.split(",", 2);

                    Map<String, String> result = new HashMap<String, String>();
                    result.put("longitude", _location[0]);
                    result.put("latitude", _location[1]);
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

    public static Map<String, String> getWeatherByAreaCode(String areaCode) {
        try {
            StringBuilder url = new StringBuilder();
            url.append("http://restapi.amap.com/v3/weather/weatherInfo?key=bc2341ff70604e75192b86cad6c35360&city=");
            url.append(areaCode);

            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(url.toString());

            CloseableHttpResponse response = httpclient.execute(httpget);
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                JSONObject contentJson = JSONObject.fromObject(content);
                if ("1".equals(contentJson.getString("status"))) {
                    JSONObject weatherInfo = contentJson.getJSONArray("lives").getJSONObject(0);

                    Map<String, String> result = new HashMap<String, String>();
                    result.put("weather", weatherInfo.getString("weather"));
                    result.put("temperature", weatherInfo.getString("temperature") + "°C");
                    result.put("humidity", weatherInfo.getString("humidity") + "%");
                    result.put("city", weatherInfo.getString("city"));

                    // 修正城市
                    AreaCodeService areaCodeService = (AreaCodeService) SpringUtil.getBean("areaCodeService");
                    String[] areaNameArray = areaCodeService.getAreaNameArray(areaCode);
                    if (StringUtils.isNotEmpty(areaNameArray[1])) { // 市
                        String[] _city = areaNameArray[1].split("市", 2);
                        result.put("city", _city[0]);
                    }

                    return result;
                }
            }
        } catch (Exception e) {
            errorLogger.error("getWeatherByAreaCode异常！areaCode:" + areaCode, e);
        }

        return null;
    }

    public static Map<String, String> getWeatherByLocation(String longitude, String latitude) {
        Map<String, String> addressMap = getAddressByLocation(longitude, latitude);
        if (addressMap != null && StringUtils.isNotEmpty(addressMap.get("areaCode"))) {
            return getWeatherByAreaCode(addressMap.get("areaCode"));
        }

        return null;
    }

}
