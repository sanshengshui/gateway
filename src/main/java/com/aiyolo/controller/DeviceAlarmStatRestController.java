package com.aiyolo.controller;

import com.aiyolo.entity.DeviceAlarmStat;
import com.aiyolo.entity.Gateway;
import com.aiyolo.entity.GatewayStatus;
import com.aiyolo.repository.DeviceAlarmNowRepository;
import com.aiyolo.repository.DeviceAlarmStatRepository;
import com.aiyolo.repository.GatewayStatusRepository;
import com.aiyolo.service.AreaCodeService;
import com.aiyolo.service.CustomUserDetailsService;
import com.aiyolo.service.GatewayService;
import com.aiyolo.vo.DeviceAlarmSearchVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/alarm/stat")
public class DeviceAlarmStatRestController {

    @Autowired
    GatewayStatusRepository gatewayStatusRepository;
    @Autowired
    DeviceAlarmStatRepository deviceAlarmStatRepository;
    @Autowired
    DeviceAlarmNowRepository deviceAlarmNowRepository;

    @Autowired
    GatewayService gatewayService;
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

        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String date = format.format(today);

        Map<String, List<String>> authorities = customUserDetailsService.getAuthorities();
        String areaCodeSearchPat = areaCodeService.getAreaCodeSearchPat(authorities, deviceAlarmSearchVo.getAreaCode());

        if (StringUtils.isNotEmpty(areaCodeSearchPat)) {
            // 当日预警&报警
            List<DeviceAlarmStat> todayWarning = deviceAlarmStatRepository.findTotalByDateAndAreaCode(
                    date,
                    areaCodeSearchPat);

            // 预警&报警年汇总
            List<DeviceAlarmStat> yearWarning = deviceAlarmStatRepository.findTotalByYearAndAreaCode(
                    "^" + date.substring(0, 4),
                    areaCodeSearchPat);

            // 正在预警/报警设备汇总
            List<Object> warningGateways = deviceAlarmNowRepository.findTotalByAreaCode(areaCodeSearchPat);

            // 正常/故障设备汇总
            List<Gateway> gateways = gatewayService.getAllGatewayByAreaCode(
                    deviceAlarmSearchVo.getAreaCode());
            Map<String, Integer> _count = new HashMap<String, Integer>();
            for (int i = 0; i < gateways.size(); i++) {
                if (StringUtils.isNotEmpty(gateways.get(i).getAreaCode())) {
                    GatewayStatus gatewayStatus = gatewayStatusRepository.findFirstByGlImeiOrderByIdDesc(gateways.get(i).getGlImei());
                    if (gatewayStatus != null) {
                        String _key = new StringBuilder()
                                .append(gateways.get(i).getAreaCode())
                                .append(gatewayStatus.getStatus() == 0 ? "Y" : "N").toString();
                        _count.put(_key, _count.get(_key) != null ? (_count.get(_key) + 1) : 1);
                    }
                }
            }
            List<Object> statusGateways = new ArrayList<Object>();
            for (Map.Entry<String, Integer> entry : _count.entrySet()) {
                Object[] _item = { entry.getKey().substring(0, 6), entry.getKey().substring(6), entry.getValue() };
                statusGateways.add(_item);
            }

            Map<String, Object> response = new HashMap<String, Object>();
            response.put("today", todayWarning);
            response.put("year", yearWarning);
            response.put("warningGateways", warningGateways);
            response.put("statusGateways", statusGateways);
            return response;
        }

        return null;
    }

}
