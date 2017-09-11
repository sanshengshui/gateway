package com.aiyolo.service;

import com.aiyolo.channel.data.request.DeviceInfoRequest;
import com.aiyolo.common.ArrayHelper;
import com.aiyolo.common.SpringUtil;
import com.aiyolo.constant.Role4DeviceEnum;
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
public class DeviceService {

//    @PersistenceContext
//    private EntityManager em;

    @Autowired DeviceRepository deviceRepository;
    @Autowired DeviceStaRepository deviceStaRepository;

    @Autowired AppUserRepository appUserRepository;
    @Autowired AppUserDeviceRepository appUserDeviceRepository;
    @Autowired AppUserSessionRepository appUserSessionRepository;

    @Autowired AreaCodeService areaCodeService;
    @Autowired CustomUserDetailsService customUserDetailsService;

//    public List<Device> getAllDeviceByAreaCode(String areaCode, Map<String, List<String>> authorities) {
//        List<Device> devices = new ArrayList<Device>();
//
//        Integer areaCodeNumber = Integer.valueOf(areaCode);
//        if (areaCodeNumber == 0) {
//            // 取有权限的区域
//            if (authorities.get("roles").contains(RoleEnum.MANAGER.name())) { // 管理员有所有区域权限
//                devices = (List<Device>) deviceRepository.findAll();
//            } else if (authorities.get("areas") != null) {
//                String[] searchAreaCode = new String[authorities.get("areas").size()];
//                for (int i = 0; i < authorities.get("areas").size(); i++) { // 遍历权限区域
//                    Integer _areaCodeNumber = Integer.valueOf(authorities.get("areas").get(i));
//                    if (_areaCodeNumber % 10000 == 0) {
//                        searchAreaCode[i] = "left(area_code, 2) = '" + String.valueOf(_areaCodeNumber / 10000) + "'";
//                    } else if (_areaCodeNumber % 100 == 0) {
//                        searchAreaCode[i] = "left(area_code, 4) = '" + String.valueOf(_areaCodeNumber / 100) + "'";
//                    } else {
//                        searchAreaCode[i] = "area_code = '" + String.valueOf(_areaCodeNumber) + "'";
//                    }
//                }
//                String searchSql = StringUtils.join(searchAreaCode, " OR ");
//                Query query = em.createNativeQuery("SELECT * FROM device WHERE " + searchSql, Device.class);
//                devices = query.getResultList();
//            }
//        } else if (areaCodeNumber % 10000 == 0) {
//            devices = deviceRepository.findByAreaCodeStartingWith(String.valueOf(areaCodeNumber / 10000));
//        } else if (areaCodeNumber % 100 == 0) {
//            devices = deviceRepository.findByAreaCodeStartingWith(String.valueOf(areaCodeNumber / 100));
//        } else {
//            devices = deviceRepository.findByAreaCode(areaCode);
//        }
//
//        return devices;
//    }

    public List<Device> getAllDeviceByAreaCode(String areaCode) {
        List<Device> devices = new ArrayList<Device>();

        // 取有权限的区域
        Map<String, List<String>> authorities = customUserDetailsService.getAuthorities();
        if (authorities.get("areas").contains("0") && "0".equals(areaCode)) {
            devices = (List<Device>) deviceRepository.findAll();
        } else {
            String areaCodeSearchPat = areaCodeService.getAreaCodeSearchPat(authorities, areaCode);
            if (StringUtils.isNotEmpty(areaCodeSearchPat)) {
                devices = deviceRepository.findByAreaCodeMatch(areaCodeSearchPat);
            }
        }

        return devices;
    }

    public Page<Device> getPageDeviceByAreaCodeAndAddress(Pageable pageable, String areaCode, String address) {
        Page<Device> devices = new PageImpl<Device>(new ArrayList<Device>());

        // 取有权限的区域
        Map<String, List<String>> authorities = customUserDetailsService.getAuthorities();
        if (authorities.get("areas").contains("0") && "0".equals(areaCode)) {
            if (StringUtils.isEmpty(address)) {
                devices = deviceRepository.findAll(pageable);
            } else {
                devices = deviceRepository.findPageByAddressLike(pageable, "%" + address + "%");
            }
        } else {
            String areaCodeSearchPat = areaCodeService.getAreaCodeSearchPat(authorities, areaCode);
            if (StringUtils.isNotEmpty(areaCodeSearchPat)) {
                if (StringUtils.isEmpty(address)) {
                    devices = deviceRepository.findPageByAreaCodeMatch(pageable, areaCodeSearchPat);
                } else {
                    devices = deviceRepository.findPageByAreaCodeAndAddressMatch(pageable, areaCodeSearchPat, address);
                }
            }
        }

        return devices;
    }

    public Device getDeviceById(String id) {
        Long deviceId;
        try {
            deviceId = Long.valueOf(id);
        } catch (NumberFormatException e) {
            return null;
        }

        return getDeviceById(deviceId);
    }

