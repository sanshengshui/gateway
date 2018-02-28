package com.aiyolo.controller;

import com.aiyolo.common.ArrayHelper;
import com.aiyolo.entity.Gateway;
import com.aiyolo.repository.GatewayRepository;
import com.aiyolo.service.GatewayService;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/gateway")
public class GatewayController {

    @Autowired
    GatewayRepository gatewayRepository;

    @Autowired
    GatewayService gatewayService;

    @RequestMapping("/map/{id}")
    public String map(@PathVariable String id, Model model) {
        return id;
    }

    @RequestMapping("/hotmap/{id}")
    public String hotmap(@PathVariable String id, Model model) {
        Gateway gateway = gatewayService.getGatewayById(id);
        if (gateway != null) {
            model.addAttribute("gateway", gateway);
            return "hotmap";
        }

        return "redirect:/404";
    }

    @RequestMapping("/view/{id}")
    public String view(@PathVariable String id, Model model) {
        Gateway gateway = gatewayService.getGatewayById(id);
        if (gateway != null) {
            model.addAttribute("gateway", gateway);
            return "gatewayView";
        }

        return "redirect:/404";
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        Gateway gateway = gatewayService.getGatewayById(id);
        if (gateway != null) {
            model.addAttribute("gateway", gateway);
            return "gatewayForm";
        }

        return "redirect:/404";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Gateway data) {
        Gateway gateway = gatewayService.getGatewayById(data.getId());
        if (gateway != null) {
            String[] userPhones = ArrayHelper.getStringArray(data.getUserPhones());
            if (userPhones != null) {
                gateway.setUserPhones(JSONArray.fromObject(ArrayHelper.removeDuplicateElement(userPhones)).toString());
            } else {
                gateway.setUserPhones("");
            }

            gateway.setUserName(data.getUserName());
            gateway.setAreaCode(data.getAreaCode());
            gateway.setAddress(data.getAddress());

            gatewayRepository.save(gateway);

            return "redirect:/gateway";
        }

        return "redirect:/404";
    }

}
