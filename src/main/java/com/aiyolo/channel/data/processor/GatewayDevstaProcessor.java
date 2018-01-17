package com.aiyolo.channel.data.processor;

import com.aiyolo.common.SpringUtil;
import com.aiyolo.entity.Checked;
import com.aiyolo.entity.DeviceStatus;
import com.aiyolo.entity.GatewayStatus;
import com.aiyolo.repository.CheckedRepository;
import com.aiyolo.repository.DeviceRepository;
import com.aiyolo.repository.DeviceStatusRepository;
import com.aiyolo.service.DeviceStatusService;
import com.aiyolo.service.GatewayAlarmService;

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

            DeviceRepository deviceRepository = (DeviceRepository) SpringUtil.getBean("deviceRepository");
            JSONArray deviceStatuses = messageBodyJson.getJSONArray("devs");

            String glImei = messageBodyJson.getString("imei");
            int mid = messageBodyJson.getInt("mid");
            for (int i = 0; i < deviceStatuses.size(); i++) {
                JSONObject deviceStatus = deviceStatuses.getJSONObject(i);

                String imei = deviceStatus.getString("imei");
                if (!glImei.equals(deviceRepository.findFirstByImeiOrderByIdDesc(imei).getGlImei())){
                    //不是所属网关汇报的状态忽略掉
                    continue;
                }
                deviceStatusRepository.save(new DeviceStatus(
                        deviceStatus.getString("dev"),
                        imei,
                        glImei,
                        mid,
                        deviceStatus.getInt("online"),
                        deviceStatus.getInt("err"),
                        deviceStatus.getInt("rssi"),
                        deviceStatus.getInt("bat"),
                        deviceStatus.getString("info"),
                        deviceStatus.getInt("check")));

                // 推送给app
                deviceStatusService.pushDeviceStatus(deviceStatus.getString("imei"));


                //-------------------------增加巡检---------------------------------
                if (deviceStatus.getInt("check") == 1) {
                    CheckedRepository checkedRepository = (CheckedRepository)
                            SpringUtil.getBean("checkedRepository");
                    checkedRepository.save(new Checked(imei, mid));
                    deviceStatusService.pushChecked(imei, mid);

                }
                //-------------------------增加巡检---------------------------------


            }

            // 写入文件待后续处理
            gatewayLogger.info(message);
        } catch (Exception e) {
            errorLogger.error("GatewayDevstaProcessor异常！message:" + message, e);
        }
    }

}
