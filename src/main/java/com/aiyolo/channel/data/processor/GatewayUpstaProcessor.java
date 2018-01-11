package com.aiyolo.channel.data.processor;

import com.aiyolo.channel.data.response.GatewayUpstaResponse;
import com.aiyolo.common.SpringUtil;
import com.aiyolo.entity.Gateway;
import com.aiyolo.entity.GatewaySetting;
import com.aiyolo.entity.GatewayStatus;
import com.aiyolo.queue.Sender;
import com.aiyolo.repository.GatewayRepository;
import com.aiyolo.repository.GatewayStatusRepository;
import com.aiyolo.service.GatewayAlarmService;
import com.aiyolo.service.GatewaySettingService;
import com.aiyolo.service.GatewayStatusService;
import com.aiyolo.service.MessagePushService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.SimpleDateFormat;
import java.util.Map;

public class GatewayUpstaProcessor extends Processor {

    private static Log gatewayLogger = LogFactory.getLog("gatewayLog");

    @Override
    public void run(String message) {
        try {
            init(message);

            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HH");
            int mid = messageBodyJson.getInt("mid");
            String _dh = format.format(mid * 1000L);
            String[] _dhArray = _dh.split(" ");

            int sos = messageBodyJson.getInt("sos");
            int check = messageBodyJson.getInt("check");
            String imei = messageBodyJson.getString("imei");
            GatewayStatus gatewayStatus = new GatewayStatus(
                    messageHeaderJson.getString("gl_id"),
                    imei,
                    mid,
                    _dhArray[0],
                    _dhArray[1],
                    messageBodyJson.getInt("rssi"),
                    messageBodyJson.getInt("temp"),
                    messageBodyJson.getInt("hum"),
                    messageBodyJson.getInt("atm"),
                    messageBodyJson.getString("ver"),
                    messageBodyJson.getInt("err"),
                    sos,
                    check);

            GatewayStatusRepository gatewayStatusRepository = (GatewayStatusRepository) SpringUtil.getBean("gatewayStatusRepository");

            //-------------------------增加网关报警和巡检---------------------------------

            GatewayStatus load = gatewayStatusRepository.findFirstByGlImeiOrderByIdDesc(gatewayStatus.getGlImei());
            if (load != null) {
                if (load.getSos() != sos) {
                    //触发网关报警或者解除网关报警
                    GatewayAlarmService gatewayAlarmService = (GatewayAlarmService) SpringUtil.getBean("gatewayAlarmService");
                    gatewayAlarmService.gatewayAlarm(imei,sos,mid);
                }
            }
            //-------------------------增加网关报警和巡检---------------------------------


            gatewayStatusRepository.save(gatewayStatus);

            // 应答
            Sender sender = (Sender) SpringUtil.getBean("sender");

            GatewayRepository gatewayRepository = (GatewayRepository) SpringUtil.getBean("gatewayRepository");
            Gateway gateway = gatewayRepository.findFirstByGlIdOrderByIdDesc(messageHeaderJson.getString("gl_id"));

            GatewaySettingService gatewaySettingService = (GatewaySettingService) SpringUtil.getBean("gatewaySettingService");
            GatewaySetting gatewaySetting = gatewaySettingService.getGatewaySetting(gateway);

            Map<String, Object> resHeaderMap = GatewayUpstaResponse.getInstance().responseHeader(messageHeaderJson.getString("gl_id"));
            Map<String, Object> resBodyMap = GatewayUpstaResponse.getInstance().responseBody(messageJson, gatewaySetting);

            sender.sendMessage(resHeaderMap, resBodyMap);

            // 推送给app
            GatewayStatusService gatewayStatusService = (GatewayStatusService) SpringUtil.getBean("gatewayStatusService");
            gatewayStatusService.pushGatewayStatus(gateway);

            // 写入文件待后续处理
            gatewayLogger.info(message);
        } catch (Exception e) {
            errorLogger.error("GatewayStatusProcessor异常！message:" + message, e);
        }
    }

}
