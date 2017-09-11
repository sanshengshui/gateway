package com.aiyolo.service.api;

import java.util.Random;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.constant.Role4DeviceEnum;
import com.aiyolo.entity.AppUserDevice;
import com.aiyolo.entity.DeviceSharePass;
import com.aiyolo.repository.AppUserDeviceRepository;
import com.aiyolo.repository.DeviceSharePassRepository;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.request.ShareDeviceRequest;
import com.aiyolo.service.api.response.Response;
import com.aiyolo.service.api.response.ResponseObject;
import com.aiyolo.service.api.response.ShareDeviceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ShareDeviceService extends BaseService {

    @Autowired AppUserDeviceRepository appUserDeviceRepository;
    @Autowired DeviceSharePassRepository deviceSharePassRepository;

    @Override
    @SuppressWarnings("unchecked")
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        String userId = authenticate(request);

        ShareDeviceRequest shareDeviceRequest = (ShareDeviceRequest) request;
        String imei = shareDeviceRequest.getImei();
        if (StringUtils.isEmpty(imei)) {
            return (Res) responseRequestParameterError(request.getAction());
        }

        AppUserDevice appUserDevice = appUserDeviceRepository.findOneByUserIdAndGlImei(userId, imei);
        if (appUserDevice == null || !appUserDevice.getRole().equals(Role4DeviceEnum.MANAGER)) {
            return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "非管理员");
        }

        Random rand = new Random();
        int randomNum = rand.nextInt((999999 - 111111) + 1) + 111111;

        DeviceSharePass deviceSharePass = deviceSharePassRepository.findFirstByGlImeiOrderByIdDesc(imei);
        if (deviceSharePass == null) {
            deviceSharePass = new DeviceSharePass(imei, String.valueOf(randomNum));
        } else {
            deviceSharePass.setPass(String.valueOf(randomNum));
        }
        deviceSharePassRepository.save(deviceSharePass);

        return (Res) new ShareDeviceResponse(request, deviceSharePass);
    }

}
