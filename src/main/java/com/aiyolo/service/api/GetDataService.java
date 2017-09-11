package com.aiyolo.service.api;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aiyolo.cache.entity.DeviceLatestStatus;
import com.aiyolo.entity.DeviceStatus;
import com.aiyolo.repository.DeviceStatusRepository;
import com.aiyolo.service.DeviceStatusService;
import com.aiyolo.service.api.request.GetDataRequest;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.response.GetDataResponse;
import com.aiyolo.service.api.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class GetDataService extends BaseService {

    @Autowired DeviceStatusRepository deviceStatusRepository;

    @Autowired DeviceStatusService deviceStatusService;

    @Override
    @SuppressWarnings("unchecked")
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        authenticate(request);

        GetDataRequest getDataRequest = (GetDataRequest) request;
        String imei = getDataRequest.getImei();
        if (StringUtils.isEmpty(imei)) {
            return (Res) responseRequestParameterError(request.getAction());
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HH");
        String _dh = format.format(System.currentTimeMillis());
        String[] _dhArray = _dh.split(" ");
        String currentDate = _dhArray[0];
        Integer currentHour = Integer.parseInt(_dhArray[1]);

        // 查询设备上报状态
        List<DeviceStatus> deviceStatuses = deviceStatusRepository.findByGlImeiDate(imei, currentDate);

        Integer[] resTempArray = new Integer[currentHour * 2];
        Arrays.fill(resTempArray, -4000);
        Integer[] resHumArray = new Integer[currentHour * 2];
        Arrays.fill(resHumArray, 0);

        if (deviceStatuses != null) {
            for (int i = 0; i < deviceStatuses.size(); i++) {
                DeviceStatus _deviceStatus = deviceStatuses.get(i);
                Integer _hour = Integer.parseInt(_deviceStatus.getHour());
                if (_hour < currentHour) {
                    if (resTempArray[_hour * 2] == -4000 && resHumArray[_hour * 2] == 0) {
                        resTempArray[_hour * 2] = _deviceStatus.getTemperature();
                        resHumArray[_hour * 2] = _deviceStatus.getHumidity();
                    } else {
                        resTempArray[_hour * 2 + 1] = _deviceStatus.getTemperature();
                        resHumArray[_hour * 2 + 1] = _deviceStatus.getHumidity();
                    }
                }
            }
        }

        Map<String, Object> deviceData = new HashMap<String, Object>();
        deviceData.put("imei", imei);
        deviceData.put("temps", resTempArray);
        deviceData.put("hums", resHumArray);

        DeviceLatestStatus latestDeviceStatus = deviceStatusService.getLatestDeviceStatusByGlImei(imei);
        deviceData.put("netStatus", latestDeviceStatus.getNetStatus());
        deviceData.put("devStatus", latestDeviceStatus.getDevStatus());
        deviceData.put("alarmType", latestDeviceStatus.getAlarmType());
        deviceData.put("temp", latestDeviceStatus.getTemperature());
        deviceData.put("hum", latestDeviceStatus.getHumidity());

        return (Res) new GetDataResponse(request, deviceData);
    }

}
