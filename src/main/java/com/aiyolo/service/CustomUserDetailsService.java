package com.aiyolo.service;

import com.aiyolo.constant.RoleEnum;
import com.aiyolo.entity.User;
import com.aiyolo.repository.UserRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import java.util.*;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired UserRepository userRepository;

    @Autowired AreaCodeService areaCodeService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("not found");
        }

        if (RoleEnum.EMPLOYEE.equals(user.getRole())) {
            // 取渠道管理员账号状态
            User channelManager = userRepository.findFirstByChannelIdAndRoleOrderByIdDesc(user.getChannelId(), RoleEnum.AGENT);
            if (channelManager != null) {
                user.setStatus(channelManager.getStatus());
            }
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        // 渠道
        authorities.add(new SimpleGrantedAuthority("CHAN_" + user.getChannelId()));

        // 关联产品
        if (StringUtils.isNotEmpty(user.getProductIds())) {
            String[] productIdArray = user.getProductIds().split(",");
            for (int i = 0; i < productIdArray.length; i++) {
                authorities.add(new SimpleGrantedAuthority("PROD_" + productIdArray[i]));
            }
        }

        // 区域权限
        String gatewayAreaCodes = user.getGatewayAreaCodes();
        if (RoleEnum.EMPLOYEE.equals(user.getRole())) {
            // 判断区域权限，不能大于渠道管理员
            User channelManager = userRepository.findFirstByChannelIdAndRoleOrderByIdDesc(user.getChannelId(), RoleEnum.AGENT);
            if (channelManager == null) {
                gatewayAreaCodes = null;
            } else if (StringUtils.isEmpty(gatewayAreaCodes)) {
                gatewayAreaCodes = channelManager.getGatewayAreaCodes();
            } else if (StringUtils.isNotEmpty(channelManager.getGatewayAreaCodes())) {
                List<String> channelAreaCodes = Arrays.asList(channelManager.getGatewayAreaCodes().split(","));
                String[] userAreaCodes = gatewayAreaCodes.split(",");
                List<String> newUserAreaCodes = new ArrayList<String>();
                for (int i = 0; i < userAreaCodes.length; i++) {
                    if (areaCodeService.checkAreaCode(userAreaCodes[i]) &&
                            areaCodeService.areaCodeMatches(userAreaCodes[i], channelAreaCodes)) {
                        newUserAreaCodes.add(userAreaCodes[i]);
                    }
                }
                if (newUserAreaCodes.size() > 0) {
                    gatewayAreaCodes = String.join(",", newUserAreaCodes);
                } else {
                    gatewayAreaCodes = null;
                }
            }
        }

        if (gatewayAreaCodes != null) {
            if ("".equals(gatewayAreaCodes)) {
                authorities.add(new SimpleGrantedAuthority("AREA_0"));
            } else {
                String[] areaCodeArray = gatewayAreaCodes.split(",");
                for (int i = 0; i < areaCodeArray.length; i++) {
                    if (areaCodeService.checkAreaCode(areaCodeArray[i])) {
                        authorities.add(new SimpleGrantedAuthority("AREA_" + areaCodeArray[i]));
                    }
                }
            }
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                true,
                true,
                true,
                User.STATUS_ENABLE.equals(user.getStatus()),
                authorities);
    }

    public Map<String, List<String>> getAuthorities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        List<String> roles = new ArrayList<String>();
        List<String> channels = new ArrayList<String>();
        List<String> products = new ArrayList<String>();
        List<String> areas = new ArrayList<String>();
        Map<String, List<String>> authorities = new HashMap<String, List<String>>();

        for (GrantedAuthority ga : userDetails.getAuthorities()) {
            if (ga.getAuthority().length() > 5 && ga.getAuthority().startsWith("ROLE_")) {
                roles.add(ga.getAuthority().substring(5));
            } else if (ga.getAuthority().length() > 5 && ga.getAuthority().startsWith("CHAN_")) {
                channels.add(ga.getAuthority().substring(5));
            } else if (ga.getAuthority().length() > 5 && ga.getAuthority().startsWith("PROD_")) {
                products.add(ga.getAuthority().substring(5));
            } else if (ga.getAuthority().length() > 5 && ga.getAuthority().startsWith("AREA_")) {
                areas.add(ga.getAuthority().substring(5));
            }
        }

        authorities.put("roles", roles);
        authorities.put("channels", channels);
        authorities.put("products", products);
        authorities.put("areas", areas);

        return authorities;
    }

    public long getChannelId() {
        Map<String, List<String>> authorities = getAuthorities();
        List<String> channels = authorities.get("channels");

        return (channels != null && channels.size() > 0) ? Long.parseLong(channels.get(0)) : null;
    }

    public RoleEnum getRole() {
        Map<String, List<String>> authorities = getAuthorities();
        List<String> roles = authorities.get("roles");

        if (ListUtils.isEmpty(roles)) {
            return null;
        }

        for (int i = 0; i < RoleEnum.values().length; i++) {
            if (RoleEnum.values()[i].name().equals(roles.get(0))) {
                return RoleEnum.values()[i];
            }
        }

        return null;
    }

}
