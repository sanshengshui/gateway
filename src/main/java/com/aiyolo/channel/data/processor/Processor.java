package com.aiyolo.channel.data.processor;

import com.aiyolo.cache.DeviceBeatCache;
import com.aiyolo.cache.DeviceLiveStatusCache;
import com.aiyolo.common.InputDataHelper;
import com.aiyolo.common.SpringUtil;
import com.aiyolo.constant.InputDataTypeEnum;
import com.aiyolo.entity.Device;
import com.aiyolo.repository.DeviceRepository;
import com.aiyolo.service.DeviceService;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

public abstract class Processor {

    public static Log errorLogger = LogFactory.getLog("errorLog");

    public JSONObject messageJson;
    public JSONObject messageHeaderJson;
    public JSONObject messageBodyJson;

    public void init(String message) throws Exception {
        messageJson = JSONObject.fromObject(message);
        messageHeaderJson = messageJson.getJSONObject("header");
        messageBodyJson = messageJson.getJSONObject("body");

        InputDataTypeEnum dataType = InputDataHelper.getInputDataType(messageHeaderJson, messageBodyJson);

        switch (dataType) {
        case DEVICE_BEAT:
            // 检查设备存活状态
            DeviceLiveStatusCache deviceLiveStatusCache = (DeviceLiveStatusCache) SpringUtil.getBean("deviceLiveStatusCache");
            int deviceLiveStatus = deviceLiveStatusCache.getByGlId(messageHeaderJson.getString("gl_id"));
            if (deviceLiveStatus != DeviceLiveStatusCache.LIVE) {
                // 更新定位经纬度
                DeviceService deviceService = (DeviceService) SpringUtil.getBean("deviceService");
                deviceService.requestDeviceInfo(messageHeaderJson.getString("gl_id"));
            }

            break;
        case DEVICE_PUSH:
            if (StringUtils.isEmpty(messageHeaderJson.getString("gl_id")) ||
                    StringUtils.isEmpty(messageBodyJson.getString("imei")) ||
                    StringUtils.isEmpty(messageBodyJson.getString("pin"))) {
                throw new RuntimeException("Device Push Params Error.");
            }

            DeviceService deviceService = (DeviceService) SpringUtil.getBean("deviceService");
            DeviceRepository deviceRepository = (DeviceRepository) SpringUtil.getBean("deviceRepository");

            // 查询该设备是否已入库
            Device device = deviceRepository.findFirstByGlIdOrderByIdDesc(messageHeaderJson.getString("gl_id"));
            if (device == null) {
                device = deviceRepository.save(new Device(
                        messageHeaderJson.getString("gl_id"),
                        messageBodyJson.getString("imei"),
                        messageBodyJson.getString("pin")));

                deviceService.requestDeviceInfo(messageHeaderJson.getString("gl_id"));
            } else {
                if (StringUtils.isEmpty(device.getGlLongitude()) || StringUtils.isEmpty(device.getGlLatitude())) {
                    deviceService.requestDeviceInfo(messageHeaderJson.getString("gl_id"));
                }
            }

            // 检查设备存活状态
            DeviceLiveStatusCache deviceLiveStatusCache = (DeviceLiveStatusCache) SpringUtil.getBean("deviceLiveStatusCache");
            int deviceLiveStatus = deviceLiveStatusCache.getByGlId(messageHeaderJson.getString("gl_id"));
            if (deviceLiveStatus != DeviceLiveStatusCache.LIVE) {
                // 记录一条心跳
                DeviceBeatCache deviceBeatCache = (DeviceBeatCache) SpringUtil.getBean("deviceBeatCache");
                deviceBeatCache.save(messageHeaderJson.getString("gl_id"), System.currentTimeMillis());
                // 更新存活状态
                deviceLiveStatusCache.save(messageHeaderJson.getString("gl_id"), DeviceLiveStatusCache.LIVE);
                // 更新定位经纬度
                deviceService.requestDeviceInfo(messageHeaderJson.getString("gl_id"));
            }

            break;
        case DEVICE_REQUEST:
        case DEVICE_RESPONSE:
            break;
        case APP_PUSH:
        case APP_REQUEST:
        case APP_RESPONSE:
            break;
        case CHANNEL_REQUEST:
        case CHANNEL_RESPONSE:
            break;
        }
    }

    public abstract void run(String message) throws Exception;

}
