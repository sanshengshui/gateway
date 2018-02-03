package com.aiyolo.service.api;

import com.aiyolo.common.SpringUtil;
import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.constant.AppNoticeTypeConsts;
import com.aiyolo.constant.Role4GatewayEnum;
import com.aiyolo.entity.AppUser;
import com.aiyolo.entity.AppUserGateway;
import com.aiyolo.entity.Gateway;
import com.aiyolo.repository.AppUserGatewayRepository;
import com.aiyolo.repository.AppUserRepository;
import com.aiyolo.repository.AppUserSessionRepository;
import com.aiyolo.service.GatewayStatusService;
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
    @Autowired
    GatewayStatusService gatewayStatusService;
    @Autowired
    AppUserSessionRepository appUserSessionRepository;

    @Override
    @SuppressWarnings("unchecked")
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        String userId = authenticate(request);

        DeleteGatewayRequest deleteGatewayRequest = (DeleteGatewayRequest) request;
        String imei = deleteGatewayRequest.getImei();
        String phone = deleteGatewayRequest.getPhone();
        String action = request.getAction();
        if (StringUtils.isEmpty(imei)) {
            return (Res) responseRequestParameterError(action);
        }

        AppUserGateway appUserGateway = appUserGatewayRepository.findOneByUserIdAndGlImei(userId, imei);
        Integer errorParameterResult = ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult();
        if (appUserGateway == null) {
            return (Res) new Response(action, errorParameterResult, "未找到绑定关系");
        }

        boolean isAdmin = appUserGateway.getRole().equals(Role4GatewayEnum.MANAGER);

        if (StringUtils.isEmpty(phone)){
            //请求解绑自己
            if (isAdmin && appUserGatewayRepository.findByGlImei(imei).size() > 1){
                //管理员需要判断网关是否还有其他用户
                return (Res) new Response(action, errorParameterResult, "请先转让管理员，或删除所有成员，才可解绑");
            }

        }else if (isAdmin){
            //请求解绑别人,管理员才可以操作
            AppUser appUser = appUserRepository.findFirstByPhoneOrderByIdDesc(phone);
            if (appUser == null) {
                return (Res) new Response(action, errorParameterResult, "未找到手机号对应的用户");
            }
            appUserGateway = appUserGatewayRepository.findOneByUserIdAndGlImei(appUser.getUserId(), imei);
            if (appUserGateway == null) {
                return (Res) new Response(action, errorParameterResult, "未找到绑定关系");
            }



            // 推送给app
            String[] mobileIds = new String[1];
            mobileIds[0] = appUserSessionRepository.findFirstByUserIdOrderByIdDesc(
                    appUserGateway.getUserId()).getMobileId();
            gatewayStatusService.pushGatewayStatus(imei, AppNoticeTypeConsts.DELETE, mobileIds);
        }else {
            return (Res) new Response(action, errorParameterResult, "非管理员");
        }
        appUserGatewayRepository.delete(appUserGateway);
//
//        if (!isAdmin && StringUtils.isEmpty(phone)) { // 非管理员请求，直接解绑
//            appUserGatewayRepository.delete(appUserGateway);
//        } else { // 管理员请求，只有网关无其他绑定用户时才可解绑自己
//            if (!isAdmin) {
//                return (Res) new Response(action, errorParameterResult, "非管理员");
//            }
//
//            Boolean deleteAdmin = false; // 是否解绑自己（管理员）
//            if (StringUtils.isNotEmpty(phone)) {
//                AppUser appUser = appUserRepository.findFirstByPhoneOrderByIdDesc(phone);
//                if (appUser == null) {
//                    return (Res) new Response(action, errorParameterResult, "未找到手机号对应的用户");
//                }
//                if (appUser.getUserId().equals(userId)) {
//                    deleteAdmin = true; // 是解绑自己（管理员）
//                } else {
//                    AppUserGateway _appUserGateway = appUserGatewayRepository.findOneByUserIdAndGlImei(appUser.getUserId(), imei);
//                    if (_appUserGateway == null) {
//                        return (Res) new Response(action, errorParameterResult, "未找到绑定关系");
//                    } else {
//                        appUserGatewayRepository.delete(_appUserGateway); // 解绑非管理员
//                    }
//                }
//            }
//
//            if (deleteAdmin || StringUtils.isEmpty(phone)) { // 解绑自己（管理员）
//                // 只有网关无其他绑定用户时才可解绑自己
//                List<AppUserGateway> gatewayAppUsers = appUserGatewayRepository.findByGlImei(imei);
//                if (gatewayAppUsers.size() > 1) {
//                    return (Res) new Response(action, errorParameterResult, "请先转让管理员，或删除所有成员，才可解绑");
//                } else {
//                    appUserGatewayRepository.delete(appUserGateway); // 解绑管理员
//                }
//            }
//        }

        return (Res) responseSuccess(action);
    }

}
