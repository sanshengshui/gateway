package com.aiyolo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aiyolo.entity.DeviceSetting;
import com.aiyolo.repository.DeviceSettingRepository;

@RestController
@RequestMapping("/device/setting")
public class DeviceSettingRestController {

    @Autowired DeviceSettingRepository deviceSettingRepository;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map<String, Object> list(
            @RequestParam(value = "draw", defaultValue = "0") Integer draw,
            @RequestParam(value = "start", defaultValue = "0") Integer start,
            @RequestParam(value = "length", defaultValue = "10") Integer length) {
        length = length > 0 ? length : 10;
        Sort sort = new Sort(Direction.DESC, "id");
        Pageable pageable = new PageRequest((start / length), length, sort);
        Page<DeviceSetting> page = deviceSettingRepository.findAll(pageable);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("draw", draw);
        response.put("recordsTotal", page.getTotalElements());
        response.put("recordsFiltered", page.getTotalElements());
        response.put("data", page.getContent());
        return response;
    }

}
