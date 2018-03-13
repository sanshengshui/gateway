package com.aiyolo.controller;

import com.aiyolo.constant.AlarmTemperatureConsts;
import com.aiyolo.entity.Gateway;
import com.aiyolo.entity.GatewaySetting;
import com.aiyolo.repository.GatewaySettingRepository;
import com.aiyolo.service.GatewayService;
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
@RequestMapping("/gateway")
public class GatewaySettingController {

    @Autowired GatewaySettingRepository gatewaySettingRepository;

    @Autowired GatewayService gatewayService;

    @RequestMapping("/setting")
    public String setting(Model model) {
        model.addAttribute("gatewaySetting", new GatewaySetting(
                "",
                "",
                AlarmTemperatureConsts.VALUE));

        return "gatewaySetting";
    }

    @RequestMapping("/probe")
    public String probe(Model model) {
        return "probe";
    }

    @RequestMapping(value = "/setting/save", method = RequestMethod.POST)
    public String save(@Valid GatewaySetting gatewaySetting, BindingResult bindingResult, RedirectAttributes redirectAttrs) {
        if (StringUtils.isNotEmpty(gatewaySetting.getGlImei()) && StringUtils.isNotEmpty(gatewaySetting.getAreaCode())) {
            bindingResult.rejectValue("areaCode", "error.gatewaySetting", "区域／网关ID只能选择一项！");
            return "gatewaySetting";
        }

        if (StringUtils.isEmpty(gatewaySetting.getGlImei()) && StringUtils.isEmpty(gatewaySetting.getAreaCode())) {
            bindingResult.rejectValue("areaCode", "error.gatewaySetting", "区域／网关ID必须选择一项！");
            return "gatewaySetting";
        }

        if (bindingResult.hasErrors()) {
            return "gatewaySetting";
        }

        // 权限判断
        if (StringUtils.isNotEmpty(gatewaySetting.getGlImei())) {
            Gateway gateway = gatewayService.getGatewayByGlImei(gatewaySetting.getGlImei());
            if (gateway == null) {
                bindingResult.rejectValue("glImei", "error.gatewaySetting", "未找到网关！");
                return "gatewaySetting";
            }
        }

        GatewaySetting en = gatewaySettingRepository.save(gatewaySetting);
        if (en != null) {
            redirectAttrs.addFlashAttribute("message_success", "设置成功！");
        } else {
            redirectAttrs.addFlashAttribute("message_error", "设置失败！");
        }

        return "redirect:/gateway/setting";
    }

}
