package com.aiyolo.service.api;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.constant.Role4GatewayEnum;
import com.aiyolo.entity.AppUserGateway;
import com.aiyolo.repository.AppUserGatewayRepository;
import com.aiyolo.service.api.request.ListUserRequest;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.response.AppUserObject;
import com.aiyolo.service.api.response.ListUserResponse;
import com.aiyolo.service.api.response.Response;
import com.aiyolo.service.api.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ListUserService extends BaseService {

    @Autowired
    AppUserGatewayRepository appUserGatewayRepository;

    @Override
    @SuppressWarnings("unchecked")
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        String userId = authenticate(request);

        ListUserRequest listUserRequest = (ListUserRequest) request;
        String imei = listUserRequest.getImei();
        if (StringUtils.isEmpty(imei)) {
            return (Res) responseRequestParameterError(request.getAction());
        }

        AppUserGateway appUserGateway = appUserGatewayRepository.findOneByUserIdAndGlImei(userId, imei);
        if (appUserGateway == null || appUserGateway.getGateway() == null) {
            return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "未找到网关");
        } else if (!appUserGateway.getRole().equals(Role4GatewayEnum.MANAGER)) {
            return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_AUTHORITY.getResult(), "无权限");
        }

        List<AppUserGateway> gatewayAppUsers = appUserGatewayRepository.findByGlImei(imei);

        List<AppUserObject> users = new ArrayList<AppUserObject>();
        for (int i = 0; i < gatewayAppUsers.size(); i++) {
            AppUserObject appUserObject = new AppUserObject();
            appUserObject.setAdmin(gatewayAppUsers.get(i).getRole().getValue());

            if (gatewayAppUsers.get(i).getAppUser() != null) {
                appUserObject.setPhone(gatewayAppUsers.get(i).getAppUser().getPhone());
            }

            users.add(appUserObject);
        }

        return (Res) new ListUserResponse(request, users);
    }

}
