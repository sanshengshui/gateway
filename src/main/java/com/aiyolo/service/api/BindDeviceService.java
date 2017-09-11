package com.aiyolo.service.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.constant.Role4DeviceEnum;
import com.aiyolo.entity.AppUserDevice;
import com.aiyolo.entity.DeviceSharePass;
import com.aiyolo.repository.AppUserDeviceRepository;
import com.aiyolo.repository.DeviceSharePassRepository;
import com.aiyolo.service.api.request.BindDeviceRequest;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.response.Response;
import com.aiyolo.service.api.response.ResponseObject;

@Service
public class BindDeviceService extends BaseService {

    @Autowired AppUserDeviceRepository appUserDeviceRepository;
    @Autowired DeviceSharePassRepository deviceSharePassRepository;

    @Override
    @SuppressWarnings("unchecked")
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        String userId = authenticate(request);

        BindDeviceRequest bindDeviceRequest = (BindDeviceRequest) request;
        String imei = bindDeviceRequest.getImei();
        String pass = bindDeviceRequest.getPass();
        if (StringUtils.isEmpty(imei)) {
            return (Res) responseRequestParameterError(request.getAction());
        }

        List<AppUserDevice> deviceAppUsers = appUserDeviceRepository.findByGlImei(imei);
        if (deviceAppUsers.size() == 0) { // 首位绑定设备的人无需密码，并且自动成为管理员
            appUserDeviceRepository.save(new AppUserDevice(userId, imei, Role4DeviceEnum.MANAGER));
        } else {
            if (StringUtils.isEmpty(pass)) {
                return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "请联系管理员分享设备");
            }

            DeviceSharePass deviceSharePass = deviceSharePassRepository.findFirstByGlImeiOrderByIdDesc(imei);
            if (deviceSharePass == null || !deviceSharePass.getPass().equals(pass) ||
                    (System.currentTimeMillis() - deviceSharePass.getUpdatedAt().getTime()) > 5*60*1000L) {
                return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "动态密码错误");
            }

            for (int i = 0; i < deviceAppUsers.size(); i++) {
                if (deviceAppUsers.get(i).getUserId().equals(userId)) {
                    return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "该用户已绑定");
                }
            }

            appUserDeviceRepository.save(new AppUserDevice(userId, imei, Role4DeviceEnum.USER));
        }

        return (Res) responseSuccess(request.getAction());
    }

}
