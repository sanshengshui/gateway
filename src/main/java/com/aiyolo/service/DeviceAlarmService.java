package com.aiyolo.service;

import com.aiyolo.constant.SingleAlarmTypeEnum;
import com.aiyolo.entity.DeviceAlarm;
import com.aiyolo.repository.DeviceAlarmRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeviceAlarmService {

    @Autowired DeviceAlarmRepository deviceAlarmRepository;

    @Autowired AreaCodeService areaCodeService;
    @Autowired CustomUserDetailsService customUserDetailsService;

    public Page<DeviceAlarm> getPageDeviceAlarmByAreaCode(Pageable pageable, String areaCode) {
        Page<DeviceAlarm> alarms = new PageImpl<DeviceAlarm>(new ArrayList<DeviceAlarm>());

        // 取有权限的区域
        Map<String, List<String>> authorities = customUserDetailsService.getAuthorities();
        if (authorities.get("areas").contains("0") && "0".equals(areaCode)) {
            alarms = deviceAlarmRepository.findAll(pageable);
        } else {
            String areaCodeSearchPat = areaCodeService.getAreaCodeSearchPat(authorities, areaCode);
            if (StringUtils.isNotEmpty(areaCodeSearchPat)) {
                alarms = deviceAlarmRepository.findPageByDeviceAreaCodeMatch(pageable, areaCodeSearchPat);
            }
        }

        return alarms;
    }

    public Map<Integer, String> getAllDeviceAlarmType() {
        return getAllDeviceAlarmType(",");
    }

    public Map<Integer, String> getAllDeviceAlarmType(String separator) {
        Integer maxTypeValue = 0;
        for (SingleAlarmTypeEnum singleType : SingleAlarmTypeEnum.values()) {
            maxTypeValue += singleType.getValue();
        }

        Map<Integer, String> alarmTypeMap = new HashMap<Integer, String>();
        alarmTypeMap.put(0, SingleAlarmTypeEnum.CLEAR.getName());
        for (int i = 1; i <= maxTypeValue; i++) {
            String typeName = getDeviceAlarmTypeName(i, separator);
            if (StringUtils.isNotEmpty(typeName)) {
                alarmTypeMap.put(i, typeName);
            }
        }

        return alarmTypeMap;
    }

    public String[] getDeviceAlarmTypeNameArray(int type) {
        List<String> typeNameList = new ArrayList<String>();

        if (type == 0) {
            typeNameList.add(SingleAlarmTypeEnum.CLEAR.getName());
        } else {
            for (SingleAlarmTypeEnum singleType : SingleAlarmTypeEnum.values()) {
                if ((singleType.getValue() & type) > 0) {
                    typeNameList.add(singleType.getName());
                }
            }
        }

        return typeNameList.toArray(new String[0]);
    }

    public String getDeviceAlarmTypeName(int type) {
        return getDeviceAlarmTypeName(type, ",");
    }

    public String getDeviceAlarmTypeName(int type, String separator) {
        return StringUtils.join(getDeviceAlarmTypeNameArray(type), separator);
    }

}
