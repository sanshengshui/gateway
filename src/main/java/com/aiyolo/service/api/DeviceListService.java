package com.aiyolo.service.api;

import java.util.ArrayList;
import java.util.List;

import com.aiyolo.cache.entity.DeviceLatestStatus;
import com.aiyolo.entity.AppUserDevice;
import com.aiyolo.repository.AppUserDeviceRepository;
import com.aiyolo.service.DeviceStatusService;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.response.DeviceListResponse;
import com.aiyolo.service.api.response.DeviceObject;
import com.aiyolo.service.api.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceListService extends BaseService {

    @Autowired AppUserDeviceRepository appUserDeviceRepository;

    @Autowired DeviceStatusService deviceStatusService;

    @Override
    @SuppressWarnings("unchecked")
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        String userId = authenticate(request);

        List<AppUserDevice> appUserDevices = appUserDeviceRepository.findByUserId(userId);

        List<DeviceObject> devices = new ArrayList<DeviceObject>();
        for (int i = 0; i < appUserDevices.size(); i++) {
            DeviceObject deviceObject = new DeviceObject();
            deviceObject.setImei(appUserDevices.get(i).getGlImei());
            deviceObject.setAdmin(appUserDevices.get(i).getRole().getValue());

            DeviceLatestStatus latestDeviceStatus = deviceStatusService.getLatestDeviceStatusByGlImei(appUserDevices.get(i).getGlImei());
            deviceObject.setNetStatus(latestDeviceStatus.getNetStatus());
            deviceObject.setDevStatus(latestDeviceStatus.getDevStatus());
            deviceObject.setAlarmType(latestDeviceStatus.getAlarmType());
            deviceObject.setVersion(latestDeviceStatus.getVersion());

            if (appUserDevices.get(i).getDevice() != null) {
                deviceObject.setGlName(appUserDevices.get(i).getDevice().getGlName());
            }

            devices.add(deviceObject);
        }

        return (Res) new DeviceListResponse(request, devices);
    }

}
