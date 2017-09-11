package com.aiyolo.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aiyolo.entity.Device;
import com.aiyolo.entity.DeviceSetting;
import com.aiyolo.repository.DeviceSettingRepository;

@Service
public class DeviceSettingService {

    @Autowired DeviceSettingRepository deviceSettingRepository;

    public DeviceSetting getDeviceSetting(Device device) {
        if (device != null && StringUtils.isNotEmpty(device.getGlImei()) && StringUtils.isNotEmpty(device.getAreaCode())) {
            String areaCode = device.getAreaCode().substring(0, 2) + "0000";
            DeviceSetting deviceSetting = deviceSettingRepository.findFirstByGlImeiOrAreaCodeOrderByIdDesc(device.getGlImei(), areaCode);

            return deviceSetting;
        }

        return null;
    }

}