    public Device getDeviceById(Long id) {
        if (id == null) {
            return null;
        }

        Device device = deviceRepository.findOne(id);
        if (device != null) {
            Map<String, List<String>> authorities = customUserDetailsService.getAuthorities();
            if (areaCodeService.areaCodeMatches(device.getAreaCode(), authorities.get("areas"))) {
                if (StringUtils.isEmpty(device.getLocationAreaCode()) || StringUtils.isEmpty(device.getLocationAddress())) {
                    // 读取基站地址
                    DeviceSta deviceSta = deviceStaRepository.findFirstByGlIdOrderByIdDesc(device.getGlId());
                    if (deviceSta != null && !StringUtils.isEmpty(deviceSta.getLocationAreaCode()) && !StringUtils.isEmpty(deviceSta.getLocationAddress())) {
                        device.setLocationAreaCode(deviceSta.getLocationAreaCode());
                        device.setLocationAddress(deviceSta.getLocationAddress());
                    }
                }

                // 添加设备管理员手机至userPhones
                String managerPhone = getManagerPhone(device.getGlImei());
                String[] userPhones = ArrayHelper.getStringArray(device.getUserPhones());
                String[] newUserPhones = (String[]) ArrayUtils.addAll(new String[]{managerPhone}, userPhones);
                device.setUserPhones(ArrayHelper.getArrayString(newUserPhones));

                return device;
            }
        }

        return null;
    }

    public Device getDeviceByGlId(String glId) {
        if (glId == null) {
            return null;
        }

        Device device = deviceRepository.findFirstByGlIdOrderByIdDesc(glId);
        if (device != null) {
            Map<String, List<String>> authorities = customUserDetailsService.getAuthorities();
            if (areaCodeService.areaCodeMatches(device.getAreaCode(), authorities.get("areas"))) {
                return device;
            }
        }

        return null;
    }

    public Device getDeviceByGlImei(String glImei) {
        if (glImei == null) {
            return null;
        }

        Device device = deviceRepository.findFirstByGlImeiOrderByIdDesc(glImei);
        if (device != null) {
            Map<String, List<String>> authorities = customUserDetailsService.getAuthorities();
            if (areaCodeService.areaCodeMatches(device.getAreaCode(), authorities.get("areas"))) {
                return device;
            }
        }

        return null;
    }

    public List<AppUserDevice> getDeviceAppUsers(String glImei) {
        if (glImei == null) {
            return null;
        }

        return appUserDeviceRepository.findByGlImei(glImei);
    }

    public String[] getDeviceUserMobileIds(String glImei) {
        return getDeviceUserMobileIds(getDeviceAppUsers(glImei));
    }

    public String[] getDeviceUserMobileIds(List<AppUserDevice> deviceAppUsers) {
        List<String> mobileIds = new ArrayList<String>();

        if (deviceAppUsers != null && deviceAppUsers.size() > 0) {
            // 查询绑定用户mobile_id
            for (int i = 0; i < deviceAppUsers.size(); i++) {
                AppUserSession appUserSession = appUserSessionRepository.findFirstByUserIdOrderByIdDesc(deviceAppUsers.get(i).getUserId());
                if (appUserSession != null) {
                    mobileIds.add(appUserSession.getMobileId());
                }
            }
        }

        return mobileIds.size() > 0 ? mobileIds.toArray(new String[0]) : null;
    }

    public String[] getDeviceUserPhones(String glImei) {
        List<String> phones = new ArrayList<String>();

        // 查询设备绑定用户
        List<AppUserDevice> deviceAppUsers = getDeviceAppUsers(glImei);
        if (deviceAppUsers != null && deviceAppUsers.size() > 0) {
            // 查询绑定用户phone
            for (int i = 0; i < deviceAppUsers.size(); i++) {
                AppUser appUser = appUserRepository.findFirstByUserIdOrderByIdDesc(deviceAppUsers.get(i).getUserId());
                if (appUser != null && StringUtils.isNotEmpty(appUser.getPhone()) && !phones.contains(appUser.getPhone())) {
                    phones.add(appUser.getPhone());
                }
            }
        }

        Device device = deviceRepository.findFirstByGlImeiOrderByIdDesc(glImei);
        if (ArrayHelper.validJsonArray(device.getUserPhones())) {
            // 查询设备联系人手机
            String[] userPhones = ArrayHelper.getStringArray(JSONArray.fromObject(device.getUserPhones()));
            for (String userPhone : userPhones) {
                if (StringUtils.isNotEmpty(userPhone) && !phones.contains(userPhone)) {
                    phones.add(userPhone);
                }
            }
        }

        return phones.size() > 0 ? phones.toArray(new String[0]) : null;
    }

    public String getManagerPhone(String glImei) {
        AppUserDevice deviceManager = appUserDeviceRepository.findOneByGlImeiAndRole(glImei, Role4DeviceEnum.MANAGER);
        if (deviceManager != null && deviceManager.getAppUser() != null) {
            return deviceManager.getAppUser().getPhone();
        }

        return "";
    }

    public String getFullAddress(String address, String areaCode) {
        String areaName = areaCodeService.getAreaName(areaCode);
        return StringUtils.isNotEmpty(areaName) ? (areaName + address) : address;
    }

    /**
     * 查询设备信息
     *
     * @param glId
     * @return
     */
    public void requestDeviceInfo(String glId) throws Exception {
        if (StringUtils.isNotEmpty(glId)) {
            Map<String, Object> headerMap = DeviceInfoRequest.getInstance().requestHeader(glId);
            Map<String, Object> bodyMap = DeviceInfoRequest.getInstance().requestBody(glId);

            Sender sender = (Sender) SpringUtil.getBean("sender");
            sender.sendMessage(headerMap, bodyMap);
        }
    }

}
