package com.aiyolo.service.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aiyolo.entity.AppUser;
import com.aiyolo.repository.AppUserRepository;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.response.GetUserInfoResponse;
import com.aiyolo.service.api.response.ResponseObject;

@Service
public class GetUserInfoService extends BaseService {

    @Autowired
    AppUserRepository appUserRepository;

    @Override
    @SuppressWarnings("unchecked")
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        String userId = authenticate(request);

        AppUser appUser = appUserRepository.findFirstByUserIdOrderByIdDesc(userId);
        return (Res) new GetUserInfoResponse(request, appUser);
    }

}
