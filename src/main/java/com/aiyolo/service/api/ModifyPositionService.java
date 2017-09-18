package com.aiyolo.service.api;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.entity.AppUserGateway;
import com.aiyolo.entity.Device;
import com.aiyolo.repository.AppUserGatewayRepository;
import com.aiyolo.repository.DeviceRepository;
import com.aiyolo.service.api.request.ModifyPositionRequest;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.response.Response;
import com.aiyolo.service.api.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ModifyPositionService extends BaseService {

    @Autowired
    AppUserGatewayRepository appUserGatewayRepository;
    @Autowired
    DeviceRepository deviceRepository;

    @Override
    @SuppressWarnings("unchecked")
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        String userId = authenticate(request);

        ModifyPositionRequest modifyPositionRequest = (ModifyPositionRequest) request;
        String imei = modifyPositionRequest.getImei();
        String position = modifyPositionRequest.getPosition();
        if (StringUtils.isEmpty(imei)) {
            return (Res) responseRequestParameterError(request.getAction());
        }

        Device device = deviceRepository.findFirstByImeiOrderByIdDesc(imei);
        if (device == null) {
            return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "未找到设备");
        } else {
            AppUserGateway appUserGateway = appUserGatewayRepository.findOneByUserIdAndGlImei(userId, device.getGlImei());
            if (appUserGateway == null || appUserGateway.getGateway() == null) {
                return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "未找到网关");
            }
        }

        device.setPosition(position);
        deviceRepository.save(device);

        return (Res) responseSuccess(request.getAction());
    }

}
