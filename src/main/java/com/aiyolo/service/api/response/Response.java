package com.aiyolo.service.api.response;

import com.aiyolo.constant.ApiResponseStateEnum;

public class Response extends ResponseObject {

    public Response() {
    }

    public Response(String action, ApiResponseStateEnum state) {
        super(action, state);
    }

    public Response(String action, Integer result, String error) {
        super(action, result, error);
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
