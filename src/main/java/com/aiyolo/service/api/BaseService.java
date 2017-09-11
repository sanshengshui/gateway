package com.aiyolo.service.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.aiyolo.AuthenticateError;
import com.aiyolo.entity.AppUserSession;
import com.aiyolo.repository.AppUserSessionRepository;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.response.ResponseObject;

@Service
public class BaseService extends ApiService {

    @Autowired AppUserSessionRepository appUserSessionRepository;

    // 认证用户
    public <Req extends RequestObject> String authenticate(Req request) throws Exception {
        AppUserSession appUserSession = appUserSessionRepository.findFirstBySessionOrderByIdDesc(request.getSession());
        if (appUserSession != null) {
            return appUserSession.getUserId();
        } else {
            // 临时测试
            if ("ok".equals(request.getSession()) && !StringUtils.isEmpty(request.getCid())) {
                return request.getCid();
            }

            throw new AuthenticateError("Authenticate Error.");
        }
    }

    @Override
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        return null;
    }

}
