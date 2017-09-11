package com.aiyolo.service.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.aiyolo.entity.AppUserDevice;
import com.aiyolo.entity.DeviceAlarm;
import com.aiyolo.repository.AppUserDeviceRepository;
import com.aiyolo.repository.DeviceAlarmRepository;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.response.DeviceAlarmObject;
import com.aiyolo.service.api.response.NoticeListResponse;
import com.aiyolo.service.api.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeListService extends BaseService {

    @Autowired AppUserDeviceRepository appUserDeviceRepository;
    @Autowired DeviceAlarmRepository deviceAlarmRepository;

    @Override
    @SuppressWarnings("unchecked")
    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {
        String userId = authenticate(request);

        List<AppUserDevice> appUserDevices = appUserDeviceRepository.findByUserId(userId);

        List<DeviceAlarmObject> notices = new ArrayList<DeviceAlarmObject>();
        for (int i = 0; i < appUserDevices.size(); i++) {
            List<DeviceAlarm> deviceAlarms = deviceAlarmRepository.findByGlImeiOrderByIdDesc(appUserDevices.get(i).getGlImei());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            for (int j = 0; j < deviceAlarms.size(); j++) {
                DeviceAlarmObject deviceAlarmObject = new DeviceAlarmObject();
                deviceAlarmObject.setImei(deviceAlarms.get(j).getGlImei());
                deviceAlarmObject.setAlarmType(deviceAlarms.get(j).getType());
                deviceAlarmObject.setSolve(deviceAlarms.get(j).getStatus() == 0 ? true : false);
                deviceAlarmObject.setTime(format.format(deviceAlarms.get(j).getTimestamp() * 1000L));

                if (appUserDevices.get(i).getDevice() != null) {
                    deviceAlarmObject.setGlName(appUserDevices.get(i).getDevice().getGlName());
                }

                notices.add(deviceAlarmObject);
            }
        }
        Collections.sort(notices, new TimeComparator());

        return (Res) new NoticeListResponse(request, notices);
    }

    static class TimeComparator implements Comparator<Object> {

        @Override
        public int compare(Object object1, Object object2) {
            DeviceAlarmObject p1 = (DeviceAlarmObject) object1;
            DeviceAlarmObject p2 = (DeviceAlarmObject) object2;

            return p2.getTime().compareTo(p1.getTime());
        }

    }

}
