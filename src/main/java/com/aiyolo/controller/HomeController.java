package com.aiyolo.controller;

import com.aiyolo.cache.GatewayLiveStatusCache;
import com.aiyolo.common.ArrayHelper;
import com.aiyolo.common.BaiduMapHelper;
import com.aiyolo.constant.DeviceOnlineStatusConsts;
import com.aiyolo.entity.DeviceAlarmNow;
import com.aiyolo.entity.Gateway;
import com.aiyolo.repository.DeviceAlarmNowRepository;
import com.aiyolo.repository.GatewayRepository;
import com.aiyolo.service.AreaCodeService;
import com.aiyolo.service.GatewayService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    GatewayLiveStatusCache gatewayLiveStatusCache;

    @Autowired
    GatewayRepository gatewayRepository;
    @Autowired
    DeviceAlarmNowRepository deviceAlarmNowRepository;


    @Autowired
    GatewayService gatewayService;
    @Autowired
    AreaCodeService areaCodeService;

    @RequestMapping("/mapData")
    public List<Map> data(
            @RequestParam(value="center", defaultValue="0") String center,
            @RequestParam(value="zoom", defaultValue="0") String zoom) {
        String[] mapCenter = center.split(",", 2);
        if (mapCenter.length < 2) {
            return null;
        }

        int mapZoom;
        try {
            mapZoom = Integer.valueOf(zoom);
        } catch (NumberFormatException e) {
            return null;
        }

        Map<String, String> locationAddress = BaiduMapHelper.getAddressByLocation(mapCenter[0], mapCenter[1]);
        String areaCode = locationAddress.get("areaCode");
        if (!areaCodeService.checkAreaCode(areaCode)) {
            return null;
        }

        List<Map> data = new ArrayList<Map>();
        if (mapZoom < 15) {
            if (mapZoom < 8) {
                areaCode = "0";
            } else if (mapZoom < 12) {
                areaCode = areaCode.substring(0, 2) + "0000";
            } else {
                areaCode = areaCode.substring(0, 4) + "00";
            }
            List<Gateway> gateways = gatewayService.getAllGatewayByAreaCode(areaCode);

            Map<String, Integer> items = new HashMap<String, Integer>();
            for (int i = 0; i < gateways.size(); i++) {
                Gateway gateway = gateways.get(i);
                int gatewayLiveStatus = gatewayLiveStatusCache.getByGlImei(gateway.getGlImei());
                if (gatewayLiveStatus == DeviceOnlineStatusConsts.OFFLINE) {
                    continue;
                }
                if (StringUtils.isNotEmpty(gateway.getAreaCode())) {
                    String key;
                    if (mapZoom < 8) {
                        key = gateway.getAreaCode().substring(0, 2) + "0000";
                    } else if (mapZoom < 12) {
                        key = gateway.getAreaCode().substring(0, 4) + "00";
                    } else {
                        key = gateway.getAreaCode();
                    }

                    if (items.get(key) == null) {
                        items.put(key, 1);
                    } else {
                        items.put(key, items.get(key) + 1);
                    }
                }
            }
            for (Map.Entry<String, Integer> entry : items.entrySet()) {
                Map<String, Object> item = new HashMap<String, Object>();
                String[] areaNameArray = areaCodeService.getAreaNameArray(entry.getKey());
                if (mapZoom < 8) {
                    item.put("position", Arrays.copyOfRange(areaNameArray, 0, 1));
                } else if (mapZoom < 12) {
                    item.put("position", Arrays.copyOfRange(areaNameArray, 0, 2));
                } else {
                    item.put("position", areaNameArray);
                }
                item.put("onlineDev", entry.getValue());

                data.add(item);
            }
        } else {
            List<Gateway> gateways = gatewayService.getAllGatewayByAreaCode(areaCode);
            for (int i = 0; i < gateways.size(); i++) {
                Gateway gateway = gateways.get(i);
                if (StringUtils.isNotEmpty(gateway.getAreaCode())) {
                    String areaName = areaCodeService.getAreaName(gateway.getAreaCode());
                    if (StringUtils.isNotEmpty(areaName) && StringUtils.isEmpty(gateway.getAddressLocation())) {
                        String fullAddress = areaName + gateway.getAddress();
                        Map<String, String> addressLocation = BaiduMapHelper.getLocationByAddress(fullAddress);
                        if (addressLocation != null) {
                            gateway.setAddressLocation(addressLocation.get("longitude") + "," + addressLocation.get("latitude"));
                            gatewayRepository.save(gateway);
                        }
                    }
                }

                // 添加网关管理员手机至userPhones
                String managerPhone = gatewayService.getManagerPhone(gateway.getGlImei());
                if (StringUtils.isNotEmpty(managerPhone)) {
                    String[] userPhones = ArrayHelper.getStringArray(gateway.getUserPhones());
                    String[] newUserPhones = (String[]) ArrayUtils.addAll(new String[]{managerPhone}, userPhones);
                    gateway.setUserPhones(ArrayHelper.getArrayString(newUserPhones));
                }

                Map<String, Object> item = new HashMap<String, Object>();
                item.put("addressLocation", gateway.getAddressLocation());
                item.put("areaCode", gateway.getAreaCode());
                item.put("address", gateway.getAddress());
                item.put("userName", gateway.getUserName());
                item.put("userPhones", gateway.getUserPhones());

                DeviceAlarmNow deviceAlarmNow = deviceAlarmNowRepository.findOneByGlImeiAndType(gateway.getGlImei(), "W");
                if (deviceAlarmNow == null) {
                    int gatewayLiveStatus = gatewayLiveStatusCache.getByGlImei(gateway.getGlImei());
                    item.put("status", gatewayLiveStatus == DeviceOnlineStatusConsts.ONLINE ? "online" : "offline");
                } else {
                    item.put("status", "warning");
                }

                data.add(item);
            }
        }

        return data;
    }

}
