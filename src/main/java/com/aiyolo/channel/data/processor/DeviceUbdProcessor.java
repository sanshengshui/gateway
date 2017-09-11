package com.aiyolo.channel.data.processor;

import java.util.List;

import com.aiyolo.common.SpringUtil;
import com.aiyolo.entity.AppUserDevice;
import com.aiyolo.entity.Device;
import com.aiyolo.repository.AppUserDeviceRepository;
import com.aiyolo.repository.DeviceRepository;
import com.aiyolo.service.DeviceService;
import com.aiyolo.service.NoticeWarningService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DeviceUbdProcessor extends Processor {

    private static Log deviceUbdLogger = LogFactory.getLog("deviceUbdLog");

    @Override
    public void run(String message) {
        try {
            init(message);

            String imei = messageBodyJson.getString("imei");

            DeviceService deviceService = (DeviceService) SpringUtil.getBean("deviceService");
            List<AppUserDevice> deviceAppUsers = deviceService.getDeviceAppUsers(imei);
            if (deviceAppUsers != null) {
                AppUserDeviceRepository appUserDeviceRepository = (AppUserDeviceRepository) SpringUtil.getBean("appUserDeviceRepository");
                appUserDeviceRepository.delete(deviceAppUsers);
            }

            DeviceRepository deviceRepository = (DeviceRepository) SpringUtil.getBean("deviceRepository");
            Device device = deviceRepository.findFirstByGlImeiOrderByIdDesc(imei);
            if (device != null) {
                device.setGlName("");
                device.setUserName("");
                device.setUserPhones("");
                device.setAreaCode("");
                device.setAddress("");
                device.setGlLongitude("");
                device.setGlLatitude("");
                deviceRepository.save(device);
            }

            // 推送给app
            NoticeWarningService noticeWarningService = (NoticeWarningService) SpringUtil.getBean("noticeWarningService");
            noticeWarningService.pushDeviceUbd(device);

            // 写入文件待后续处理
            deviceUbdLogger.info(message);
        } catch (Exception e) {
            errorLogger.error("DeviceUbdProcessor异常！message:" + message, e);
        }
    }

}
