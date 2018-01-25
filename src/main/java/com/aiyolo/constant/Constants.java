package com.aiyolo.constant;

/**
 * 静态变量及配置信息
 * 
 * @author hl
 */
public interface Constants
{
    String MESSAGE_ID = "GL-%06d-%010d";
    String DELIMITER = "\0";
    String PRODUCT_ID = "product_id";
    String IMEI = "imei";
    String CODE = "code";
    String MESSAGE = "message";
    String RESULT = "result";
    String LENGTH = "length";
    String IMEIS = "imeis";
    String MOBILE_IDS = "mobile_ids";

    String UTF_8 = "UTF-8";
    String POST = "POST";
    String APPLICATION_JSON = "application/json";
    int HTTP_TIMEOUT = 5000;
    /**
     * 以下信息需服务端提供并修改
     */
    String DES_KEY = "12345678";// des加解密密码
    String DEVICE_PRODUCT_ID = "40001040010011";// 产品id
    String URL = "http://test.igelian.com:9111/mpfesHttp/s/d";// 个联服务接口地址
    String WHITELIST = "*";// 个联服务器ip白名单

}
