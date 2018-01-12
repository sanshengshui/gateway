package com.aiyolo.controller;

import com.aiyolo.data.SimplePageResponse;
import com.aiyolo.entity.DeviceAlarm;
import com.aiyolo.service.AreaCodeService;
import com.aiyolo.service.DeviceAlarmService;
import com.aiyolo.vo.DeviceAlarmSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/alarm")
public class DeviceAlarmRestController {

    @Autowired
    DeviceAlarmService deviceAlarmService;
    @Autowired
    AreaCodeService areaCodeService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map<String, Object> list(@Valid DeviceAlarmSearchVo deviceAlarmSearchVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return null;
        }

        if (!areaCodeService.checkAreaCode(deviceAlarmSearchVo.getAreaCode(), new String[] {"0"})) {
            return null;
        }

        Page<DeviceAlarm> page = deviceAlarmService.getPageDeviceAlarm(deviceAlarmSearchVo);
        List<DeviceAlarm> records = page.getContent();
        return SimplePageResponse.data(deviceAlarmSearchVo, page, records.toArray());
    }

}
