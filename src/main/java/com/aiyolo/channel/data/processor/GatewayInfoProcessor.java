package com.aiyolo.channel.data.processor;

import com.aiyolo.common.SpringUtil;
import com.aiyolo.common.StringHelper;
import com.aiyolo.entity.Gateway;
import com.aiyolo.repository.GatewayRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GatewayInfoProcessor extends Processor {

    private static Log gatewayLogger = LogFactory.getLog("gatewayLog");

    @Override
    public void run(String message) {
        try {
            init(message);

            String logintime = messageHeaderJson.getString("logintime");
            String longitude = messageHeaderJson.getString("longitude");
            String latitude = messageHeaderJson.getString("latitude");

            if (StringUtils.isNotEmpty(logintime) && StringHelper.checkLocation(longitude, latitude)) {
                GatewayRepository gatewayRepository = (GatewayRepository) SpringUtil.getBean("gatewayRepository");

                // 查询该网关是否存在
                Gateway gateway = gatewayRepository.findFirstByGlIdOrderByIdDesc(messageHeaderJson.getString("gl_id"));
                if (gateway != null) {
                    gateway.setGlLogintime(logintime);
                    gateway.setGlLongitude(longitude);
                    gateway.setGlLatitude(latitude);
                    gatewayRepository.save(gateway);
                }
            }

            // 写入文件待后续处理
            gatewayLogger.info(message);
        } catch (Exception e) {
            errorLogger.error("GatewayInfoProcessor异常！message:" + message, e);
        }
    }

}
