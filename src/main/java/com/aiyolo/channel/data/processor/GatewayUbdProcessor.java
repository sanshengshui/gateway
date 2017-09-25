package com.aiyolo.channel.data.processor;

import com.aiyolo.common.SpringUtil;
import com.aiyolo.constant.AppNoticeTypeConsts;
import com.aiyolo.entity.AppUserGateway;
import com.aiyolo.entity.Gateway;
import com.aiyolo.repository.AppUserGatewayRepository;
import com.aiyolo.repository.GatewayRepository;
import com.aiyolo.service.GatewayService;
import com.aiyolo.service.GatewayStatusService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class GatewayUbdProcessor extends Processor {

    private static Log gatewayLogger = LogFactory.getLog("gatewayLog");

    @Override
    public void run(String message) {
        try {
            init(message);

            String imei = messageBodyJson.getString("imei");

            GatewayService gatewayService = (GatewayService) SpringUtil.getBean("gatewayService");
            List<AppUserGateway> gatewayAppUsers = gatewayService.getGatewayAppUsers(imei);
            if (gatewayAppUsers != null) {
                AppUserGatewayRepository appUserGatewayRepository = (AppUserGatewayRepository) SpringUtil.getBean("appUserGatewayRepository");
                appUserGatewayRepository.delete(gatewayAppUsers);
            }

            GatewayRepository gatewayRepository = (GatewayRepository) SpringUtil.getBean("gatewayRepository");
            Gateway gateway = gatewayRepository.findFirstByGlImeiOrderByIdDesc(imei);
            if (gateway != null) {
                gateway.setGlName("");
                gateway.setUserName("");
                gateway.setUserPhones("");
                gateway.setAreaCode("");
                gateway.setVillage("");
                gateway.setAddress("");
                gateway.setGlLongitude("");
                gateway.setGlLatitude("");
                gatewayRepository.save(gateway);
            }

            // 推送给app
            GatewayStatusService gatewayStatusService = (GatewayStatusService) SpringUtil.getBean("gatewayStatusService");
            gatewayStatusService.pushGatewayStatus(gateway, AppNoticeTypeConsts.DELETE);

            // 写入文件待后续处理
            gatewayLogger.info(message);
        } catch (Exception e) {
            errorLogger.error("GatewayUbdProcessor异常！message:" + message, e);
        }
    }

}
