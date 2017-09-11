package com.aiyolo.service.api;

import java.util.HashMap;
import java.util.Map;

import com.aiyolo.cache.DeviceIOSetCache;
import com.aiyolo.channel.data.request.DeviceIOSetRequest;
import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.constant.ChannelConsts;
import com.aiyolo.constant.DeviceNetStatusEnum;
import com.aiyolo.entity.AppUserDevice;
import com.aiyolo.entity.Device;
import com.aiyolo.queue.Sender;
import com.aiyolo.repository.AppUserDeviceRepository;
import com.aiyolo.service.DeviceStatusService;
import com.aiyolo.service.api.request.CtrlDeviceRequest;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.response.Response;
import com.aiyolo.service.api.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CtrlDeviceService extends BaseService {

    @Autowired DeviceIOSetCache deviceIOSetCache;

    @Autowired AppUserDeviceRepository appUserDeviceRepository;

    @Autowired DeviceStatusService deviceStatusService;

    @Autowired Sender sender;

    @Override
    @SuppressWarnings("unchecked")
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        String userId = authenticate(request);

        CtrlDeviceRequest ctrlDeviceRequest = (CtrlDeviceRequest) request;
        String imei = ctrlDeviceRequest.getImei();
        if (StringUtils.isEmpty(imei)) {
            return (Res) responseRequestParameterError(request.getAction());
        }

        AppUserDevice appUserDevice = appUserDeviceRepository.findOneByUserIdAndGlImei(userId, imei);
        if (appUserDevice == null || appUserDevice.getDevice() == null) {
            return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_REQUEST_PARAMETER.getResult(), "未找到设备");
        } else {
            // 判断设备是否离线
            Integer deviceNetStatus = deviceStatusService.getDeviceNetStatus(imei);
            if (DeviceNetStatusEnum.OFFLINE.getValue().equals(deviceNetStatus)) {
                return (Res) responseDeviceOfflineError(request.getAction());
            }

            Device device = appUserDevice.getDevice();
            Map<String, Object> headerMap = DeviceIOSetRequest.getInstance().requestHeader(device.getGlId());

            Map<String, Object> queryParamMap = new HashMap<String, Object>();
            queryParamMap.put("imei", imei);
            if (!StringUtils.isEmpty(ctrlDeviceRequest.getWarnVoice())) {
                queryParamMap.put("voi", ctrlDeviceRequest.getWarnVoice());
            }
            if (!StringUtils.isEmpty(ctrlDeviceRequest.getGasSwitch())) {
                queryParamMap.put("swi", ctrlDeviceRequest.getGasSwitch());
            }
            Map<String, Object> bodyMap = DeviceIOSetRequest.getInstance().requestBody(queryParamMap);

            // 初始化响应结果
            int IOSetResult = deviceIOSetCache.getByImei(imei);
            if (IOSetResult != DeviceIOSetCache.RESULT_EMPTY) {
                deviceIOSetCache.save(imei, DeviceIOSetCache.RESULT_EMPTY);
            }

            sender.sendMessage(headerMap, bodyMap);

            // 同步等待设备响应
            int i = 0;
            while (i < (ChannelConsts.DEVICE_RESPONSE_TIMEOUT / ChannelConsts.DEVICE_RESPONSE_READ_INTERVAL)) {
                Thread.sleep(ChannelConsts.DEVICE_RESPONSE_READ_INTERVAL);
                IOSetResult = deviceIOSetCache.getByImei(imei);
                switch (IOSetResult) {
                    case DeviceIOSetCache.RESULT_EMPTY:
                        i++;
                        break;
                    case DeviceIOSetCache.RESULT_SUCCESS:
                        return (Res) responseSuccess(request.getAction());
                    case DeviceIOSetCache.RESULT_FAILED:
                        return (Res) new Response(request.getAction(), ApiResponseStateEnum.ERROR_BUSINESS.getResult(), "设置失败");
                    default:
                        return (Res) responseUnkownError(request.getAction());
                }
            }

            return (Res) responseDeviceNoneResponseError(request.getAction());
        }
    }

}
