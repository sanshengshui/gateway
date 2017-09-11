package com.aiyolo.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class StringHelper {

    public static String stripHTMLTag(String str) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(str);
        str = m_script.replaceAll(""); //过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(str);
        str = m_style.replaceAll(""); //过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(str);
        str = m_html.replaceAll(""); //过滤html标签

       return str.trim(); //返回文本字符串
    }

    // 下划线转驼峰
    public static String underline2Camel(String str) {
        return underline2Camel(str, false);
    }

    // 下划线转驼峰
    public static String underline2Camel(String str, boolean firstLetter2Upper) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }

        int len = str.length();
        StringBuilder builder = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (builder.length() == 0 && Character.isDigit(c)) {
                continue;
            }

            if (c == '_') {
                if (++i < len) {
                    builder.append(Character.toUpperCase(str.charAt(i)));
                }
            } else {
                if (builder.length() == 0 && firstLetter2Upper) {
                    builder.append(Character.toUpperCase(c));
                } else {
                    builder.append(Character.toLowerCase(c));
                }
            }
        }

        return builder.toString();
    }

    // 驼峰转下划线
    public static String camel2Underline(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }

        StringBuilder builder = new StringBuilder(str);
        Pattern p = Pattern.compile("[A-Z]");
        Matcher mc = p.matcher(str);
        int i = 0;
        while (mc.find()) {
            builder.replace(mc.start() + i, mc.end() + i, "_" + mc.group().toLowerCase());
            i++;
        }

        if ('_' == builder.charAt(0)) {
            builder.deleteCharAt(0);
        }

        return builder.toString();
    }

    /**
     * 检验经纬度是否合法值
     *
     * @param longitude
     * @param latitude
     * @return
     */
    public static Boolean checkLocation(String longitude, String latitude) {
        Float longitudeFloat;
        Float latitudeFloat;

        try {
            longitudeFloat = Float.parseFloat(longitude);
        } catch (NumberFormatException e) {
            longitudeFloat = (float) 0.0;
        }

        try {
            latitudeFloat = Float.parseFloat(latitude);
        } catch (NumberFormatException e) {
            latitudeFloat = (float) 0.0;
        }

        if (longitudeFloat == 0 || latitudeFloat == 0) {
            return false;
        }

        return true;
    }

}
