package com.aiyolo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aiyolo.entity.User;
import com.aiyolo.repository.UserRepository;

@Controller
@RequestMapping("/profile")
public class UserProfileController {

    @Autowired UserRepository userRepository;

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public String changePassword(
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            Authentication authentication,
            RedirectAttributes redirectAttrs) {
        if (oldPassword != null && newPassword != null) {
            User user = userRepository.findByUsername(authentication.getName());
            if (user != null) {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                    String hashedPassword = passwordEncoder.encode(newPassword);
                    user.setPassword(hashedPassword);

                    userRepository.save(user);

                    redirectAttrs.addFlashAttribute("message_success", "密码修改成功！");
                } else {
                    redirectAttrs.addFlashAttribute("message_error", "原密码错误！");
                }

                return "redirect:/profile";
            }
        }

        return "redirect:/404";
    }

}
