package com.aiyolo.channel.data.processor;

import com.aiyolo.common.SpringUtil;
import com.aiyolo.constant.AppNoticeTypeConsts;
import com.aiyolo.entity.Device;
import com.aiyolo.entity.DeviceStatus;
import com.aiyolo.repository.DeviceRepository;
import com.aiyolo.repository.DeviceStatusRepository;
import com.aiyolo.service.DeviceStatusService;
import com.aiyolo.service.GatewayStatusService;
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
            DeviceStatusService deviceStatusService = (DeviceStatusService) SpringUtil.getBean("deviceStatusService");
            DeviceStatusRepository deviceStatusRepository = (DeviceStatusRepository) SpringUtil.getBean("deviceStatusRepository");

            JSONArray devs = messageBodyJson.getJSONArray("devs");

            for (int i = 0; i < devs.size(); i++) {
                JSONObject dev = devs.getJSONObject(i);

                Device _device = deviceRepository.findFirstByImeiOrderByIdDesc(dev.getString("imei"));
                if (_device != null) {
                    deviceRepository.delete(_device);
                }

                Device device = new Device(
                        dev.getString("dev"),
                        dev.getString("pid"),
                        dev.getString("imei"),
                        messageBodyJson.getString("imei"));
                deviceRepository.save(device);


                //设备配网时候肯定在线，修改在线状态
                DeviceStatus deviceStatus = deviceStatusRepository.findFirstByImeiOrderByIdDesc(dev.getString("imei"));
                deviceStatus.setOnline(1);
                deviceStatusRepository.save(deviceStatus);

                deviceStatusService.pushDeviceStatus(device, AppNoticeTypeConsts.ADD);
            }

            GatewayStatusService gatewayStatusService = (GatewayStatusService) SpringUtil.getBean("gatewayStatusService");
            gatewayStatusService.pushGatewayStatus(messageBodyJson.getString("imei"));

            // 写入文件待后续处理
            gatewayLogger.info(message);
        } catch (Exception e) {
            errorLogger.error("GatewayAdddevProcessor异常！message:" + message, e);
        }
    }

}
