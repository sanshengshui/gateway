package com.aiyolo.service.api;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.constant.Role4GatewayEnum;
import com.aiyolo.entity.AppUserGateway;
import com.aiyolo.entity.GatewaySharePass;
import com.aiyolo.repository.AppUserGatewayRepository;
import com.aiyolo.repository.GatewaySharePassRepository;
import com.aiyolo.service.api.request.AddGatewayRequest;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.response.Response;
import com.aiyolo.service.api.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class AddGatewayService extends BaseService {

    @Autowired
    AppUserGatewayRepository appUserGatewayRepository;
    @Autowired
    GatewaySharePassRepository gatewaySharePassRepository;

    @Override
    @SuppressWarnings("unchecked")
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        String userId = authenticate(request);

        AddGatewayRequest addGatewayRequest = (AddGatewayRequest) request;
        String imei = addGatewayRequest.getImei();
        String pass = addGatewayRequest.getPass();
        if (StringUtils.isEmpty(imei)) {
            return (Res) responseRequestParameterError(request.getAction());
        }

        List<AppUserGateway> gatewayAppUsers = appUserGatewayRepository.findByGlImei(imei);
        if (gatewayAppUsers.size() == 0) { // 首位绑定网关的人无需密码，并且自动成为管理员
            appUserGatewayRepository.save(new AppUserGateway(userId, imei, Role4GatewayEnum.MANAGER));
        } else {
            if (StringUtils.isEmpty(pass)) {
                return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "请联系管理员分享网关");
            }

            GatewaySharePass gatewaySharePass = gatewaySharePassRepository.findFirstByGlImeiOrderByIdDesc(imei);
            if (gatewaySharePass == null || !gatewaySharePass.getPass().equals(pass) ||
                    (System.currentTimeMillis() - gatewaySharePass.getUpdatedAt().getTime()) > 5*60*1000L) {
                return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "动态密码错误");
            }

            for (int i = 0; i < gatewayAppUsers.size(); i++) {
                if (gatewayAppUsers.get(i).getUserId().equals(userId)) {
                    return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "该用户已绑定");
                }
            }

            appUserGatewayRepository.save(new AppUserGateway(userId, imei, Role4GatewayEnum.USER));
        }

        return (Res) responseSuccess(request.getAction());
    }

}
