package com.aiyolo.controller;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthController {

    @RequestMapping("/login")
    public String login(
            @RequestParam(value = "error", required = false) String error,
            HttpServletRequest request,
            RedirectAttributes redirectAttrs) {
        if (error != null) {
            Object exception = request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (exception != null) {
                if (exception instanceof BadCredentialsException) {
                    redirectAttrs.addFlashAttribute("message_error", "用户名/密码错误！");
                } else if (exception instanceof LockedException) {
                    redirectAttrs.addFlashAttribute("message_error", "该账号已被停用！");
                }

                return "redirect:/login";
            }
        }

        return "login";
    }

}
