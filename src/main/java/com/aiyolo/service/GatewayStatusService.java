package com.aiyolo.service;

import com.aiyolo.cache.GatewayLiveStatusCache;
import com.aiyolo.channel.data.request.AppNoticeGatewayRequest;
import com.aiyolo.constant.AppNoticeTypeConsts;
import com.aiyolo.constant.PushConsts;
import com.aiyolo.entity.Device;
import com.aiyolo.entity.Gateway;
import com.aiyolo.entity.GatewayStatus;
import com.aiyolo.queue.Sender;
import com.aiyolo.repository.DeviceRepository;
import com.aiyolo.repository.GatewayRepository;
import com.aiyolo.repository.GatewayStatusRepository;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GatewayStatusService {

    private static final Log errorLogger = LogFactory.getLog("errorLog");
    private static final Log taskLogger = LogFactory.getLog("taskLog");

    @Autowired
    Sender sender;

    @Autowired
    GatewayLiveStatusCache gatewayLiveStatusCache;

    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    GatewayRepository gatewayRepository;
    @Autowired
    GatewayStatusRepository gatewayStatusRepository;

    @Autowired
    GatewayService gatewayService;

//    public void pushGatewayStatus(Gateway gateway) {
//        pushGatewayStatus(gateway, AppNoticeTypeConsts.MODIFY, null);
//    }
    public void pushGatewayStatus(String glImei) {
        pushGatewayStatus(glImei, AppNoticeTypeConsts.MODIFY, null);
    }

    public void pushGatewayStatus(String glImei, Integer noticeType, String[] mobileIds) {
        if (StringUtils.isEmpty(glImei)) {
            return;
        }

        try {
            if (mobileIds == null) {
                mobileIds = gatewayService.getGatewayUserMobileIds(glImei);
            }

            if (mobileIds != null && mobileIds.length > 0) {
                // 推送给APP
                Map<String, Object> headerMap = AppNoticeGatewayRequest.getInstance().requestHeader(mobileIds);

                Map<String, Object> queryParamMap = new HashMap<String, Object>();
                queryParamMap.put("imei", glImei);
                queryParamMap.put("notice", noticeType);

                List<Device> devices = deviceRepository.findByGlImei(glImei);
                queryParamMap.put("dev_num", devices.size());

                int gatewayLiveStatus = gatewayLiveStatusCache.getByGlImei(glImei);
                queryParamMap.put("online", gatewayLiveStatus);

                GatewayStatus gatewayStatus = gatewayStatusRepository.findFirstByGlImeiOrderByIdDesc(glImei);
                if (gatewayStatus != null) {
                    queryParamMap.put("err", gatewayStatus.getStatus());
                    queryParamMap.put("temp", gatewayStatus.getTemperature());
                    queryParamMap.put("hum", gatewayStatus.getHumidity());
                    queryParamMap.put("atm", gatewayStatus.getAtmosphere());
                    queryParamMap.put("ver", gatewayStatus.getVersion());
                    queryParamMap.put("check", gatewayStatus.getChecked());
                    queryParamMap.put("htmp", gatewayStatus.getHtmp());
                }

                Map<String, Object> bodyMap = AppNoticeGatewayRequest.getInstance().requestBody(queryParamMap);

                sender.sendMessage(headerMap, bodyMap);
            }
        } catch (Exception e) {
            errorLogger.error("pushGatewayStatus异常！gateway:" + glImei, e);
        }
    }

//    public void pushGatewayStatus(String glImei) {
//        Gateway gateway = gatewayRepository.findFirstByGlImeiOrderByIdDesc(glImei);
//        if (gateway == null) {
//            return;
//        }
//
//        pushGatewayStatus(gateway);
//    }

    @Async
    public void notifyGatewayLiveStatusChange(String glImei) {
        try {
            Gateway gateway = gatewayRepository.findFirstByGlImeiOrderByIdDesc(glImei);
            if (gateway != null) {
                pushGatewayStatus(glImei);
            }
            taskLogger.info("notifyGatewayLiveStatusChange completed.(glImei:" + glImei + ")");
        } catch (Exception e) {
            errorLogger.error("notifyGatewayLiveStatusChange异常！glImei:" + glImei, e);
        }
    }

}
