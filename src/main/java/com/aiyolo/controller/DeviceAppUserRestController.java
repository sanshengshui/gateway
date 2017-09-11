package com.aiyolo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aiyolo.entity.AppUserDevice;
import com.aiyolo.entity.Device;
import com.aiyolo.repository.AppUserDeviceRepository;
import com.aiyolo.service.DeviceService;

@RestController
@RequestMapping("/device/user")
public class DeviceAppUserRestController {

    @Autowired AppUserDeviceRepository appUserDeviceRepository;

    @Autowired DeviceService deviceService;

    @RequestMapping(value = "/list/{glImei}", method = RequestMethod.GET)
    public Map<String, Object> list(
            @PathVariable String glImei,
            @RequestParam(value = "draw", defaultValue = "0") Integer draw,
            @RequestParam(value = "start", defaultValue = "0") Integer start,
            @RequestParam(value = "length", defaultValue = "10") Integer length) {
        // 权限判断
        Device device = deviceService.getDeviceByGlImei(glImei);
        if (device == null) {
            return null;
        }

        length = length > 0 ? length : 10;
        Sort sort = new Sort(Direction.DESC, "id");
        Pageable pageable = new PageRequest((start / length), length, sort);
        Page<AppUserDevice> page = appUserDeviceRepository.findPageByGlImei(pageable, glImei);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("draw", draw);
        response.put("recordsTotal", page.getTotalElements());
        response.put("recordsFiltered", page.getTotalElements());
        response.put("data", page.getContent());
        return response;
    }

}
