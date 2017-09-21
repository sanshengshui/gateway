package com.aiyolo.service.api;

import com.aiyolo.common.ArrayHelper;
import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.constant.Role4GatewayEnum;
import com.aiyolo.entity.AppUserGateway;
import com.aiyolo.entity.Gateway;
import com.aiyolo.repository.AppUserGatewayRepository;
import com.aiyolo.repository.GatewayRepository;
import com.aiyolo.service.api.request.InputInfoRequest;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.response.Response;
import com.aiyolo.service.api.response.ResponseObject;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class InputInfoService extends BaseService {

    @Autowired
    GatewayRepository gatewayRepository;
    @Autowired
    AppUserGatewayRepository appUserGatewayRepository;

    @Override
    @SuppressWarnings("unchecked")
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        String userId = authenticate(request);

        InputInfoRequest inputInfoRequest = (InputInfoRequest) request;
        String imei = inputInfoRequest.getImei();
        if (StringUtils.isEmpty(imei)) {
            return (Res) responseRequestParameterError(request.getAction());
        }

        AppUserGateway appUserGateway = appUserGatewayRepository.findOneByUserIdAndGlImei(userId, imei);
        if (appUserGateway == null || appUserGateway.getGateway() == null) {
            return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "未找到网关");
        } else if (!appUserGateway.getRole().equals(Role4GatewayEnum.MANAGER)) {
            return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_AUTHORITY.getResult(), "无权限");
        } else {
            Gateway gateway = appUserGateway.getGateway();
            gateway.setGlName(inputInfoRequest.getGlName());
            gateway.setUserName(inputInfoRequest.getUserName());
            gateway.setUserPhones(JSONArray.fromObject(ArrayHelper.removeDuplicateElement(inputInfoRequest.getPhones())).toString());
            gateway.setAreaCode(inputInfoRequest.getAreaCode());
            gateway.setAddress(inputInfoRequest.getAddress());

            gatewayRepository.save(gateway);

            return (Res) responseSuccess(request.getAction());
        }
    }

}
