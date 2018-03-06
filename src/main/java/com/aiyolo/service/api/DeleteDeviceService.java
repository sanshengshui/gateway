package com.aiyolo.service.api;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.constant.Role4GatewayEnum;
import com.aiyolo.entity.AppUserGateway;
import com.aiyolo.entity.Device;
import com.aiyolo.repository.AppUserGatewayRepository;
import com.aiyolo.repository.DeviceRepository;
import com.aiyolo.service.DeviceStatusService;
import com.aiyolo.service.api.request.DeleteDeviceRequest;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.response.Response;
import com.aiyolo.service.api.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class DeleteDeviceService extends BaseService {

    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    DeviceStatusService deviceStatusService;
    @Autowired
    AppUserGatewayRepository appUserGatewayRepository;

    @Override
    @SuppressWarnings("unchecked")
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        String userId = authenticate(request);

        DeleteDeviceRequest deleteDeviceRequest = (DeleteDeviceRequest) request;
        String imeiGateway = deleteDeviceRequest.getImeiGateway();
        String imeiDev = deleteDeviceRequest.getImeiDev();
        if (StringUtils.isEmpty(imeiGateway) || StringUtils.isEmpty(imeiDev)) {
            return (Res) responseRequestParameterError(request.getAction());
        }

        AppUserGateway appUserGateway = appUserGatewayRepository.findOneByUserIdAndGlImei(userId, imeiGateway);
        if (appUserGateway == null || appUserGateway.getGateway() == null) {
            return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "未找到网关");
        } /*else if (!appUserGateway.getRole().equals(Role4GatewayEnum.MANAGER)) {
            return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_AUTHORITY.getResult(), "无权限");
        } */else {
            Device device = deviceRepository.findFirstByImeiOrderByIdDesc(imeiDev);
            if (device == null) {
                return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "未找到设备");
            } else if (!imeiGateway.equals(device.getGlImei())) {
                return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_AUTHORITY.getResult(), "无权限");
            }

            deviceRepository.delete(device);

            return (Res) responseSuccess(request.getAction());
        }
    }

}
