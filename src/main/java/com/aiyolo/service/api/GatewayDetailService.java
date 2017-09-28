package com.aiyolo.service.api;

import com.aiyolo.cache.GatewayLiveStatusCache;
import com.aiyolo.constant.AlarmStatusEnum;
import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.entity.*;
import com.aiyolo.repository.*;
import com.aiyolo.service.api.request.GatewayDetailRequest;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.response.DeviceObject;
import com.aiyolo.service.api.response.GatewayDetailResponse;
import com.aiyolo.service.api.response.Response;
import com.aiyolo.service.api.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class GatewayDetailService extends BaseService {

    @Autowired
    GatewayLiveStatusCache gatewayLiveStatusCache;

    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    DeviceStatusRepository deviceStatusRepository;
    @Autowired
    DeviceAlarmRepository deviceAlarmRepository;
    @Autowired
    GatewayStatusRepository gatewayStatusRepository;
    @Autowired
    AppUserGatewayRepository appUserGatewayRepository;

    @Override
    @SuppressWarnings("unchecked")
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        String userId = authenticate(request);

        GatewayDetailRequest gatewayDetailRequest = (GatewayDetailRequest) request;
        String imei = gatewayDetailRequest.getImei();
        if (StringUtils.isEmpty(imei)) {
            return (Res) responseRequestParameterError(request.getAction());
        }

        AppUserGateway appUserGateway = appUserGatewayRepository.findOneByUserIdAndGlImei(userId, imei);
        if (appUserGateway == null || appUserGateway.getGateway() == null) {
            return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "未找到网关");
        }

        GatewayDetailResponse gatewayDetailResponse = new GatewayDetailResponse(request);
        gatewayDetailResponse.setImei(imei);

        GatewayStatus gatewayStatus = gatewayStatusRepository.findFirstByGlImeiOrderByIdDesc(imei);
        if (gatewayStatus != null) {
            gatewayDetailResponse.setErr(gatewayStatus.getStatus());
            gatewayDetailResponse.setTemp(gatewayStatus.getTemperature());
            gatewayDetailResponse.setHum(gatewayStatus.getHumidity());
            gatewayDetailResponse.setAtm(gatewayStatus.getAtmosphere());
            gatewayDetailResponse.setVer(gatewayStatus.getVersion());
        }

        int gatewayLiveStatus = gatewayLiveStatusCache.getByGlId(appUserGateway.getGateway().getGlId());
        gatewayDetailResponse.setOnline(gatewayLiveStatus);

        List<Device> devices = deviceRepository.findByGlImei(imei);
        List<DeviceObject> devs = new ArrayList<DeviceObject>();
        for (int i = 0; i < devices.size(); i++) {
            DeviceObject dev = new DeviceObject();
            dev.setDev(devices.get(i).getType());
            dev.setPid(devices.get(i).getPid());
            dev.setPosition(devices.get(i).getPosition());
            dev.setName(devices.get(i).getName());
            dev.setImei(devices.get(i).getImei());

            DeviceStatus deviceStatus = deviceStatusRepository.findFirstByImeiOrderByIdDesc(devices.get(i).getImei());
            if (deviceStatus != null) {
                dev.setOnline(deviceStatus.getOnline());
                dev.setRssi(deviceStatus.getRssi());
                dev.setErr(deviceStatus.getStatus());
                dev.setBat(deviceStatus.getBat());
            }

            DeviceAlarm deviceAlarm = deviceAlarmRepository.findFirstByImeiOrderByIdDesc(devices.get(i).getImei());
            if (deviceAlarm != null) {
                dev.setVal(AlarmStatusEnum.CLEAR.getValue().equals(deviceAlarm.getStatus()) ? AlarmStatusEnum.CLEAR.getValue() : deviceAlarm.getValue());
            }

            devs.add(dev);
        }
        gatewayDetailResponse.setDevs(devs);

        return (Res) gatewayDetailResponse;
    }

}
