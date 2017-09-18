package com.aiyolo.service.api;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.response.Response;
import com.aiyolo.service.api.response.ResponseObject;

public abstract class ApiService {

    public abstract <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception;

    public Response responseSuccess(String action) {
        return new Response(action, ApiResponseStateEnum.SUCCESS);
    }

    public Response responseInternalServerError(String action) {
        return new Response(action, ApiResponseStateEnum.ERROR_SERVER);
    }

    public Response responseBusinessError(String action) {
        return new Response(action, ApiResponseStateEnum.ERROR_BUSINESS);
    }

    public Response responseRequestParameterError() {
        return new Response("", ApiResponseStateEnum.ERROR_REQUEST_PARAMETER);
    }

    public Response responseRequestParameterError(String action) {
        return new Response(action, ApiResponseStateEnum.ERROR_REQUEST_PARAMETER);
    }

    public Response responseNoneDataError(String action) {
        return new Response(action, ApiResponseStateEnum.ERROR_NONE_DATA);
    }

    // 认证错误
    public Response responseAuthenticateError(String action) {
        return new Response(action, ApiResponseStateEnum.ERROR_AUTHENTICATE);
    }

    // 无权限
    public Response responseAuthorityError(String action) {
        return new Response(action, ApiResponseStateEnum.ERROR_AUTHORITY);
    }

    // 设备离线
    public Response responseDeviceOfflineError(String action) {
        return new Response(action, ApiResponseStateEnum.ERROR_GATEWAY_OFFLINE);
    }

    // 设备未响应
    public Response responseDeviceNoneResponseError(String action) {
        return new Response(action, ApiResponseStateEnum.ERROR_GATEWAY_NONE_RESPONSE);
    }

    // 未知错误
    public Response responseUnkownError(String action) {
        return new Response(action, ApiResponseStateEnum.ERROR_UNKOWN);
    }

}
