package com.aiyolo.service.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.constant.Role4GatewayEnum;
import com.aiyolo.entity.AppUser;
import com.aiyolo.entity.AppUserGateway;
import com.aiyolo.repository.AppUserGatewayRepository;
import com.aiyolo.repository.AppUserRepository;
import com.aiyolo.service.api.request.AdminTransferRequest;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.response.Response;
import com.aiyolo.service.api.response.ResponseObject;

@Service
public class AdminTransferService extends BaseService {

    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    AppUserGatewayRepository appUserGatewayRepository;

    @Override
    @SuppressWarnings("unchecked")
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        String userId = authenticate(request);

        AdminTransferRequest adminTransferRequest = (AdminTransferRequest) request;
        String imei = adminTransferRequest.getImei();
        String phone = adminTransferRequest.getPhone();
        if (StringUtils.isEmpty(imei) || StringUtils.isEmpty(phone)) {
            return (Res) responseRequestParameterError(request.getAction());
        }

        AppUserGateway appUserGateway = appUserGatewayRepository.findOneByUserIdAndGlImei(userId, imei);
        if (appUserGateway == null) {
            return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "未找到绑定关系");
        }
        if (!appUserGateway.getRole().equals(Role4GatewayEnum.MANAGER)) {
            return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "非管理员");
        }

        AppUser appUser = appUserRepository.findFirstByPhoneOrderByIdDesc(phone);
        if (appUser == null) {
            return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "未找到手机号对应的用户");
        }
        AppUserGateway _appUserGateway = appUserGatewayRepository.findOneByUserIdAndGlImei(appUser.getUserId(), imei);
        if (_appUserGateway == null) {
            return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "未找到绑定关系");
        }

        appUserGateway.setRole(Role4GatewayEnum.USER);
        appUserGatewayRepository.save(appUserGateway);

        _appUserGateway.setRole(Role4GatewayEnum.MANAGER);
        appUserGatewayRepository.save(_appUserGateway);

        return (Res) responseSuccess(request.getAction());
    }

}
