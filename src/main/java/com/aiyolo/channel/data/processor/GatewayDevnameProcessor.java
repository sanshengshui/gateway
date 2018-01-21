package com.aiyolo.channel.data.processor;

import com.aiyolo.channel.data.response.GatewayDevnameResponse;
import com.aiyolo.common.SpringUtil;
import com.aiyolo.constant.ProtocolFieldConsts;
import com.aiyolo.entity.DeviceCategory;
import com.aiyolo.queue.Sender;
import com.aiyolo.repository.DeviceCategoryRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

public class GatewayDevnameProcessor extends Processor {

    private static Log gatewayLogger = LogFactory.getLog("gatewayLog");

    @Override
    public void run(String message) {
        try {
            init(message);

            // 应答
            Sender sender = (Sender) SpringUtil.getBean("sender");

            DeviceCategoryRepository deviceCategoryRepository = (DeviceCategoryRepository) SpringUtil.getBean("deviceCategoryRepository");
            DeviceCategory deviceCategory = deviceCategoryRepository.findOneByCategoryAndType(
                    messageBodyJson.getInt("cat"),
                    messageBodyJson.getInt("type"));

            Map<String, Object> resHeaderMap = GatewayDevnameResponse.getInstance().responseHeader(messageHeaderJson.getString(ProtocolFieldConsts.IMEI));
            Map<String, Object> resBodyMap = GatewayDevnameResponse.getInstance().responseBody(messageJson, deviceCategory);

            sender.sendMessage(resHeaderMap, resBodyMap);

            // 写入文件待后续处理
            gatewayLogger.info(message);
        } catch (Exception e) {
            errorLogger.error("GatewayDevnameProcessor异常！message:" + message, e);
        }
    }

}
