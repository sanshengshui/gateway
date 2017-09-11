package com.aiyolo.service.api;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.constant.Role4DeviceEnum;
import com.aiyolo.entity.AppUserDevice;
import com.aiyolo.repository.AppUserDeviceRepository;
import com.aiyolo.service.api.request.GetContactInfoRequest;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.response.GetContactInfoResponse;
import com.aiyolo.service.api.response.Response;
import com.aiyolo.service.api.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class GetContactInfoService extends BaseService {

    @Autowired AppUserDeviceRepository appUserDeviceRepository;

    @Override
    @SuppressWarnings("unchecked")
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        String userId = authenticate(request);

        GetContactInfoRequest getContactInfoRequest = (GetContactInfoRequest) request;
        String imei = getContactInfoRequest.getImei();
        if (StringUtils.isEmpty(imei)) {
            return (Res) responseRequestParameterError(request.getAction());
        }

        AppUserDevice appUserDevice = appUserDeviceRepository.findOneByUserIdAndGlImei(userId, imei);
        if (appUserDevice == null || appUserDevice.getDevice() == null) {
            return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "未找到设备");
        } else if (!appUserDevice.getRole().equals(Role4DeviceEnum.MANAGER)) {
            return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_AUTHORITY.getResult(), "无权限");
        } else {
            return (Res) new GetContactInfoResponse(request, appUserDevice.getDevice());
        }
    }

}
