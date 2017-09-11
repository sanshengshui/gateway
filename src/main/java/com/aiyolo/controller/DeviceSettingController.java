package com.aiyolo.controller;

import com.aiyolo.constant.AlarmTemperatureConsts;
import com.aiyolo.entity.Device;
import com.aiyolo.entity.DeviceSetting;
import com.aiyolo.repository.DeviceSettingRepository;
import com.aiyolo.service.DeviceService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/device")
public class DeviceSettingController {

    @Autowired DeviceSettingRepository deviceSettingRepository;

    @Autowired DeviceService deviceService;

    @RequestMapping("/setting")
    public String setting(Model model) {
        model.addAttribute("deviceSetting", new DeviceSetting(
                "",
                "",
                AlarmTemperatureConsts.ONE_LEVEL,
                AlarmTemperatureConsts.TWO_LEVEL));

        return "deviceSetting";
    }

    @RequestMapping(value = "/setting/save", method = RequestMethod.POST)
    public String save(@Valid DeviceSetting deviceSetting, BindingResult bindingResult, RedirectAttributes redirectAttrs) {
        if (StringUtils.isNotEmpty(deviceSetting.getGlImei()) && StringUtils.isNotEmpty(deviceSetting.getAreaCode())) {
            bindingResult.rejectValue("areaCode", "error.deviceSetting", "区域／设备ID只能选择一项！");
            return "deviceSetting";
        }

        if (StringUtils.isEmpty(deviceSetting.getGlImei()) && StringUtils.isEmpty(deviceSetting.getAreaCode())) {
            bindingResult.rejectValue("areaCode", "error.deviceSetting", "区域／设备ID必须选择一项！");
            return "deviceSetting";
        }

        if (bindingResult.hasErrors()) {
            return "deviceSetting";
        }

        if (deviceSetting.getOneLevelAlarmTemperature() > deviceSetting.getTwoLevelAlarmTemperature()) {
            bindingResult.rejectValue("twoLevelAlarmTemperature", "error.deviceSetting", "二级预警温度值应大于一级预警温度值！");
            return "deviceSetting";
        }

        // 权限判断
        if (StringUtils.isNotEmpty(deviceSetting.getGlImei())) {
            Device device = deviceService.getDeviceByGlImei(deviceSetting.getGlImei());
            if (device == null) {
                bindingResult.rejectValue("glImei", "error.deviceSetting", "未找到设备！");
                return "deviceSetting";
            }
        }

        DeviceSetting en = deviceSettingRepository.save(deviceSetting);
        if (en != null) {
            redirectAttrs.addFlashAttribute("message_success", "设置成功！");
        } else {
            redirectAttrs.addFlashAttribute("message_error", "设置失败！");
        }

        return "redirect:/device/setting";
    }

}
