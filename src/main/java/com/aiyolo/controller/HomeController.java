package com.aiyolo.controller;

import com.aiyolo.common.ArrayHelper;
import com.aiyolo.common.BaiduMapHelper;
import com.aiyolo.entity.Device;
import com.aiyolo.entity.DeviceAlarm;
import com.aiyolo.repository.DeviceAlarmRepository;
import com.aiyolo.repository.DeviceRepository;
import com.aiyolo.service.AreaCodeService;
import com.aiyolo.service.DeviceService;
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

    @Autowired DeviceRepository deviceRepository;
    @Autowired DeviceAlarmRepository deviceAlarmRepository;

    @Autowired DeviceService deviceService;
    @Autowired AreaCodeService areaCodeService;

    @RequestMapping("/data")
    public Map<String, Object> data(
            @RequestParam(value="id", defaultValue="0") String id,
            @RequestParam(value="areaCode", defaultValue="0") String areaCode) {
        Long deviceId;
        try {
            deviceId = Long.valueOf(id);
        } catch (NumberFormatException e) {
            return null;
        }

        if (! areaCodeService.checkAreaCode(areaCode, new String[] {"0"})) {
            return null;
        }

        List<Device> devices = new ArrayList<Device>();
        if (deviceId == 0) {
            devices = deviceService.getAllDeviceByAreaCode(areaCode);
        } else {
            Device device = deviceService.getDeviceById(deviceId);
            if (device != null) {
                devices.add(device);
            }
        }

        Map<String, DeviceAlarm> deviceAlarms = new HashMap<String, DeviceAlarm>();
        for (int i = 0; i < devices.size(); i++) {
            if (StringUtils.isNotEmpty(devices.get(i).getAreaCode())) {
                String areaName = areaCodeService.getAreaName(devices.get(i).getAreaCode());
                if (StringUtils.isNotEmpty(areaName) && StringUtils.isEmpty(devices.get(i).getAddressLocation())) {
                    String fullAddress = areaName + devices.get(i).getAddress();
                    Map<String, String> addressLocation = BaiduMapHelper.getLocationByAddress(fullAddress);
                    if (addressLocation != null) {
                        devices.get(i).setAddressLocation(addressLocation.get("longitude") + "," + addressLocation.get("latitude"));
                        deviceRepository.save(devices.get(i));
                    }
                }
            }

            // 添加设备管理员手机至userPhones
            String managerPhone = deviceService.getManagerPhone(devices.get(i).getGlImei());
            if (StringUtils.isNotEmpty(managerPhone)) {
                String[] userPhones = ArrayHelper.getStringArray(devices.get(i).getUserPhones());
                String[] newUserPhones = (String[]) ArrayUtils.addAll(new String[]{managerPhone}, userPhones);
                devices.get(i).setUserPhones(ArrayHelper.getArrayString(newUserPhones));
            }

            // 获取最近一次警报记录
            DeviceAlarm deviceAlarm = deviceAlarmRepository.findFirstByGlIdOrderByIdDesc(devices.get(i).getGlId());
            if (deviceAlarm != null) {
                deviceAlarms.put(deviceAlarm.getGlId(), deviceAlarm);
            }
        }

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("devices", devices);
        response.put("deviceAlarms", deviceAlarms);
        return response;
    }

}
