package com.aiyolo.service.api;

import java.util.ArrayList;
import java.util.List;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.constant.Role4DeviceEnum;
import com.aiyolo.entity.AppUserDevice;
import com.aiyolo.repository.AppUserDeviceRepository;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.request.UserListRequest;
import com.aiyolo.service.api.response.AppUserObject;
import com.aiyolo.service.api.response.Response;
import com.aiyolo.service.api.response.ResponseObject;
import com.aiyolo.service.api.response.UserListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserListService extends BaseService {

    @Autowired AppUserDeviceRepository appUserDeviceRepository;

    @Override
    @SuppressWarnings("unchecked")
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        String userId = authenticate(request);

        UserListRequest userListRequest = (UserListRequest) request;
        String imei = userListRequest.getImei();
        if (StringUtils.isEmpty(imei)) {
            return (Res) responseRequestParameterError(request.getAction());
        }

        AppUserDevice appUserDevice = appUserDeviceRepository.findOneByUserIdAndGlImei(userId, imei);
        if (appUserDevice == null || appUserDevice.getDevice() == null) {
            return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "未找到设备");
        } else if (!appUserDevice.getRole().equals(Role4DeviceEnum.MANAGER)) {
            return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_AUTHORITY.getResult(), "无权限");
        }

        List<AppUserDevice> deviceAppUsers = appUserDeviceRepository.findByGlImei(imei);

        List<AppUserObject> users = new ArrayList<AppUserObject>();
        for (int i = 0; i < deviceAppUsers.size(); i++) {
            AppUserObject appUserObject = new AppUserObject();
            appUserObject.setAdmin(deviceAppUsers.get(i).getRole().getValue());

            if (deviceAppUsers.get(i).getAppUser() != null) {
                appUserObject.setName(deviceAppUsers.get(i).getAppUser().getName());
                appUserObject.setPhone(deviceAppUsers.get(i).getAppUser().getPhone());
                appUserObject.setAvatar(deviceAppUsers.get(i).getAppUser().getAvatar());
            }

            users.add(appUserObject);
        }

        return (Res) new UserListResponse(request, users);
    }

}
