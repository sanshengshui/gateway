package com.aiyolo.controller;

import com.aiyolo.common.ArrayHelper;
import com.aiyolo.entity.Device;
import com.aiyolo.repository.DeviceRepository;
import com.aiyolo.service.DeviceService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/device")
public class DeviceController {

    @Autowired DeviceRepository deviceRepository;

    @Autowired DeviceService deviceService;

    @RequestMapping("/view/{id}")
    public String view(@PathVariable String id, Model model) {
        Device device = deviceService.getDeviceById(id);
        if (device != null) {
            model.addAttribute("device", device);
            return "deviceView";
        }

        return "redirect:/404";
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        Device device = deviceService.getDeviceById(id);
        if (device != null) {
            model.addAttribute("device", device);
            return "deviceForm";
        }

        return "redirect:/404";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Device data) {
        Device device = deviceService.getDeviceById(data.getId());
        if (device != null) {
            String[] userPhones = ArrayHelper.getStringArray(data.getUserPhones());
            if (userPhones != null) {
                device.setUserPhones(JSONArray.fromObject(ArrayHelper.removeDuplicateElement(userPhones)).toString());
            } else {
                device.setUserPhones("");
            }

            device.setUserName(data.getUserName());
            device.setAreaCode(data.getAreaCode());
            device.setAddress(data.getAddress());

            deviceRepository.save(device);

            return "redirect:/device";
        }

        return "redirect:/404";
    }

}
