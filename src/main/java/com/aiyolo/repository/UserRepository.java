package com.aiyolo.repository;

import com.aiyolo.constant.RoleEnum;
import com.aiyolo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findByUsername(String username);

    User findFirstByChannelIdAndRoleOrderByIdDesc(Long channelId, RoleEnum role);

    List<User> findByChannelId(int channelId);

    Page<User> findPageByChannelId(Pageable pageable, int channelId);

    Page<User> findPageByRoleAndUsernameLikeAndRealnameLikeAndPhoneLike(
            Pageable pageable,
            RoleEnum role,
            String username,
            String realname,
            String phone);

    Page<User> findPageByRoleAndChannelIdAndUsernameLikeAndRealnameLikeAndPhoneLike(
            Pageable pageable,
            RoleEnum role,
            long channelId,
            String username,
            String realname,
            String phone);

    Page<User> findPageByRoleAndChannelNameLikeAndUsernameLikeAndPhoneLike(
            Pageable pageable,
            RoleEnum role,
            String channelName,
            String username,
            String phone);

}
