package com.aiyolo.service.api;

import com.aiyolo.channel.data.request.GatewayPairRequest;
import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.entity.AppUserGateway;
import com.aiyolo.queue.Sender;
import com.aiyolo.repository.AppUserGatewayRepository;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.request.SetPairRequest;
import com.aiyolo.service.api.response.Response;
import com.aiyolo.service.api.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static com.aiyolo.constant.ProtocolFieldConsts.IMEI;
import static com.aiyolo.constant.ProtocolFieldConsts.PIN;

@Service
public class SetPairService extends BaseService {

    @Autowired
    Sender sender;

    @Autowired
    AppUserGatewayRepository appUserGatewayRepository;

    @Override
    @SuppressWarnings("unchecked")
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        String userId = authenticate(request);

        SetPairRequest setPairRequest = (SetPairRequest) request;
        String glImei = setPairRequest.getImei();
        if (StringUtils.isEmpty(glImei)) {
            return (Res) responseRequestParameterError(request.getAction());
        }

        AppUserGateway appUserGateway = appUserGatewayRepository.findOneByUserIdAndGlImei(userId, glImei);
        if (appUserGateway == null || appUserGateway.getGateway() == null) {
            return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "未找到网关");
        } else {
            // 下发组网配对模式
            Map<String, Object> headerMap = GatewayPairRequest.getInstance().requestHeader(glImei);

            Map<String, Object> data = new HashMap<String, Object>();
            data.put(IMEI, glImei);
            data.put(PIN, appUserGateway.getGateway().getGlPin());
            Map<String, Object> bodyMap = GatewayPairRequest.getInstance().requestBody(data);

            sender.sendMessage(headerMap, bodyMap);

            return (Res) responseSuccess(request.getAction());
        }
    }

}
