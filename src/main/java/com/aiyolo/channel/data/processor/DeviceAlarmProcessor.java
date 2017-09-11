package com.aiyolo.channel.data.processor;

import com.aiyolo.common.SpringUtil;
import com.aiyolo.constant.AlarmStatusEnum;
import com.aiyolo.constant.SingleAlarmTypeEnum;
import com.aiyolo.entity.DeviceAlarm;
import com.aiyolo.entity.DeviceAlarmCancel;
import com.aiyolo.repository.DeviceAlarmCancelRepository;
import com.aiyolo.repository.DeviceAlarmRepository;
import com.aiyolo.service.DeviceStatusService;
import com.aiyolo.service.NoticeWarningService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DeviceAlarmProcessor extends Processor {

    private static Log deviceAlarmLogger = LogFactory.getLog("deviceAlarmLog");

    @Override
    public void run(String message) {
        try {
            init(message);

            DeviceAlarm deviceAlarm = new DeviceAlarm(
                    messageHeaderJson.getString("gl_id"),
                    messageBodyJson.getString("imei"),
                    messageBodyJson.getInt("tsp"),
                    messageBodyJson.getInt("typ"),
                    AlarmStatusEnum.LIFE.getValue(),
                    messageBodyJson.getInt("tmp"),
                    messageBodyJson.getInt("hum"));

            DeviceAlarmRepository deviceAlarmRepository = (DeviceAlarmRepository) SpringUtil.getBean("deviceAlarmRepository");
            if (messageBodyJson.getInt("typ") == SingleAlarmTypeEnum.CLEAR.getValue()) {
                // 报警解除记录单独存储
                DeviceAlarmCancelRepository deviceAlarmCancelRepository = (DeviceAlarmCancelRepository) SpringUtil.getBean("deviceAlarmCancelRepository");
                deviceAlarmCancelRepository.save(new DeviceAlarmCancel(
                        messageHeaderJson.getString("gl_id"),
                        messageBodyJson.getString("imei"),
                        messageBodyJson.getInt("tsp"),
                        messageBodyJson.getInt("tmp"),
                        messageBodyJson.getInt("hum")));

                // 先查询是否有未解除的报警
                DeviceAlarm _deviceAlarm = deviceAlarmRepository.findFirstByGlIdAndStatusOrderByIdDesc(
                        messageHeaderJson.getString("gl_id"),
                        AlarmStatusEnum.LIFE.getValue());
                if (_deviceAlarm == null) { // 如果没有未解除的报警，直接返回
                    return;
                }

                // 报警解除
                deviceAlarmRepository.updateStatusByGlId(messageHeaderJson.getString("gl_id"), AlarmStatusEnum.CLEAR.getValue());
            } else {
                // 添加记录
                deviceAlarmRepository.save(deviceAlarm);
            }

            // 更新设备状态缓存
            DeviceStatusService deviceStatusService = (DeviceStatusService) SpringUtil.getBean("deviceStatusService");
            deviceStatusService.updateLatestDeviceStatus(deviceAlarm);

            // 推送给app&个推&发送短信
            NoticeWarningService noticeWarningService = (NoticeWarningService) SpringUtil.getBean("noticeWarningService");
            noticeWarningService.pushWarningNotice(messageBodyJson.getString("imei"), deviceAlarm, true);

            // 写入文件待后续处理
            deviceAlarmLogger.info(message);
        } catch (Exception e) {
            errorLogger.error("DeviceAlarmProcessor异常！message:" + message, e);
        }
    }

}
