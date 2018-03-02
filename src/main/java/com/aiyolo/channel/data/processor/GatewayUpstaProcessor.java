package com.aiyolo.channel.data.processor;

import com.aiyolo.channel.data.request.GatewayMaccfgRequest;
import com.aiyolo.channel.data.response.GatewayUpstaResponse;
import com.aiyolo.common.SpringUtil;
import com.aiyolo.entity.Checked;
import com.aiyolo.entity.Gateway;
import com.aiyolo.entity.GatewaySetting;
import com.aiyolo.entity.GatewayStatus;
import com.aiyolo.queue.Sender;
import com.aiyolo.repository.CheckedRepository;
import com.aiyolo.repository.GatewayRepository;
import com.aiyolo.repository.GatewayStatusRepository;
import com.aiyolo.service.GatewayAlarmService;
import com.aiyolo.service.GatewaySettingService;
import com.aiyolo.service.GatewayStatusService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.aiyolo.constant.ProtocolFieldConsts.IMEI;
import static com.aiyolo.constant.ProtocolFieldConsts.MID;
import static com.aiyolo.constant.ProtocolFieldConsts.PIN;

public class GatewayUpstaProcessor extends Processor {

    private static Log gatewayLogger = LogFactory.getLog("gatewayLog");

    @Override
    public void run(String message) {
        try {
            init(message);

            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HH");
            int mid = messageBodyJson.getInt(MID);
            String _dh = format.format(mid * 1000L);
            String[] _dhArray = _dh.split(" ");

            String glImei = messageHeaderJson.getString(IMEI);
            int check = messageBodyJson.getInt("check");
            int sos = messageBodyJson.getInt("sos");
            int htmp = messageBodyJson.getInt("htmp");
            int needcfg = messageBodyJson.getInt("needcfg");
            GatewayStatus gatewayStatus = new GatewayStatus(
                    glImei,
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
                    check,
                    htmp,
                    needcfg
            );

            GatewayStatusRepository gatewayStatusRepository = (GatewayStatusRepository) SpringUtil.getBean("gatewayStatusRepository");


            //-------------------------增加网关报警和巡检---------------------------------

            GatewayAlarmService gatewayAlarmService = (GatewayAlarmService) SpringUtil.getBean("gatewayAlarmService");
            if (check == 1) {
                CheckedRepository checkedRepository = (CheckedRepository) SpringUtil.getBean("checkedRepository");
                checkedRepository.save(new Checked(glImei, mid));
                gatewayAlarmService.pushChecked(glImei, mid);
            }


            GatewayStatus load = gatewayStatusRepository.findFirstByGlImeiOrderByIdDesc(glImei);
            if (load != null) {
                if (load.getSos() != sos) {
                    //触发网关报警或者解除网关报警
                    gatewayAlarmService.gatewayAlarm(glImei, sos, mid);
                }

                if (load.getHtmp() != htmp) {
                    // TODO: 2018/2/1 发送预警通知栏推送和短信，以及记录日志
                }

            }
            //-------------------------增加网关报警和巡检---------------------------------


            gatewayStatusRepository.save(gatewayStatus);

            // 应答
            Sender sender = (Sender) SpringUtil.getBean("sender");

            GatewayRepository gatewayRepository = (GatewayRepository) SpringUtil.getBean("gatewayRepository");
            Gateway gateway = gatewayRepository.findFirstByGlImeiOrderByIdDesc(glImei);

            GatewaySettingService gatewaySettingService = (GatewaySettingService) SpringUtil.getBean("gatewaySettingService");
            GatewaySetting gatewaySetting = gatewaySettingService.getGatewaySetting(gateway);

            Map<String, Object> resHeaderMap = GatewayUpstaResponse.getInstance().responseHeader(glImei);
            Map<String, Object> resBodyMap = GatewayUpstaResponse.getInstance().responseBody(messageJson, gatewaySetting);

            sender.sendMessage(resHeaderMap, resBodyMap);


            //-------------------------增加下发mac配置---------------------------------
            if (needcfg > 0) {
                Map<String, Object> headerMap = GatewayMaccfgRequest.getInstance().requestHeader(glImei);

                Map<String, Object> data = new LinkedHashMap<>();
                data.put(IMEI, glImei);
                data.put(PIN, gateway.getGlPin());
                Map<String, Object> bodyMap =  GatewayMaccfgRequest.getInstance().requestBody(data);

                sender.sendMessage(headerMap, bodyMap);
            }


            //-------------------------增加下发mac配置---------------------------------


            // 推送给app
            GatewayStatusService gatewayStatusService = (GatewayStatusService) SpringUtil.getBean("gatewayStatusService");
            gatewayStatusService.pushGatewayStatus(glImei);

            // 写入文件待后续处理
            gatewayLogger.info(message);
        } catch (Exception e) {
            errorLogger.error("GatewayUpstaProcessor异常！message:" + message, e);
        }
    }

}
