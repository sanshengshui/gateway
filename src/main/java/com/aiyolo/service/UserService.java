package com.aiyolo.service;

import com.aiyolo.constant.RoleEnum;
import com.aiyolo.entity.User;
import com.aiyolo.repository.UserRepository;
import com.aiyolo.data.SimplePageRequest;
import com.aiyolo.vo.UserSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {

    @Autowired UserRepository userRepository;

    @Autowired CustomUserDetailsService customUserDetailsService;

    public Page<User> getPageUser(UserSearchVo userSearchVo) {
        Page<User> users = new PageImpl<User>(new ArrayList<User>());

        if (! RoleEnum.MANAGER.equals(customUserDetailsService.getRole())) {
            // 非管理员，固定只显示本渠道用户
            userSearchVo.setChannelId(customUserDetailsService.getChannelId());
        }

        if (userSearchVo.getChannelId() != null) {
            users = userRepository.findPageByRoleAndChannelIdAndUsernameLikeAndRealnameLikeAndPhoneLike(
                    SimplePageRequest.getPageable(userSearchVo),
                    userSearchVo.getRole(),
                    userSearchVo.getChannelId(),
                    "%" + userSearchVo.getUsername() + "%",
                    "%" + userSearchVo.getRealname() + "%",
                    "%" + userSearchVo.getPhone() + "%");
        } else {
            users = userRepository.findPageByRoleAndUsernameLikeAndRealnameLikeAndPhoneLike(
                    SimplePageRequest.getPageable(userSearchVo),
                    userSearchVo.getRole(),
                    "%" + userSearchVo.getUsername() + "%",
                    "%" + userSearchVo.getRealname() + "%",
                    "%" + userSearchVo.getPhone() + "%");
        }

        return users;
    }

    public User getUserById(String id) {
        Long userId;
        try {
            userId = Long.valueOf(id);
        } catch (NumberFormatException e) {
            return null;
        }

        return getUserById(userId);
    }

    public User getUserById(Long id) {
        if (id == null) {
            return null;
        }

        User user = userRepository.findOne(id);
        if (user != null) {
            RoleEnum role = customUserDetailsService.getRole();
            long channelId = customUserDetailsService.getChannelId();

            if (RoleEnum.EMPLOYEE.equals(user.getRole()) &&
                    (RoleEnum.MANAGER.equals(role) || user.getChannelId().equals(channelId))) {
                return user;
            }
        }

        return null;
    }

}
