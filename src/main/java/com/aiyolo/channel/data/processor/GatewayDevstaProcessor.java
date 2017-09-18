package com.aiyolo.channel.data.processor;

import com.aiyolo.common.SpringUtil;
import com.aiyolo.entity.DeviceStatus;
import com.aiyolo.repository.DeviceStatusRepository;
import com.aiyolo.service.DeviceStatusService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GatewayDevstaProcessor extends Processor {

    private static Log gatewayLogger = LogFactory.getLog("gatewayLog");

    @Override
    public void run(String message) {
        try {
            init(message);

            DeviceStatusRepository deviceStatusRepository = (DeviceStatusRepository) SpringUtil.getBean("deviceStatusRepository");

            DeviceStatusService deviceStatusService = (DeviceStatusService) SpringUtil.getBean("deviceStatusService");

            JSONArray deviceStatuses = messageBodyJson.getJSONArray("devs");

            for (int i = 0; i < deviceStatuses.size(); i++) {
                JSONObject deviceStatus = deviceStatuses.getJSONObject(i);

                deviceStatusRepository.save(new DeviceStatus(
                        deviceStatus.getString("dev"),
                        deviceStatus.getString("imei"),
                        messageBodyJson.getString("imei"),
                        messageBodyJson.getInt("mid"),
                        deviceStatus.getInt("online"),
                        deviceStatus.getInt("err"),
                        deviceStatus.getInt("rssi"),
                        deviceStatus.getInt("bat"),
                        deviceStatus.getString("info")));

                // 推送给app
                deviceStatusService.pushDeviceStatus(deviceStatus.getString("imei"));
            }

            // 写入文件待后续处理
            gatewayLogger.info(message);
        } catch (Exception e) {
            errorLogger.error("GatewayDevstaProcessor异常！message:" + message, e);
        }
    }

}
