package com.aiyolo.channel.data.processor;

import com.aiyolo.common.SpringUtil;
import com.aiyolo.common.StringHelper;
import com.aiyolo.entity.Device;
import com.aiyolo.repository.DeviceRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DeviceInfoProcessor extends Processor {

    private static Log deviceInfoLogger = LogFactory.getLog("deviceInfoLog");

    @Override
    public void run(String message) {
        try {
            init(message);

            String logintime = messageHeaderJson.getString("logintime");
            String longitude = messageHeaderJson.getString("longitude");
            String latitude = messageHeaderJson.getString("latitude");

            if (StringUtils.isNotEmpty(logintime) && StringHelper.checkLocation(longitude, latitude)) {
                DeviceRepository deviceRepository = (DeviceRepository) SpringUtil.getBean("deviceRepository");

                // 查询该设备是否存在
                Device device = deviceRepository.findFirstByGlIdOrderByIdDesc(messageHeaderJson.getString("gl_id"));
                if (device != null) {
                    device.setGlLogintime(logintime);
                    device.setGlLongitude(longitude);
                    device.setGlLatitude(latitude);
                    deviceRepository.save(device);
                }
            }

            // 写入文件待后续处理
            deviceInfoLogger.info(message);
        } catch (Exception e) {
            errorLogger.error("DeviceInfoProcessor异常！message:" + message, e);
        }
    }

}
