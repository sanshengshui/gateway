package com.aiyolo.service.api;

import com.aiyolo.common.ArrayHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.constant.Role4DeviceEnum;
import com.aiyolo.entity.AppUserDevice;
import com.aiyolo.entity.Device;
import com.aiyolo.repository.AppUserDeviceRepository;
import com.aiyolo.repository.DeviceRepository;
import com.aiyolo.service.api.request.InputContactInfoRequest;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.response.Response;
import com.aiyolo.service.api.response.ResponseObject;

import net.sf.json.JSONArray;

@Service
public class InputContactInfoService extends BaseService {

    @Autowired DeviceRepository deviceRepository;
    @Autowired AppUserDeviceRepository appUserDeviceRepository;

    @Override
    @SuppressWarnings("unchecked")
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        String userId = authenticate(request);

        InputContactInfoRequest inputContactInfoRequest = (InputContactInfoRequest) request;
        String imei = inputContactInfoRequest.getImei();
        if (StringUtils.isEmpty(imei)) {
            return (Res) responseRequestParameterError(request.getAction());
        }

        AppUserDevice appUserDevice = appUserDeviceRepository.findOneByUserIdAndGlImei(userId, imei);
        if (appUserDevice == null || appUserDevice.getDevice() == null) {
            return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "未找到设备");
        } else if (!appUserDevice.getRole().equals(Role4DeviceEnum.MANAGER)) {
            return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_AUTHORITY.getResult(), "无权限");
        } else {
            Device device = appUserDevice.getDevice();
            device.setGlName(inputContactInfoRequest.getGlName());
            device.setUserName(inputContactInfoRequest.getUserName());
            device.setUserPhones(JSONArray.fromObject(ArrayHelper.removeDuplicateElement(inputContactInfoRequest.getPhones())).toString());
            device.setAreaCode(inputContactInfoRequest.getAreaCode());
            device.setAddress(inputContactInfoRequest.getAddress());
            deviceRepository.save(device);
            return (Res) responseSuccess(request.getAction());
        }
    }

}
