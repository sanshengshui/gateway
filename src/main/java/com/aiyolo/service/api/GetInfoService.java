package com.aiyolo.service.api;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.constant.Role4GatewayEnum;
import com.aiyolo.entity.AppUserGateway;
import com.aiyolo.repository.AppUserGatewayRepository;
import com.aiyolo.service.api.request.GetInfoRequest;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.response.GetInfoResponse;
import com.aiyolo.service.api.response.Response;
import com.aiyolo.service.api.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class GetInfoService extends BaseService {

    @Autowired
    AppUserGatewayRepository appUserGatewayRepository;

    @Override
    @SuppressWarnings("unchecked")
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        String userId = authenticate(request);

        GetInfoRequest getInfoRequest = (GetInfoRequest) request;
        String imei = getInfoRequest.getImei();
        if (StringUtils.isEmpty(imei)) {
            return (Res) responseRequestParameterError(request.getAction());
        }

        AppUserGateway appUserGateway = appUserGatewayRepository.findOneByUserIdAndGlImei(userId, imei);
        if (appUserGateway == null || appUserGateway.getGateway() == null) {
            return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "未找到网关");
        } else if (!appUserGateway.getRole().equals(Role4GatewayEnum.MANAGER)) {
            return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_AUTHORITY.getResult(), "无权限");
        } else {
            return (Res) new GetInfoResponse(request, appUserGateway.getGateway());
        }
    }

}
