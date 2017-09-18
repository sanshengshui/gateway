package com.aiyolo.controller;

import com.aiyolo.entity.AppUserGateway;
import com.aiyolo.entity.Gateway;
import com.aiyolo.repository.AppUserGatewayRepository;
import com.aiyolo.service.GatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/gateway/user")
public class GatewayAppUserRestController {

    @Autowired AppUserGatewayRepository appUserGatewayRepository;

    @Autowired GatewayService gatewayService;

    @RequestMapping(value = "/list/{glImei}", method = RequestMethod.GET)
    public Map<String, Object> list(
            @PathVariable String glImei,
            @RequestParam(value = "draw", defaultValue = "0") Integer draw,
            @RequestParam(value = "start", defaultValue = "0") Integer start,
            @RequestParam(value = "length", defaultValue = "10") Integer length) {
        // 权限判断
        Gateway gateway = gatewayService.getGatewayByGlImei(glImei);
        if (gateway == null) {
            return null;
        }

        length = length > 0 ? length : 10;
        Sort sort = new Sort(Direction.DESC, "id");
        Pageable pageable = new PageRequest((start / length), length, sort);
        Page<AppUserGateway> page = appUserGatewayRepository.findPageByGlImei(pageable, glImei);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("draw", draw);
        response.put("recordsTotal", page.getTotalElements());
        response.put("recordsFiltered", page.getTotalElements());
        response.put("data", page.getContent());
        return response;
    }

}
