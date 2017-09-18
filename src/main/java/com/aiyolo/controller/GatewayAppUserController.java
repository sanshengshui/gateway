package com.aiyolo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aiyolo.constant.Role4GatewayEnum;
import com.aiyolo.entity.AppUserGateway;
import com.aiyolo.entity.Gateway;
import com.aiyolo.repository.AppUserGatewayRepository;
import com.aiyolo.service.GatewayService;

@Controller
@RequestMapping("/gateway/user")
public class GatewayAppUserController {

    @Autowired AppUserGatewayRepository appUserGatewayRepository;

    @Autowired GatewayService gatewayService;

    @RequestMapping("/admin/{id}")
    public String admin(@PathVariable String id) {
        Long appUserGatewayId;
        try {
            appUserGatewayId = Long.valueOf(id);
        } catch (NumberFormatException e) {
            return "redirect:/404";
        }

        AppUserGateway appUserGateway = appUserGatewayRepository.findOne(appUserGatewayId);
        if (appUserGateway != null) {
            // 权限判断
            Gateway gateway = gatewayService.getGatewayByGlImei(appUserGateway.getGlImei());
            if (gateway == null) {
                return "redirect:/404";
            }

            appUserGatewayRepository.updateRoleByGlImei(gateway.getGlImei(), Role4GatewayEnum.USER);
            appUserGateway.setRole(Role4GatewayEnum.MANAGER);
            appUserGatewayRepository.save(appUserGateway);

            return "redirect:/gateway/view/" + gateway.getId();
        }

        return "redirect:/404";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        Long appUserGatewayId;
        try {
            appUserGatewayId = Long.valueOf(id);
        } catch (NumberFormatException e) {
            return "redirect:/404";
        }

        AppUserGateway appUserGateway = appUserGatewayRepository.findOne(appUserGatewayId);
        if (appUserGateway != null && !appUserGateway.getRole().equals(Role4GatewayEnum.MANAGER)) { // 管理员不可被删除
            // 权限判断
            Gateway gateway = gatewayService.getGatewayByGlImei(appUserGateway.getGlImei());
            if (gateway == null) {
                return "redirect:/404";
            }

            appUserGatewayRepository.delete(appUserGateway);

            return "redirect:/gateway/view/" + gateway.getId();
        }

        return "redirect:/404";
    }

}
