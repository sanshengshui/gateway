package com.aiyolo.channel.data.processor;

import com.aiyolo.common.SpringUtil;
import com.aiyolo.common.StringHelper;
import com.aiyolo.constant.ProtocolFieldConsts;
import com.aiyolo.entity.Gateway;
import com.aiyolo.repository.GatewayRepository;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

public class GatewayInfoProcessor extends Processor {

    private static Log gatewayLogger = LogFactory.getLog("gatewayLog");

    public static final Map<String, String> MAP_MSG_IMEI = new HashMap<>();

    @Override
    public void run(String message) {
        try {
            init(message);
            String messageId = messageHeaderJson.getString(ProtocolFieldConsts.MESSAGE);
            String glImei = MAP_MSG_IMEI.get(messageId);
            if (glImei != null) {
                //            String glImei = messageHeaderJson.getString(ProtocolFieldConsts.IMEI);
                String logintime = messageHeaderJson.getString(ProtocolFieldConsts.LOGIN_TIME);
                String longitude = messageHeaderJson.getString(ProtocolFieldConsts.LONGITUDE);
                String latitude = messageHeaderJson.getString(ProtocolFieldConsts.LATITUDE);

                if (StringUtils.isNotEmpty(logintime) && StringHelper.checkLocation(longitude, latitude)) {
                    GatewayRepository gatewayRepository = (GatewayRepository) SpringUtil.getBean("gatewayRepository");

                    // 查询该网关是否存在
                    Gateway gateway = gatewayRepository.findFirstByGlImeiOrderByIdDesc(glImei);
                    if (gateway != null) {
                        gateway.setGlLogintime(logintime);
                        gateway.setGlLongitude(longitude);
                        gateway.setGlLatitude(latitude);
                        gatewayRepository.save(gateway);
                    }
                }
            }


            // 写入文件待后续处理
            gatewayLogger.info(message);
        } catch (Exception e) {
            errorLogger.error("GatewayInfoProcessor异常！message:" + message, e);
        }
    }

}
