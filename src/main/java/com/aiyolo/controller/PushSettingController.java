package com.aiyolo.controller;

import com.aiyolo.entity.PushSetting;
import com.aiyolo.repository.PushSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class PushSettingController {

    @Autowired PushSettingRepository pushSettingRepository;

    @RequestMapping("/push_setting/{type}")
    public String setting(@PathVariable String type, Model model) {
        List<PushSetting> pushSetting = pushSettingRepository.findByType(type);
        if (pushSetting != null && pushSetting.size() > 0) {
            for (int i = 0; i < pushSetting.size(); i++) {
                model.addAttribute("pushSetting" + pushSetting.get(i).getLevel(), pushSetting.get(i));
            }

            return "pushSetting";
        }

        return "redirect:/404";
    }

    @RequestMapping(value = "/push_setting/save", method = RequestMethod.POST)
    public String save(PushSetting data, RedirectAttributes redirectAttrs) {
        PushSetting pushSetting = pushSettingRepository.findOne(data.getId());
        if (pushSetting != null) {
            pushSetting.setTitle(data.getTitle());
            pushSetting.setContent(data.getContent());

            PushSetting en = pushSettingRepository.save(pushSetting);
            if (en != null) {
                redirectAttrs.addFlashAttribute("message_success", "设置成功！");
            } else {
                redirectAttrs.addFlashAttribute("message_error", "设置失败！");
            }

            return "redirect:/push_setting/" + pushSetting.getType() + "#" + pushSetting.getLevel();
        }

        return "redirect:/404";
    }

}
