package com.aiyolo.channel.data.processor;

import java.text.SimpleDateFormat;
import java.util.Map;

import com.aiyolo.channel.data.response.DeviceStatusResponse;
import com.aiyolo.common.SpringUtil;
import com.aiyolo.entity.Device;
import com.aiyolo.entity.DeviceSetting;
import com.aiyolo.entity.DeviceStatus;
import com.aiyolo.queue.Sender;
import com.aiyolo.repository.DeviceRepository;
import com.aiyolo.repository.DeviceStatusRepository;
import com.aiyolo.service.DeviceSettingService;
import com.aiyolo.service.DeviceStatusService;
import com.aiyolo.service.NoticeWarningService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DeviceStatusProcessor extends Processor {

    private static Log deviceStatusLogger = LogFactory.getLog("deviceStatusLog");

    @Override
    public void run(String message) {
        try {
            init(message);

            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HH");
            String _dh = format.format(messageBodyJson.getInt("tsp") * 1000L);
            String[] _dhArray = _dh.split(" ");

            DeviceStatus deviceStatus = new DeviceStatus(
                    messageHeaderJson.getString("gl_id"),
                    messageBodyJson.getString("imei"),
                    messageBodyJson.getInt("tsp"),
                    _dhArray[0],
                    _dhArray[1],
                    messageBodyJson.getString("ver"),
                    messageBodyJson.getInt("tmp"),
                    messageBodyJson.getInt("hum"),
                    messageBodyJson.getInt("net"),
                    messageBodyJson.getInt("dev"));

            DeviceStatusRepository deviceStatusRepository = (DeviceStatusRepository) SpringUtil.getBean("deviceStatusRepository");
            deviceStatusRepository.save(deviceStatus);

            // 应答
            Sender sender = (Sender) SpringUtil.getBean("sender");

            DeviceRepository deviceRepository = (DeviceRepository) SpringUtil.getBean("deviceRepository");
            Device device = deviceRepository.findFirstByGlIdOrderByIdDesc(messageHeaderJson.getString("gl_id"));

            DeviceSettingService deviceSettingService = (DeviceSettingService) SpringUtil.getBean("deviceSettingService");
            DeviceSetting deviceSetting = deviceSettingService.getDeviceSetting(device);

            Map<String, Object> resHeaderMap = DeviceStatusResponse.getInstance().responseHeader(messageHeaderJson.getString("gl_id"));
            Map<String, Object> resBodyMap = DeviceStatusResponse.getInstance().responseBody(messageJson, deviceSetting);

            sender.sendMessage(resHeaderMap, resBodyMap);

            // 更新设备状态缓存
            DeviceStatusService deviceStatusService = (DeviceStatusService) SpringUtil.getBean("deviceStatusService");
            deviceStatusService.updateLatestDeviceStatus(deviceStatus);

            // 推送给app
            NoticeWarningService noticeWarningService = (NoticeWarningService) SpringUtil.getBean("noticeWarningService");
            noticeWarningService.pushDeviceStatus(device);

            // 写入文件待后续处理
            deviceStatusLogger.info(message);
        } catch (Exception e) {
            errorLogger.error("DeviceStatusProcessor异常！message:" + message, e);
        }
    }

}
