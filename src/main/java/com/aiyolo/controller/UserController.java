package com.aiyolo.controller;

import com.aiyolo.constant.RoleEnum;
import com.aiyolo.entity.User;
import com.aiyolo.repository.UserRepository;
import com.aiyolo.service.AreaCodeService;
import com.aiyolo.service.CustomUserDetailsService;
import com.aiyolo.service.UserService;
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
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired UserRepository userRepository;

    @Autowired UserService userService;
    @Autowired AreaCodeService areaCodeService;
    @Autowired CustomUserDetailsService customUserDetailsService;

    @RequestMapping("/new")
    public String add(Model model) {
        User user = new User();
        model.addAttribute("user", user);

        return "userForm";
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        User user = userService.getUserById(id);
        if (user != null) {
            user.setPassword("");
            model.addAttribute("user", user);

            return "userForm";
        }

        return "redirect:/404";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@Valid User user, BindingResult bindingResult, BCryptPasswordEncoder passwordEncoder) {
        if (bindingResult.hasErrors()) {
            return "userForm";
        }

        if (StringUtils.isEmpty(user.getProductIds())) {
            bindingResult.rejectValue("productIds", "error.user", "请选择关联产品！");
            return "userForm";
        }

        if (! RoleEnum.MANAGER.equals(customUserDetailsService.getRole())) {
            // 非管理员，固定只能添加本渠道用户
            user.setChannelId(customUserDetailsService.getChannelId());
        }

        // 取有权限的产品
        List<String> ownProductIds = customUserDetailsService.getAuthorities().get("products");
        String[] userProductIds = user.getProductIds().split(",");
        List<String> newUserProductIds = new ArrayList<String>();
        for (int i = 0; i < userProductIds.length; i++) {
            if (ownProductIds.contains(userProductIds[i])) {
                newUserProductIds.add(userProductIds[i]);
            }
        }
        if (newUserProductIds.size() < 1) {
            return "redirect:/404";
        } else {
            user.setProductIds(String.join(",", newUserProductIds));
        }

        // 取有权限的区域
        if (! StringUtils.isEmpty(user.getDeviceAreaCodes())) {
            List<String> ownAreaCodes = customUserDetailsService.getAuthorities().get("areas");
            String[] userAreaCodes = user.getDeviceAreaCodes().split(",");
            List<String> newUserAreaCodes = new ArrayList<String>();
            for (int i = 0; i < userAreaCodes.length; i++) {
                if (areaCodeService.checkAreaCode(userAreaCodes[i]) &&
                        areaCodeService.areaCodeMatches(userAreaCodes[i], ownAreaCodes)) {
                    newUserAreaCodes.add(userAreaCodes[i]);
                }
            }
            if (newUserAreaCodes.size() > 0) {
                user.setDeviceAreaCodes(String.join(",", newUserAreaCodes));
            } else {
                user.setDeviceAreaCodes("");
            }
        }

        if (user.getId() != null) {
            User userRecord = userService.getUserById(user.getId());
            if (userRecord != null) {
                userRecord.setChannelId(user.getChannelId());
                userRecord.setRealname(user.getRealname());
                userRecord.setPhone(user.getPhone());
                userRecord.setProductIds(user.getProductIds());
                userRecord.setDeviceAreaCodes(user.getDeviceAreaCodes());

                if (! StringUtils.isEmpty(user.getPassword())) {
                    String hashedPassword = passwordEncoder.encode(user.getPassword());
                    userRecord.setPassword(hashedPassword);
                }

                userRepository.save(userRecord);

                return "redirect:/user";
            }
        } else {
            if (userRepository.findByUsername(user.getUsername()) != null) {
                bindingResult.rejectValue("username", "error.user", "用户名已存在！");
                return "userForm";
            }

            String hashedPassword = passwordEncoder.encode(user.getPassword());
            userRepository.save(new User(
                    user.getChannelId(),
                    user.getUsername(),
                    user.getRealname(),
                    user.getPhone(),
                    hashedPassword,
                    RoleEnum.EMPLOYEE,
                    user.getProductIds(),
                    user.getDeviceAreaCodes()));

            return "redirect:/user";
        }

        return "redirect:/404";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        User user = userService.getUserById(id);
        if (user != null) {
            userRepository.delete(user);

            return "redirect:/user";
        }

        return "redirect:/404";
    }

}
