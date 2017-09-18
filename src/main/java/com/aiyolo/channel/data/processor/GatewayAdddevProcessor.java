package com.aiyolo.channel.data.processor;

import com.aiyolo.common.SpringUtil;
import com.aiyolo.entity.Device;
import com.aiyolo.repository.DeviceRepository;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GatewayAdddevProcessor extends Processor {

    private static Log gatewayLogger = LogFactory.getLog("gatewayLog");

    @Override
    public void run(String message) {
        try {
            init(message);

            DeviceRepository deviceRepository = (DeviceRepository) SpringUtil.getBean("deviceRepository");

            JSONArray devices = messageBodyJson.getJSONArray("devs");

            for (int i = 0; i < devices.size(); i++) {
                JSONObject device = devices.getJSONObject(i);

                deviceRepository.save(new Device(
                        device.getString("dev"),
                        device.getString("pid"),
                        device.getString("imei"),
                        messageBodyJson.getString("imei")));
            }

            // 写入文件待后续处理
            gatewayLogger.info(message);
        } catch (Exception e) {
            errorLogger.error("GatewayAdddevProcessor异常！message:" + message, e);
        }
    }

}
