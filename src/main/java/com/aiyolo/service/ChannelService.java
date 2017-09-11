package com.aiyolo.service;

import com.aiyolo.constant.RoleEnum;
import com.aiyolo.data.SimplePageRequest;
import com.aiyolo.entity.User;
import com.aiyolo.repository.UserRepository;
import com.aiyolo.vo.ChannelSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ChannelService {

    @Autowired UserRepository userRepository;

    public Page<User> getPageChannelUser(ChannelSearchVo channelSearchVo) {
        Page<User> users = new PageImpl<User>(new ArrayList<User>());

        users = userRepository.findPageByRoleAndChannelNameLikeAndUsernameLikeAndPhoneLike(
                SimplePageRequest.getPageable(channelSearchVo),
                channelSearchVo.getRole(),
                "%" + channelSearchVo.getChannelName() + "%",
                "%" + channelSearchVo.getUsername() + "%",
                "%" + channelSearchVo.getPhone() + "%");

        return users;
    }

    public User getChannelUserById(String id) {
        Long userId;
        try {
            userId = Long.valueOf(id);
        } catch (NumberFormatException e) {
            return null;
        }

        return getChannelUserById(userId);
    }

    public User getChannelUserById(Long id) {
        if (id == null) {
            return null;
        }

        User user = userRepository.findOne(id);
        if (user != null) {
            if (user.getRole().equals(RoleEnum.AGENT)) {
                return user;
            }
        }

        return null;
    }

}
