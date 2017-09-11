package com.aiyolo.service.api;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.aiyolo.common.GaodeMapHelper;
import com.aiyolo.service.api.request.LocalInfoRequest;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.response.LocalInfoResponse;
import com.aiyolo.service.api.response.ResponseObject;

@Service
public class LocalInfoService extends BaseService {

    @Override
    @SuppressWarnings("unchecked")
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        authenticate(request);

        LocalInfoRequest localInfoRequest = (LocalInfoRequest) request;
        Double latitude = localInfoRequest.getLatitude();
        Double longitude = localInfoRequest.getLongitude();
        if (latitude == null || longitude == null) {
            return (Res) responseRequestParameterError(request.getAction());
        }

        Map<String, String> localInfo = GaodeMapHelper.getWeatherByLocation(Double.toString(longitude), Double.toString(latitude));
        if (localInfo != null) {
            return (Res) new LocalInfoResponse(request, localInfo);
        } else {
            return (Res) responseBusinessError(request.getAction());
        }
    }

}
