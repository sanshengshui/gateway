package com.aiyolo.service.api;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.constant.Role4GatewayEnum;
import com.aiyolo.entity.AppUser;
import com.aiyolo.entity.AppUserGateway;
import com.aiyolo.repository.AppUserGatewayRepository;
import com.aiyolo.repository.AppUserRepository;
import com.aiyolo.service.api.request.DeleteGatewayRequest;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.response.Response;
import com.aiyolo.service.api.response.ResponseObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeleteGatewayService extends BaseService {

    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    AppUserGatewayRepository appUserGatewayRepository;

    @Override
    @SuppressWarnings("unchecked")
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        String userId = authenticate(request);

        DeleteGatewayRequest deleteGatewayRequest = (DeleteGatewayRequest) request;
        String imei = deleteGatewayRequest.getImei();
        String phone = deleteGatewayRequest.getPhone();
        if (StringUtils.isEmpty(imei)) {
            return (Res) responseRequestParameterError(request.getAction());
        }

        AppUserGateway appUserGateway = appUserGatewayRepository.findOneByUserIdAndGlImei(userId, imei);
        if (appUserGateway == null) {
            return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "未找到绑定关系");
        }

        if (!appUserGateway.getRole().equals(Role4GatewayEnum.MANAGER) && StringUtils.isEmpty(phone)) { // 非管理员请求，直接解绑
            appUserGatewayRepository.delete(appUserGateway);
        } else { // 管理员请求，只有网关无其他绑定用户时才可解绑自己
            if (!appUserGateway.getRole().equals(Role4GatewayEnum.MANAGER)) {
                return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "非管理员");
            }

            Boolean deleteAdmin = false; // 是否解绑自己（管理员）
            if (StringUtils.isNotEmpty(phone)) {
                AppUser appUser = appUserRepository.findFirstByPhoneOrderByIdDesc(phone);
                if (appUser == null) {
                    return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "未找到手机号对应的用户");
                }
                if (appUser.getUserId().equals(userId)) {
                    deleteAdmin = true; // 是解绑自己（管理员）
                } else {
                    AppUserGateway _appUserGateway = appUserGatewayRepository.findOneByUserIdAndGlImei(appUser.getUserId(), imei);
                    if (_appUserGateway == null) {
                        return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "未找到绑定关系");
                    } else {
                        appUserGatewayRepository.delete(_appUserGateway); // 解绑非管理员
                    }
                }
            }

            if (deleteAdmin || StringUtils.isEmpty(phone)) { // 解绑自己（管理员）
                // 只有网关无其他绑定用户时才可解绑自己
                List<AppUserGateway> gatewayAppUsers = appUserGatewayRepository.findByGlImei(imei);
                if (gatewayAppUsers.size() > 1) {
                    return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "请先转让管理员，或删除所有成员，才可解绑");
                } else {
                    appUserGatewayRepository.delete(appUserGateway); // 解绑管理员
                }
            }
        }

        return (Res) responseSuccess(request.getAction());
    }

}
