package com.aiyolo.channel.data.processor;

import com.aiyolo.common.SpringUtil;
import com.aiyolo.constant.AppNoticeTypeConsts;
import com.aiyolo.constant.DeviceOnlineStatusConsts;
import com.aiyolo.constant.ProtocolFieldConsts;
import com.aiyolo.entity.Device;
import com.aiyolo.repository.DeviceRepository;
import com.aiyolo.service.DeviceStatusService;
import com.aiyolo.service.GatewayStatusService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static com.aiyolo.constant.ProtocolFieldConsts.DEV;
import static com.aiyolo.constant.ProtocolFieldConsts.DEVS;
import static com.aiyolo.constant.ProtocolFieldConsts.IMEI;
import static com.aiyolo.constant.ProtocolFieldConsts.PID;

public class GatewayAdddevProcessor extends Processor {

    private static Log gatewayLogger = LogFactory.getLog("gatewayLog");

    @Override
    public void run(String message) {
        try {
            init(message);

            DeviceRepository deviceRepository = (DeviceRepository)
                    SpringUtil.getBean("deviceRepository");
            DeviceStatusService deviceStatusService = (DeviceStatusService)
                    SpringUtil.getBean("deviceStatusService");

            JSONArray devs = messageBodyJson.getJSONArray(DEVS);

            String glImei = messageHeaderJson.getString(IMEI);
            for (int i = 0; i < devs.size(); i++) {
                JSONObject dev = devs.getJSONObject(i);

                String imei = dev.getString(IMEI);
                Device _device = deviceRepository.findFirstByImeiOrderByIdDesc(imei);
                if (_device != null) {
                    deviceRepository.delete(_device);
                }

                Device device = new Device(
                        dev.getString(DEV),
                        dev.getString(PID),
                        imei,
                        glImei);
                deviceRepository.save(device);

                deviceStatusService.pushDeviceStatus(device, AppNoticeTypeConsts.ADD,
                        DeviceOnlineStatusConsts.ONLINE);
            }

            GatewayStatusService gatewayStatusService = (GatewayStatusService)
                    SpringUtil.getBean("gatewayStatusService");
            gatewayStatusService.pushGatewayStatus(glImei);

            // 写入文件待后续处理
            gatewayLogger.info(message);
        } catch (Exception e) {
            errorLogger.error("GatewayAdddevProcessor异常！message:" + message, e);
        }
    }

}
