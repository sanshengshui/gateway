package com.aiyolo.service;

import com.aiyolo.channel.data.request.AppNoticeGatewayRequest;
import com.aiyolo.constant.AppNoticeTypeConsts;
import com.aiyolo.entity.Gateway;
import com.aiyolo.entity.GatewayStatus;
import com.aiyolo.queue.Sender;
import com.aiyolo.repository.GatewayRepository;
import com.aiyolo.repository.GatewayStatusRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GatewayStatusService {

    private static final Log errorLogger = LogFactory.getLog("errorLog");
    private static final Log taskLogger = LogFactory.getLog("taskLog");

    @Autowired Sender sender;

    @Autowired GatewayRepository gatewayRepository;
    @Autowired GatewayStatusRepository gatewayStatusRepository;

    @Autowired GatewayService gatewayService;

    public void pushGatewayStatus(Gateway gateway) {
        pushGatewayStatus(gateway, AppNoticeTypeConsts.MODIFY);
    }

    public void pushGatewayStatus(Gateway gateway, Integer noticeType) {
        if (gateway == null) {
            return;
        }

        try {
            String[] mobileIds = gatewayService.getGatewayUserMobileIds(gateway.getGlImei());
            if (mobileIds != null && mobileIds.length > 0) {
                // 推送给app
                Map<String, Object> headerMap = AppNoticeGatewayRequest.getInstance().requestHeader(mobileIds);
                headerMap.put("cache_time", 24 * 60 * 60 * 1000L);

                Map<String, Object> queryParamMap = new HashMap<String, Object>();
                queryParamMap.put("imei", gateway.getGlImei());
                queryParamMap.put("notice", noticeType);
                queryParamMap.put("dev_num", 1);
                queryParamMap.put("online", 1);

                GatewayStatus gatewayStatus = gatewayStatusRepository.findFirstByGlImeiOrderByIdDesc(gateway.getGlImei());
                queryParamMap.put("err", gatewayStatus.getStatus());
                queryParamMap.put("temp", gatewayStatus.getTemperature());
                queryParamMap.put("hum", gatewayStatus.getHumidity());
                queryParamMap.put("atm", gatewayStatus.getAtmosphere());
                queryParamMap.put("ver", gatewayStatus.getVersion());

                Map<String, Object> bodyMap = AppNoticeGatewayRequest.getInstance().requestBody(queryParamMap);

                sender.sendMessage(headerMap, bodyMap);
            }
        } catch (Exception e) {
            errorLogger.error("pushGatewayStatus异常！gateway:" + gateway.toString(), e);
        }
    }

    @Async
    public void notifyGatewayLiveStatusChange(String glId) {
        try {
            Gateway gateway = gatewayRepository.findFirstByGlIdOrderByIdDesc(glId);
            if (gateway != null) {
                pushGatewayStatus(gateway);
            }
            taskLogger.info("notifyGatewayLiveStatusChange completed.(glId:" + glId + ")");
        } catch (Exception e) {
            errorLogger.error("notifyGatewayLiveStatusChange异常！glId:" + glId, e);
        }
    }

}
