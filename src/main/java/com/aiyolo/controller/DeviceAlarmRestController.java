package com.aiyolo.controller;

import com.aiyolo.entity.Device;
import com.aiyolo.entity.DeviceAlarm;
import com.aiyolo.repository.DeviceAlarmRepository;
import com.aiyolo.service.AreaCodeService;
import com.aiyolo.service.DeviceAlarmService;
import com.aiyolo.service.DeviceService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/alarm")
public class DeviceAlarmRestController {

    @Autowired DeviceAlarmRepository deviceAlarmRepository;

    @Autowired DeviceService deviceService;
    @Autowired DeviceAlarmService deviceAlarmService;
    @Autowired AreaCodeService areaCodeService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map<String, Object> list(
            @RequestParam(value = "draw", defaultValue = "0") Integer draw,
            @RequestParam(value = "start", defaultValue = "0") Integer start,
            @RequestParam(value = "length", defaultValue = "20") Integer length,
            @RequestParam(value = "areaCode", defaultValue = "0") String areaCode,
            @RequestParam(value = "glImei", defaultValue = "") String glImei) {
        if (! areaCodeService.checkAreaCode(areaCode, new String[] {"0"})) {
            return null;
        }

        length = length > 0 ? length : 20;
        Sort sort = new Sort(Direction.DESC, "timestamp");
        Pageable pageable = new PageRequest((start / length), length, sort);

        Page<DeviceAlarm> page = new PageImpl<DeviceAlarm>(new ArrayList<DeviceAlarm>());
        if (StringUtils.isNotEmpty(glImei)) {
            // 权限判断
            Device device = deviceService.getDeviceByGlImei(glImei);
            if (device == null) {
                return null;
            }

            page = deviceAlarmRepository.findPageByGlImei(pageable, glImei);
        } else {
            page = deviceAlarmService.getPageDeviceAlarmByAreaCode(pageable, areaCode);
        }

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("draw", draw);
        response.put("recordsTotal", page.getTotalElements());
        response.put("recordsFiltered", page.getTotalElements());
        response.put("data", page.getContent());
        return response;
    }

}
