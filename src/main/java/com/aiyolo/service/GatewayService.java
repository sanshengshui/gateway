package com.aiyolo.service;

import com.aiyolo.channel.data.request.GatewayInfoRequest;
import com.aiyolo.common.ArrayHelper;
import com.aiyolo.common.SpringUtil;
import com.aiyolo.constant.Role4GatewayEnum;
import com.aiyolo.entity.*;
import com.aiyolo.queue.Sender;
import com.aiyolo.repository.*;
import net.sf.json.JSONArray;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GatewayService {

    @Autowired
    GatewayRepository gatewayRepository;
    @Autowired
    GatewayStaRepository gatewayStaRepository;

    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    AppUserGatewayRepository appUserGatewayRepository;
    @Autowired
    AppUserSessionRepository appUserSessionRepository;

    @Autowired
    AreaCodeService areaCodeService;
    @Autowired
    CustomUserDetailsService customUserDetailsService;

    public List<Gateway> getAllGatewayByAreaCode(String areaCode) {
        List<Gateway> gateways = new ArrayList<Gateway>();

        // 取有权限的区域
        Map<String, List<String>> authorities = customUserDetailsService.getAuthorities();
        if (authorities.get("areas").contains("0") && "0".equals(areaCode)) {
            gateways = (List<Gateway>) gatewayRepository.findAll();
        } else {
            String areaCodeSearchPat = areaCodeService.getAreaCodeSearchPat(authorities, areaCode);
            if (StringUtils.isNotEmpty(areaCodeSearchPat)) {
                gateways = gatewayRepository.findByAreaCodeMatch(areaCodeSearchPat);
            }
        }

        return gateways;
    }

    public Page<Gateway> getPageGatewayByAreaCodeAndAddress(Pageable pageable, String areaCode, String address) {
        Page<Gateway> gateways = new PageImpl<Gateway>(new ArrayList<Gateway>());

        // 取有权限的区域
        Map<String, List<String>> authorities = customUserDetailsService.getAuthorities();
        if (authorities.get("areas").contains("0") && "0".equals(areaCode)) {
            if (StringUtils.isEmpty(address)) {
                gateways = gatewayRepository.findAll(pageable);
            } else {
                gateways = gatewayRepository.findPageByAddressLike(pageable, "%" + address + "%");
            }
        } else {
            String areaCodeSearchPat = areaCodeService.getAreaCodeSearchPat(authorities, areaCode);
            if (StringUtils.isNotEmpty(areaCodeSearchPat)) {
                if (StringUtils.isEmpty(address)) {
                    gateways = gatewayRepository.findPageByAreaCodeMatch(pageable, areaCodeSearchPat);
                } else {
                    gateways = gatewayRepository.findPageByAreaCodeAndAddressMatch(pageable, areaCodeSearchPat, address);
                }
            }
        }

        return gateways;
    }

    public Gateway getGatewayById(String id) {
        Long gatewayId;
        try {
            gatewayId = Long.valueOf(id);
        } catch (NumberFormatException e) {
            return null;
        }

        return getGatewayById(gatewayId);
    }

    public Gateway getGatewayById(Long id) {
        if (id == null) {
            return null;
        }

        Gateway gateway = gatewayRepository.findOne(id);
        if (gateway != null) {
            Map<String, List<String>> authorities = customUserDetailsService.getAuthorities();
            if (areaCodeService.areaCodeMatches(gateway.getAreaCode(), authorities.get("areas"))) {
                if (StringUtils.isEmpty(gateway.getLocationAreaCode()) || StringUtils.isEmpty(gateway.getLocationAddress())) {
                    // 读取基站地址
                    GatewaySta gatewaySta = gatewayStaRepository.findFirstByGlIdOrderByIdDesc(gateway.getGlId());
                    if (gatewaySta != null && !StringUtils.isEmpty(gatewaySta.getLocationAreaCode()) && !StringUtils.isEmpty(gatewaySta.getLocationAddress())) {
                        gateway.setLocationAreaCode(gatewaySta.getLocationAreaCode());
                        gateway.setLocationAddress(gatewaySta.getLocationAddress());
                    }
                }

                // 添加网关管理员手机至userPhones
                String managerPhone = getManagerPhone(gateway.getGlImei());
                String[] userPhones = ArrayHelper.getStringArray(gateway.getUserPhones());
                String[] newUserPhones = (String[]) ArrayUtils.addAll(new String[]{managerPhone}, userPhones);
                gateway.setUserPhones(ArrayHelper.getArrayString(newUserPhones));

                return gateway;
            }
        }

        return null;
    }

    public Gateway getGatewayByGlId(String glId) {
        if (glId == null) {
            return null;
        }

        Gateway gateway = gatewayRepository.findFirstByGlIdOrderByIdDesc(glId);
        if (gateway != null) {
            Map<String, List<String>> authorities = customUserDetailsService.getAuthorities();
            if (areaCodeService.areaCodeMatches(gateway.getAreaCode(), authorities.get("areas"))) {
                return gateway;
            }
        }

        return null;
    }

    public Gateway getGatewayByGlImei(String glImei) {
        if (glImei == null) {
            return null;
        }

        Gateway gateway = gatewayRepository.findFirstByGlImeiOrderByIdDesc(glImei);
        if (gateway != null) {
            Map<String, List<String>> authorities = customUserDetailsService.getAuthorities();
            if (areaCodeService.areaCodeMatches(gateway.getAreaCode(), authorities.get("areas"))) {
                return gateway;
            }
        }

        return null;
    }

    public List<AppUserGateway> getGatewayAppUsers(String glImei) {
        if (glImei == null) {
            return null;
        }

        return appUserGatewayRepository.findByGlImei(glImei);
    }

    public String[] getGatewayUserMobileIds(String glImei) {
        return getGatewayUserMobileIds(getGatewayAppUsers(glImei));
    }

    public String[] getGatewayUserMobileIds(List<AppUserGateway> gatewayAppUsers) {
        List<String> mobileIds = new ArrayList<String>();

        if (gatewayAppUsers != null && gatewayAppUsers.size() > 0) {
            // 查询绑定用户mobile_id
            for (int i = 0; i < gatewayAppUsers.size(); i++) {
                AppUserSession appUserSession = appUserSessionRepository.findFirstByUserIdOrderByIdDesc(gatewayAppUsers.get(i).getUserId());
                if (appUserSession != null) {
                    mobileIds.add(appUserSession.getMobileId());
                }
            }
        }

        return mobileIds.size() > 0 ? mobileIds.toArray(new String[0]) : null;
    }

    public String[] getGatewayUserPhones(String glImei) {
        List<String> phones = new ArrayList<String>();

        // 查询网关绑定用户
        List<AppUserGateway> gatewayAppUsers = getGatewayAppUsers(glImei);
        if (gatewayAppUsers != null && gatewayAppUsers.size() > 0) {
            // 查询绑定用户phone
            for (int i = 0; i < gatewayAppUsers.size(); i++) {
                AppUser appUser = appUserRepository.findFirstByUserIdOrderByIdDesc(gatewayAppUsers.get(i).getUserId());
                if (appUser != null && StringUtils.isNotEmpty(appUser.getPhone()) && !phones.contains(appUser.getPhone())) {
                    phones.add(appUser.getPhone());
                }
            }
        }

        Gateway gateway = gatewayRepository.findFirstByGlImeiOrderByIdDesc(glImei);
        if (ArrayHelper.validJsonArray(gateway.getUserPhones())) {
            // 查询网关联系人手机
            String[] userPhones = ArrayHelper.getStringArray(JSONArray.fromObject(gateway.getUserPhones()));
            for (String userPhone : userPhones) {
                if (StringUtils.isNotEmpty(userPhone) && !phones.contains(userPhone)) {
                    phones.add(userPhone);
                }
            }
        }

        return phones.size() > 0 ? phones.toArray(new String[0]) : null;
    }

    public String getManagerPhone(String glImei) {
        AppUserGateway gatewayManager = appUserGatewayRepository.findOneByGlImeiAndRole(glImei, Role4GatewayEnum.MANAGER);
        if (gatewayManager != null && gatewayManager.getAppUser() != null) {
            return gatewayManager.getAppUser().getPhone();
        }

        return "";
    }

    public String getFullAddress(String address, String areaCode) {
        String areaName = areaCodeService.getAreaName(areaCode);
        return StringUtils.isNotEmpty(areaName) ? (areaName + address) : address;
    }

    /**
     * 查询网关信息
     *
     * @param glId
     * @return
     */
    public void requestGatewayInfo(String glId) throws Exception {
        if (StringUtils.isNotEmpty(glId)) {
            Map<String, Object> headerMap = GatewayInfoRequest.getInstance().requestHeader(glId);
            Map<String, Object> bodyMap = GatewayInfoRequest.getInstance().requestBody(glId);

            Sender sender = (Sender) SpringUtil.getBean("sender");
            sender.sendMessage(headerMap, bodyMap);
        }
    }

}
