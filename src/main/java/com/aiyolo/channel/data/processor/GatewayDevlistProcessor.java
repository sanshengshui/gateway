package com.aiyolo.channel.data.processor;

import com.aiyolo.channel.data.response.GatewayDevlistResponse;
import com.aiyolo.common.SpringUtil;
import com.aiyolo.entity.Device;
import com.aiyolo.queue.Sender;
import com.aiyolo.repository.DeviceRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.Map;

public class GatewayDevlistProcessor extends Processor {

    private static Log gatewayLogger = LogFactory.getLog("gatewayLog");

    @Override
    public void run(String message) {
        try {
            init(message);

            // 应答
            Sender sender = (Sender) SpringUtil.getBean("sender");

            DeviceRepository deviceRepository = (DeviceRepository) SpringUtil.getBean("deviceRepository");
            List<Device> devices = deviceRepository.findByGlImei(messageBodyJson.getString("imei"));

            Map<String, Object> resHeaderMap = GatewayDevlistResponse.getInstance().responseHeader(messageHeaderJson.getString("gl_id"));
            Map<String, Object> resBodyMap = GatewayDevlistResponse.getInstance().responseBody(messageJson, devices);

            sender.sendMessage(resHeaderMap, resBodyMap);

            // 写入文件待后续处理
            gatewayLogger.info(message);
        } catch (Exception e) {
            errorLogger.error("GatewayDevlistProcessor异常！message:" + message, e);
        }
    }

}
