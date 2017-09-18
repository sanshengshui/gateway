package com.aiyolo.controller;

import com.aiyolo.common.ArrayHelper;
import com.aiyolo.common.BaiduMapHelper;
import com.aiyolo.entity.Gateway;
import com.aiyolo.entity.DeviceAlarm;
import com.aiyolo.repository.DeviceAlarmRepository;
import com.aiyolo.repository.GatewayRepository;
import com.aiyolo.service.AreaCodeService;
import com.aiyolo.service.GatewayService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired GatewayRepository gatewayRepository;
    @Autowired DeviceAlarmRepository deviceAlarmRepository;

    @Autowired GatewayService gatewayService;
    @Autowired AreaCodeService areaCodeService;

    @RequestMapping("/data")
    public Map<String, Object> data(
            @RequestParam(value="id", defaultValue="0") String id,
            @RequestParam(value="areaCode", defaultValue="0") String areaCode) {
        Long gatewayId;
        try {
            gatewayId = Long.valueOf(id);
        } catch (NumberFormatException e) {
            return null;
        }

        if (! areaCodeService.checkAreaCode(areaCode, new String[] {"0"})) {
            return null;
        }

        List<Gateway> gateways = new ArrayList<Gateway>();
        if (gatewayId == 0) {
            gateways = gatewayService.getAllGatewayByAreaCode(areaCode);
        } else {
            Gateway gateway = gatewayService.getGatewayById(gatewayId);
            if (gateway != null) {
                gateways.add(gateway);
            }
        }

        Map<String, DeviceAlarm> deviceAlarms = new HashMap<String, DeviceAlarm>();
        for (int i = 0; i < gateways.size(); i++) {
            if (StringUtils.isNotEmpty(gateways.get(i).getAreaCode())) {
                String areaName = areaCodeService.getAreaName(gateways.get(i).getAreaCode());
                if (StringUtils.isNotEmpty(areaName) && StringUtils.isEmpty(gateways.get(i).getAddressLocation())) {
                    String fullAddress = areaName + gateways.get(i).getAddress();
                    Map<String, String> addressLocation = BaiduMapHelper.getLocationByAddress(fullAddress);
                    if (addressLocation != null) {
                        gateways.get(i).setAddressLocation(addressLocation.get("longitude") + "," + addressLocation.get("latitude"));
                        gatewayRepository.save(gateways.get(i));
                    }
                }
            }

            // 添加网关管理员手机至userPhones
            String managerPhone = gatewayService.getManagerPhone(gateways.get(i).getGlImei());
            if (StringUtils.isNotEmpty(managerPhone)) {
                String[] userPhones = ArrayHelper.getStringArray(gateways.get(i).getUserPhones());
                String[] newUserPhones = (String[]) ArrayUtils.addAll(new String[]{managerPhone}, userPhones);
                gateways.get(i).setUserPhones(ArrayHelper.getArrayString(newUserPhones));
            }

            // 获取最近一次警报记录
            DeviceAlarm deviceAlarm = deviceAlarmRepository.findFirstByGlImeiOrderByIdDesc(gateways.get(i).getGlImei());
            if (deviceAlarm != null) {
                deviceAlarms.put(deviceAlarm.getGlImei(), deviceAlarm);
            }
        }

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("gateways", gateways);
        response.put("deviceAlarms", deviceAlarms);
        return response;
    }

}
