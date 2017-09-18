package com.aiyolo.controller;

import com.aiyolo.constant.RoleEnum;
import com.aiyolo.entity.Channel;
import com.aiyolo.entity.User;
import com.aiyolo.repository.ChannelRepository;
import com.aiyolo.repository.UserRepository;
import com.aiyolo.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping("/channel")
public class ChannelController {

    @Autowired UserRepository userRepository;
    @Autowired ChannelRepository channelRepository;

    @Autowired ChannelService channelService;

    @RequestMapping("/new")
    public String add(Model model) {
        User user = new User();
        model.addAttribute("user", user);

        return "channelForm";
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        User user = channelService.getChannelUserById(id);
        if (user != null) {
            user.setPassword("");
            model.addAttribute("user", user);

            return "channelForm";
        }

        return "redirect:/404";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@Valid User user, BindingResult bindingResult, BCryptPasswordEncoder passwordEncoder) {
        if (bindingResult.hasErrors()) {
            return "channelForm";
        }

        if (StringUtils.isEmpty(user.getProductIds())) {
            bindingResult.rejectValue("productIds", "error.user", "请选择关联产品！");
            return "channelForm";
        }

        if (user.getId() != null) {
            User userRecord = channelService.getChannelUserById(user.getId());
            if (userRecord != null) {
                Channel channel = channelRepository.findOneByName(user.getChannel().getName());
                if (channel != null && !channel.getId().equals(userRecord.getChannelId())) {
                    bindingResult.rejectValue("channel.name", "error.user", "渠道名称已存在！");
                    return "channelForm";
                }

                userRecord.getChannel().setName(user.getChannel().getName());
                userRecord.setRealname(user.getRealname());
                userRecord.setPhone(user.getPhone());
                userRecord.setProductIds(user.getProductIds());
                userRecord.setGatewayAreaCodes(user.getGatewayAreaCodes());

                if (! StringUtils.isEmpty(user.getPassword())) {
                    String hashedPassword = passwordEncoder.encode(user.getPassword());
                    userRecord.setPassword(hashedPassword);
                }

                userRepository.save(userRecord);

                return "redirect:/channel";
            }
        } else {
            if (channelRepository.findOneByName(user.getChannel().getName()) != null) {
                bindingResult.rejectValue("channel.name", "error.user", "渠道名称已存在！");
                return "channelForm";
            }

            if (userRepository.findByUsername(user.getUsername()) != null) {
                bindingResult.rejectValue("username", "error.user", "用户名已存在！");
                return "channelForm";
            }

            Channel channel = channelRepository.save(new Channel(user.getChannel().getName()));

            String hashedPassword = passwordEncoder.encode(user.getPassword());
            userRepository.save(new User(
                    channel.getId(),
                    user.getUsername(),
                    user.getRealname(),
                    user.getPhone(),
                    hashedPassword,
                    RoleEnum.AGENT,
                    user.getProductIds(),
                    user.getGatewayAreaCodes()));

            return "redirect:/channel";
        }

        return "redirect:/404";
    }

    @RequestMapping("/switch/{id}")
    public String status(@PathVariable String id) {
        User user = channelService.getChannelUserById(id);
        if (user != null) {
            user.setStatus(user.getStatus() == 0 ? 1 : 0);

            userRepository.save(user);

            return "redirect:/channel";
        }

        return "redirect:/404";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        User user = channelService.getChannelUserById(id);
        if (user != null) {
            userRepository.delete(user);

            return "redirect:/channel";
        }

        return "redirect:/404";
    }

}
