package com.aiyolo.service.api;

import java.util.List;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.constant.Role4DeviceEnum;
import com.aiyolo.entity.AppUser;
import com.aiyolo.entity.AppUserDevice;
import com.aiyolo.repository.AppUserDeviceRepository;
import com.aiyolo.repository.AppUserRepository;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.request.UnbindDeviceRequest;
import com.aiyolo.service.api.response.Response;
import com.aiyolo.service.api.response.ResponseObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnbindDeviceService extends BaseService {

    @Autowired AppUserRepository appUserRepository;
    @Autowired AppUserDeviceRepository appUserDeviceRepository;

    @Override
    @SuppressWarnings("unchecked")
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        String userId = authenticate(request);

        UnbindDeviceRequest unbindDeviceRequest = (UnbindDeviceRequest) request;
        String imei = unbindDeviceRequest.getImei();
        String phone = unbindDeviceRequest.getPhone();
        if (StringUtils.isEmpty(imei)) {
            return (Res) responseRequestParameterError(request.getAction());
        }

        AppUserDevice appUserDevice = appUserDeviceRepository.findOneByUserIdAndGlImei(userId, imei);
        if (appUserDevice == null) {
            return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "未找到绑定关系");
        }

        if (!appUserDevice.getRole().equals(Role4DeviceEnum.MANAGER) && StringUtils.isEmpty(phone)) { // 非管理员请求，直接解绑
            appUserDeviceRepository.delete(appUserDevice);
        } else { // 管理员请求，只有设备无其他绑定用户时才可解绑自己
            if (!appUserDevice.getRole().equals(Role4DeviceEnum.MANAGER)) {
                return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "非管理员");
            }

            Boolean unbindAdmin = false; // 是否解绑自己（管理员）
            if (StringUtils.isNotEmpty(phone)) {
                AppUser appUser = appUserRepository.findFirstByPhoneOrderByIdDesc(phone);
                if (appUser == null) {
                    return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "未找到手机号对应的用户");
                }
                if (appUser.getUserId().equals(userId)) {
                    unbindAdmin = true; // 是解绑自己（管理员）
                } else {
                    AppUserDevice _appUserDevice = appUserDeviceRepository.findOneByUserIdAndGlImei(appUser.getUserId(), imei);
                    if (_appUserDevice == null) {
                        return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "未找到绑定关系");
                    } else {
                        appUserDeviceRepository.delete(_appUserDevice); // 解绑非管理员
                    }
                }
            }

            if (unbindAdmin || StringUtils.isEmpty(phone)) { // 解绑自己（管理员）
                // 只有设备无其他绑定用户时才可解绑自己
                List<AppUserDevice> deviceAppUsers = appUserDeviceRepository.findByGlImei(imei);
                if (deviceAppUsers.size() > 1) {
                    return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "请先转让管理员，或删除所有成员，才可解绑");
                } else {
                    appUserDeviceRepository.delete(appUserDevice); // 解绑管理员
                }
            }
        }

        return (Res) responseSuccess(request.getAction());
    }

}
