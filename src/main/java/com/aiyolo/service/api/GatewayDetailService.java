package com.aiyolo.service.api;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.entity.AppUserGateway;
import com.aiyolo.repository.AppUserGatewayRepository;
import com.aiyolo.repository.DeviceRepository;
import com.aiyolo.repository.GatewayStatusRepository;
import com.aiyolo.service.api.request.GatewayDetailRequest;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.response.GatewayDetailResponse;
import com.aiyolo.service.api.response.Response;
import com.aiyolo.service.api.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class GatewayDetailService extends BaseService {

    @Autowired
    GatewayStatusRepository gatewayStatusRepository;
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    AppUserGatewayRepository appUserGatewayRepository;

    @Override
    @SuppressWarnings("unchecked")
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        String userId = authenticate(request);

        GatewayDetailRequest gatewayDetailRequest = (GatewayDetailRequest) request;
        String imei = gatewayDetailRequest.getImei();
        if (StringUtils.isEmpty(imei)) {
            return (Res) responseRequestParameterError(request.getAction());
        }

        AppUserGateway appUserGateway = appUserGatewayRepository.findOneByUserIdAndGlImei(userId, imei);
        if (appUserGateway == null || appUserGateway.getGateway() == null) {
            return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "未找到设备");
        }

        Map<String, Object> gatewayDetail = new HashMap<String, Object>();
        gatewayDetail.put("imei", imei);


        return (Res) new GatewayDetailResponse(request, gatewayDetail);
    }

}
