package com.aiyolo.service.api;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.constant.Role4GatewayEnum;
import com.aiyolo.entity.AppUserGateway;
import com.aiyolo.entity.GatewaySharePass;
import com.aiyolo.repository.AppUserGatewayRepository;
import com.aiyolo.repository.GatewaySharePassRepository;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.request.ShareGatewayRequest;
import com.aiyolo.service.api.response.Response;
import com.aiyolo.service.api.response.ResponseObject;
import com.aiyolo.service.api.response.ShareGatewayResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Random;

@Service
public class ShareGatewayService extends BaseService {

    @Autowired
    AppUserGatewayRepository appUserGatewayRepository;
    @Autowired
    GatewaySharePassRepository gatewaySharePassRepository;

    @Override
    @SuppressWarnings("unchecked")
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        String userId = authenticate(request);

        ShareGatewayRequest shareGatewayRequest = (ShareGatewayRequest) request;
        String imei = shareGatewayRequest.getImei();
        if (StringUtils.isEmpty(imei)) {
            return (Res) responseRequestParameterError(request.getAction());
        }

        AppUserGateway appUserGateway = appUserGatewayRepository.findOneByUserIdAndGlImei(userId, imei);
        if (appUserGateway == null || !appUserGateway.getRole().equals(Role4GatewayEnum.MANAGER)) {
            return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "非管理员");
        }

        Random rand = new Random();
        int randomNum = rand.nextInt((999999 - 111111) + 1) + 111111;

        GatewaySharePass gatewaySharePass = gatewaySharePassRepository.findFirstByGlImeiOrderByIdDesc(imei);
        if (gatewaySharePass == null) {
            gatewaySharePass = new GatewaySharePass(imei, String.valueOf(randomNum));
        } else {
            gatewaySharePass.setPass(String.valueOf(randomNum));
        }
        gatewaySharePassRepository.save(gatewaySharePass);

        return (Res) new ShareGatewayResponse(request, gatewaySharePass);
    }

}
