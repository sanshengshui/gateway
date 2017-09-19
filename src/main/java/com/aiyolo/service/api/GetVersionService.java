package com.aiyolo.service.api;

import com.aiyolo.entity.AppVersion;
import com.aiyolo.repository.AppVersionRepository;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.response.GetVersionResponse;
import com.aiyolo.service.api.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetVersionService extends BaseService {

    @Autowired
    AppVersionRepository appVersionRepository;

    @Override
    @SuppressWarnings("unchecked")
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        authenticate(request);

        AppVersion appVersion = appVersionRepository.findFirstBySystemOrderByIdDesc(request.getSystem());
        return (Res) new GetVersionResponse(request, appVersion);
    }

}
