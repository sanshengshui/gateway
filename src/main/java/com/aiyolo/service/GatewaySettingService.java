package com.aiyolo.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aiyolo.entity.Gateway;
import com.aiyolo.entity.GatewaySetting;
import com.aiyolo.repository.GatewaySettingRepository;

@Service
public class GatewaySettingService {

    @Autowired GatewaySettingRepository gatewaySettingRepository;

    public GatewaySetting getGatewaySetting(Gateway gateway) {
        if (gateway != null && StringUtils.isNotEmpty(gateway.getGlImei()) && StringUtils.isNotEmpty(gateway.getAreaCode())) {
            String areaCode = gateway.getAreaCode().substring(0, 2) + "0000";
            GatewaySetting gatewaySetting = gatewaySettingRepository.findFirstByGlImeiOrAreaCodeOrderByIdDesc(gateway.getGlImei(), areaCode);

            return gatewaySetting;
        }

        return null;
    }

}
