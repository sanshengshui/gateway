package com.aiyolo.controller;

import com.aiyolo.constant.RoleEnum;
import com.aiyolo.data.SimplePageResponse;
import com.aiyolo.entity.User;
import com.aiyolo.service.ChannelService;
import com.aiyolo.vo.ChannelSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/channel")
public class ChannelRestController {

    @Autowired
    ChannelService channelService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map<String, Object> list(@Valid ChannelSearchVo channelSearchVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return null;
        }

        channelSearchVo.setRole(RoleEnum.AGENT);
        Page<User> page = channelService.getPageChannelUser(channelSearchVo);

        List<User> records = page.getContent();
        for (int i = 0; i < records.size(); i++) {
            records.get(i).setPassword("");
        }

        return SimplePageResponse.data(channelSearchVo, page, records.toArray());
    }

}
