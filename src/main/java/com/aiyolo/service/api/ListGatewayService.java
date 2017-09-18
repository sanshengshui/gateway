package com.aiyolo.service.api;

import com.aiyolo.entity.AppUserGateway;
import com.aiyolo.repository.AppUserGatewayRepository;
import com.aiyolo.service.GatewayStatusService;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.response.GatewayObject;
import com.aiyolo.service.api.response.ListGatewayResponse;
import com.aiyolo.service.api.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ListGatewayService extends BaseService {

    @Autowired AppUserGatewayRepository appUserGatewayRepository;

    @Autowired GatewayStatusService gatewayStatusService;

    @Override
    @SuppressWarnings("unchecked")
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        String userId = authenticate(request);

        List<AppUserGateway> appUserGateways = appUserGatewayRepository.findByUserId(userId);

        List<GatewayObject> gateways = new ArrayList<GatewayObject>();
        for (int i = 0; i < appUserGateways.size(); i++) {
            GatewayObject gatewayObject = new GatewayObject();
            gatewayObject.setImei(appUserGateways.get(i).getGlImei());
            gatewayObject.setAdmin(appUserGateways.get(i).getRole().getValue());

            gatewayObject.setOnline(1);
            gatewayObject.setDevNum(1);
            gatewayObject.setVer("1");

            if (appUserGateways.get(i).getGateway() != null) {
                gatewayObject.setGlName(appUserGateways.get(i).getGateway().getGlName());
            }

            gateways.add(gatewayObject);
        }

        return (Res) new ListGatewayResponse(request, gateways);
    }

}
