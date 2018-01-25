package com.aiyolo.service.api;

import com.aiyolo.cache.GatewayLiveStatusCache;
import com.aiyolo.entity.AppUserGateway;
import com.aiyolo.entity.Device;
import com.aiyolo.entity.GatewayStatus;
import com.aiyolo.repository.AppUserGatewayRepository;
import com.aiyolo.repository.DeviceRepository;
import com.aiyolo.repository.GatewayStatusRepository;
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

    @Autowired
    GatewayLiveStatusCache gatewayLiveStatusCache;

    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    GatewayStatusRepository gatewayStatusRepository;
    @Autowired
    AppUserGatewayRepository appUserGatewayRepository;

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

            if (appUserGateways.get(i).getGateway() != null) {
                int gatewayLiveStatus = gatewayLiveStatusCache.getByGlImei(appUserGateways.get(i).getGateway().getGlImei());
                gatewayObject.setOnline(gatewayLiveStatus);
                gatewayObject.setGlName(appUserGateways.get(i).getGateway().getGlName());
            }

            GatewayStatus gatewayStatus = gatewayStatusRepository.findFirstByGlImeiOrderByIdDesc(appUserGateways.get(i).getGlImei());
            if (gatewayStatus != null) {
                gatewayObject.setVer(gatewayStatus.getVersion());
                gatewayObject.setErr(gatewayStatus.getStatus());
            }

            List<Device> devices = deviceRepository.findByGlImei(appUserGateways.get(i).getGlImei());
            gatewayObject.setDevNum(devices.size());

            gateways.add(gatewayObject);
        }

        return (Res) new ListGatewayResponse(request, gateways);
    }

}
