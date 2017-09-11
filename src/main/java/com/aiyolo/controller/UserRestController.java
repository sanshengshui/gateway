package com.aiyolo.controller;

import com.aiyolo.constant.RoleEnum;
import com.aiyolo.data.SimplePageResponse;
import com.aiyolo.entity.User;
import com.aiyolo.service.UserService;
import com.aiyolo.vo.UserSearchVo;
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
@RequestMapping("/user")
public class UserRestController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map<String, Object> list(@Valid UserSearchVo userSearchVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return null;
        }

        userSearchVo.setRole(RoleEnum.EMPLOYEE);
        Page<User> page = userService.getPageUser(userSearchVo);

        List<User> records = page.getContent();
        for (int i = 0; i < records.size(); i++) {
            records.get(i).setPassword("");
        }

        return SimplePageResponse.data(userSearchVo, page, records.toArray());
    }

}
