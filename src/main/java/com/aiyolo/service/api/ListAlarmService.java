package com.aiyolo.service.api;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.entity.AppUserGateway;
import com.aiyolo.entity.Check;
import com.aiyolo.entity.Device;
import com.aiyolo.entity.DeviceAlarm;
import com.aiyolo.entity.Gateway;
import com.aiyolo.repository.AppUserGatewayRepository;
import com.aiyolo.repository.CheckRepository;
import com.aiyolo.repository.DeviceAlarmRepository;
import com.aiyolo.repository.DeviceRepository;
import com.aiyolo.repository.GatewayRepository;
import com.aiyolo.service.api.request.ListAlarmRequest;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.response.AlarmObject;
import com.aiyolo.service.api.response.ListAlarmResponse;
import com.aiyolo.service.api.response.Response;
import com.aiyolo.service.api.response.ResponseObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ListAlarmService extends BaseService {

    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    DeviceAlarmRepository deviceAlarmRepository;
    @Autowired
    AppUserGatewayRepository appUserGatewayRepository;

    @Autowired
    CheckRepository checkRepository;
    @Autowired
    GatewayRepository gatewayRepository;

    @Override
    @SuppressWarnings("unchecked")
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        String userId = authenticate(request);

        ListAlarmRequest listAlarmRequest = (ListAlarmRequest) request;
        String imei = listAlarmRequest.getImei();
        if (StringUtils.isEmpty(imei)) {
            return (Res) responseRequestParameterError(request.getAction());
        }
        String glImei;
        List<Check> checks;

        List<AlarmObject> alarms = new ArrayList<AlarmObject>();
        if (listAlarmRequest.getType() == 0) {
            //返回设备的日志
            Device device = deviceRepository.findFirstByImeiOrderByIdDesc(imei);
            if (device == null) {
                return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "未找到设备");
            }
            glImei = device.getGlImei();
            AppUserGateway appUserGateway = appUserGatewayRepository.findOneByUserIdAndGlImei(userId, glImei);
            if (appUserGateway == null || appUserGateway.getGateway() == null) {
                return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "未找到网关");
            }

            //报警日志
            List<DeviceAlarm> deviceAlarms = deviceAlarmRepository.findByImeiOrderByIdDesc(imei);
            for (int i = 0; i < deviceAlarms.size(); i++) {
                AlarmObject alarmObject = new AlarmObject();
                alarmObject.setTimeAlarm(deviceAlarms.get(i).getTimestamp() * 1000L);
                alarmObject.setTimeCut(0L);
                if (deviceAlarms.get(i).getStatus() == 0) {
                    alarmObject.setTimeRemove(deviceAlarms.get(i).getUpdatedAt().getTime());
                }
                alarmObject.setVal(deviceAlarms.get(i).getValue());
                alarmObject.setType(0);//0代表报警,1代表巡检
                alarms.add(alarmObject);
            }

            //巡检日志
            checks = checkRepository.findByImeiOrderByIdDesc(imei);
            for (Check check : checks) {
                AlarmObject alarmObject = new AlarmObject();
                alarmObject.setTimeAlarm(check.getTimestamp() * 1000L);
                alarmObject.setType(1);//0代表报警,1代表巡检
                alarms.add(alarmObject);
            }


        } else if (listAlarmRequest.getType() == 1) {
            //返回网关报警和巡检日志
            //            Gateway gateway = gatewayRepository.findFirstByGlImeiOrderByIdDesc(imei);
            //            if (gateway == null) {
            //                return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "未找到网关");
            //            }
            //            glImei = imei;
            //            AppUserGateway appUserGateway = appUserGatewayRepository.findOneByUserIdAndGlImei(userId, glImei);
            //            if (appUserGateway == null || appUserGateway.getGateway() == null) {
            //                return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "未找到网关");
            //            }


        } else {
            return (Res) responseRequestParameterError(request.getAction());
        }

        return (Res) new ListAlarmResponse(request, alarms);
    }

}
