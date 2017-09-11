package com.aiyolo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aiyolo.constant.Role4DeviceEnum;
import com.aiyolo.entity.AppUserDevice;
import com.aiyolo.entity.Device;
import com.aiyolo.repository.AppUserDeviceRepository;
import com.aiyolo.service.DeviceService;

@Controller
@RequestMapping("/device/user")
public class DeviceAppUserController {

    @Autowired AppUserDeviceRepository appUserDeviceRepository;

    @Autowired DeviceService deviceService;

    @RequestMapping("/admin/{id}")
    public String admin(@PathVariable String id) {
        Long appUserDeviceId;
        try {
            appUserDeviceId = Long.valueOf(id);
        } catch (NumberFormatException e) {
            return "redirect:/404";
        }

        AppUserDevice appUserDevice = appUserDeviceRepository.findOne(appUserDeviceId);
        if (appUserDevice != null) {
            // 权限判断
            Device device = deviceService.getDeviceByGlImei(appUserDevice.getGlImei());
            if (device == null) {
                return "redirect:/404";
            }

            appUserDeviceRepository.updateRoleByGlImei(device.getGlImei(), Role4DeviceEnum.USER);
            appUserDevice.setRole(Role4DeviceEnum.MANAGER);
            appUserDeviceRepository.save(appUserDevice);

            return "redirect:/device/view/" + device.getId();
        }

        return "redirect:/404";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        Long appUserDeviceId;
        try {
            appUserDeviceId = Long.valueOf(id);
        } catch (NumberFormatException e) {
            return "redirect:/404";
        }

        AppUserDevice appUserDevice = appUserDeviceRepository.findOne(appUserDeviceId);
        if (appUserDevice != null && !appUserDevice.getRole().equals(Role4DeviceEnum.MANAGER)) { // 管理员不可被删除
            // 权限判断
            Device device = deviceService.getDeviceByGlImei(appUserDevice.getGlImei());
            if (device == null) {
                return "redirect:/404";
            }

            appUserDeviceRepository.delete(appUserDevice);

            return "redirect:/device/view/" + device.getId();
        }

        return "redirect:/404";
    }

}
