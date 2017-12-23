package com.aiyolo.channel.data.processor;

import com.aiyolo.common.SpringUtil;
import com.aiyolo.constant.AlarmStatusEnum;
import com.aiyolo.entity.DeviceAlarm;
import com.aiyolo.entity.DeviceAlarmCancel;
import com.aiyolo.entity.DeviceStatus;
import com.aiyolo.repository.DeviceAlarmCancelRepository;
import com.aiyolo.repository.DeviceAlarmRepository;
import com.aiyolo.repository.DeviceStatusRepository;
import com.aiyolo.service.DeviceAlarmService;
import com.aiyolo.service.DeviceStatusService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GatewayAlarmProcessor extends Processor {

    private static Log gatewayLogger = LogFactory.getLog("gatewayLog");
    @Override
    public void run(String message) {
        try {
            init(message);

            DeviceAlarmRepository deviceAlarmRepository = (DeviceAlarmRepository) SpringUtil.getBean("deviceAlarmRepository");
            DeviceAlarmCancelRepository deviceAlarmCancelRepository = (DeviceAlarmCancelRepository) SpringUtil.getBean("deviceAlarmCancelRepository");

            DeviceStatusService deviceStatusService = (DeviceStatusService) SpringUtil.getBean("deviceStatusService");
            DeviceAlarmService deviceAlarmService = (DeviceAlarmService) SpringUtil.getBean("deviceAlarmService");

            DeviceStatusRepository deviceStatusRepository = (DeviceStatusRepository) SpringUtil.getBean("deviceStatusRepository");

            JSONArray deviceAlarms = messageBodyJson.getJSONArray("devs");

            for (int i = 0; i < deviceAlarms.size(); i++) {
                JSONObject alarm = deviceAlarms.getJSONObject(i);

                DeviceAlarm deviceAlarm = new DeviceAlarm(
                        alarm.getString("dev"),
                        alarm.getString("imei"),
                        messageBodyJson.getString("imei"),
                        messageBodyJson.getInt("mid"),
                        alarm.getInt("val"),
                        AlarmStatusEnum.LIFE.getValue());

                if (AlarmStatusEnum.CLEAR.getValue().equals(alarm.getInt("val"))) {
                    // 警报解除记录单独存储
                    deviceAlarmCancelRepository.save(new DeviceAlarmCancel(
                            alarm.getString("dev"),
                            alarm.getString("imei"),
                            messageBodyJson.getString("imei"),
                            messageBodyJson.getInt("mid")));

                    // 先查询是否有未解除的报警
                    DeviceAlarm _deviceAlarm = deviceAlarmRepository.findFirstByImeiAndStatusOrderByIdDesc(
                            alarm.getString("imei"),
                            AlarmStatusEnum.LIFE.getValue());
                    if (_deviceAlarm == null) { // 如果没有未解除的报警，直接返回
                        return;
                    }

                    // 报警解除
                    deviceAlarmRepository.updateStatusByImei(alarm.getString("imei"), AlarmStatusEnum.CLEAR.getValue());
                } else {
                    // 添加记录
                    deviceAlarmRepository.save(deviceAlarm);
                }

                //设备发生报警时候肯定在线，修改在线状态
                DeviceStatus deviceStatus = deviceStatusRepository.findFirstByImeiOrderByIdDesc(deviceAlarm.getImei());
                deviceStatus.setOnline(1);
                deviceStatusRepository.save(deviceStatus);

                // 推送设备状态变化
                deviceStatusService.pushDeviceStatus(deviceAlarm.getImei());

                // 推送预报警通知给APP&个推&发送短信
                deviceAlarmService.pushDeviceAlarm(deviceAlarm);
            }

            // 写入文件待后续处理
            gatewayLogger.info(message);
        } catch (Exception e) {
            errorLogger.error("GatewayAlarmProcessor异常！message:" + message, e);
        }
    }

}
