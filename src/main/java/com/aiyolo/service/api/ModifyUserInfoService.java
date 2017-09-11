package com.aiyolo.service.api;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.entity.AppUser;
import com.aiyolo.repository.AppUserDeviceRepository;
import com.aiyolo.repository.AppUserRepository;
import com.aiyolo.repository.DeviceRepository;
import com.aiyolo.service.DeviceService;
import com.aiyolo.service.api.request.ModifyUserInfoRequest;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.response.Response;
import com.aiyolo.service.api.response.ResponseObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModifyUserInfoService extends BaseService {

    @Autowired AppUserRepository appUserRepository;
    @Autowired AppUserDeviceRepository appUserDeviceRepository;
    @Autowired DeviceRepository deviceRepository;

    @Autowired DeviceService deviceService;

    @Override
    @SuppressWarnings("unchecked")
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        String userId = authenticate(request);

        ModifyUserInfoRequest modifyUserInfoRequest = (ModifyUserInfoRequest) request;
        String name = modifyUserInfoRequest.getName();
        String phone = modifyUserInfoRequest.getPhone();
        String avatar = modifyUserInfoRequest.getAvatar();
        String mailAddress = modifyUserInfoRequest.getMailAddress();

        if (StringUtils.isNotEmpty(phone)) {
            AppUser _appUser = appUserRepository.findFirstByPhoneOrderByIdDesc(phone);
            if (_appUser != null && !_appUser.getUserId().equals(userId)) {
                return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "手机号已存在");
            }
        }

        AppUser appUser = appUserRepository.findFirstByUserIdOrderByIdDesc(userId);
        if (appUser != null) {
            if (StringUtils.isNotEmpty(name)) {
                appUser.setName(name);
            }
            if (StringUtils.isNotEmpty(phone)) {
                appUser.setPhone(phone);
            }
            if (StringUtils.isNotEmpty(avatar)) {
                appUser.setAvatar(avatar);
            }
            if (StringUtils.isNotEmpty(mailAddress)) {
                appUser.setMailAddress(mailAddress);
            }
        } else {
            appUser = new AppUser(userId, name, phone, avatar, mailAddress);
        }
        appUserRepository.save(appUser);

        return (Res) responseSuccess(request.getAction());
    }

}
