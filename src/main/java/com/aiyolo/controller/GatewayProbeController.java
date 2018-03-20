package com.aiyolo.controller;

import com.aiyolo.channel.data.request.GatewayMaccfgRequest;
import com.aiyolo.entity.Gateway;
import com.aiyolo.queue.Sender;
import com.aiyolo.repository.GatewayRepository;
import com.aiyolo.service.GatewayService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
@RequestMapping("/gateway")
public class GatewayProbeController {
    @Autowired
    GatewayRepository gatewayRepository;

    @Autowired
    GatewayService gatewayService;

    @Autowired
    Sender sender;


    @RequestMapping("/probe")
    public String probe(Model model) {
        model.addAttribute("gatewayProbe");
        return "gatewayProbe";
    }

    @RequestMapping("/probe/edit/{id}")
    public String probeSetting(@PathVariable String id, Model model) {
        Gateway gateway = gatewayService.getGatewayById(id);
        if (gateway != null) {
            model.addAttribute("gateway", gateway);
            return "probeSetting";
        }

        return "redirect:/404";
    }

    @RequestMapping(value = "/probe/save", method = RequestMethod.POST)
    public String save(Gateway data) {
        Gateway gateway = gatewayService.getGatewayById(data.getId());
        if (gateway != null) {
            gateway.setReport_url(data.getReport_url());
            Integer report_interval = data.getReport_interval();
            if (report_interval <= 0) {
                gateway.setProbe_status(0);
                gateway.setReport_interval(0);
                gatewayRepository.save(gateway);
            } else {
                if (report_interval > 540) {
                    report_interval = 540;
                }
                gateway.setReport_interval(report_interval);
                gateway.setProbe_status(data.getProbe_status());
                gatewayRepository.save(gateway);
            }
            //-------------------------增加下发mac配置---------------------------------
            Map<String, Object> headerMap = GatewayMaccfgRequest.getInstance().requestHeader(gateway.getGlImei());
            Map<String, Object> bodyMap = GatewayMaccfgRequest.getInstance().requestBody(gateway);
            sender.sendMessage(headerMap, bodyMap);
            //-------------------------增加下发mac配置---------------------------------
            return "redirect:/gateway";
        }

        return "redirect:/404";
    }
}
