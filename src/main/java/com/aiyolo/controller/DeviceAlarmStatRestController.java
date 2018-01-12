package com.aiyolo.controller;

import com.aiyolo.entity.DeviceAlarmStat;
import com.aiyolo.repository.DeviceAlarmStatRepository;
import com.aiyolo.service.AreaCodeService;
import com.aiyolo.service.CustomUserDetailsService;
import com.aiyolo.vo.DeviceAlarmSearchVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/alarm/stat")
public class DeviceAlarmStatRestController {

    @Autowired
    DeviceAlarmStatRepository deviceAlarmStatRepository;

    @Autowired
    AreaCodeService areaCodeService;
    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public Map<String, Object> data(@Valid DeviceAlarmSearchVo deviceAlarmSearchVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return null;
        }

        if (!areaCodeService.checkAreaCode(deviceAlarmSearchVo.getAreaCode(), new String[]{"0"})) {
            return null;
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String date = format.format(today);

        Map<String, List<String>> authorities = customUserDetailsService.getAuthorities();
        String areaCodeSearchPat = areaCodeService.getAreaCodeSearchPat(authorities, deviceAlarmSearchVo.getAreaCode());

        String village = StringUtils.trim(deviceAlarmSearchVo.getVillage());

        if (StringUtils.isNotEmpty(areaCodeSearchPat)) {
            // 当日预警&报警
            List<DeviceAlarmStat> todayWarning = deviceAlarmStatRepository.findTotalByDateAndAreaCodeAndVillage(
                    date,
                    areaCodeSearchPat,
                    "%" + village + "%");

            // 年汇总
            List<DeviceAlarmStat> yearWarning = deviceAlarmStatRepository.findTotalByYearAndAreaCodeAndVillage(
                    "^" + date.substring(0, 4),
                    areaCodeSearchPat,
                    "%" + village + "%");

            Map<String, Object> response = new HashMap<String, Object>();
            response.put("today", todayWarning);
            response.put("year", yearWarning);
            return response;
        }

        return null;
    }

}
