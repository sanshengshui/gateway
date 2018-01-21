package com.aiyolo.channel.data.processor;

import com.aiyolo.cache.GatewayBeatCache;
import com.aiyolo.cache.GatewayLiveStatusCache;
import com.aiyolo.common.InputDataHelper;
import com.aiyolo.common.SpringUtil;
import com.aiyolo.constant.InputDataTypeEnum;
import com.aiyolo.constant.ProtocolCodeConsts;
import com.aiyolo.constant.ProtocolFieldConsts;
import com.aiyolo.entity.Gateway;
import com.aiyolo.repository.GatewayRepository;
import com.aiyolo.service.GatewayService;

import net.sf.json.JSONObject;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import static com.aiyolo.cache.GatewayLiveStatusCache.LIVE;
import static com.aiyolo.constant.ProtocolFieldConsts.ACT;
import static com.aiyolo.constant.ProtocolFieldConsts.ACTION;
import static com.aiyolo.constant.ProtocolFieldConsts.IMEI;
import static com.aiyolo.constant.ProtocolFieldConsts.PIN;

public abstract class Processor {

    public static Log errorLogger = LogFactory.getLog("errorLog");

    public JSONObject messageJson;
    public JSONObject messageHeaderJson;
    public JSONObject messageBodyJson;

    public void init(String message) throws Exception {
        messageJson = JSONObject.fromObject(message);
        messageHeaderJson = messageJson.getJSONObject("header");
        messageBodyJson = messageJson.getJSONObject("body");


        int code = messageHeaderJson.getInt(ProtocolFieldConsts.CODE);

        boolean containsImei = messageHeaderJson.containsKey(IMEI);
        switch (code) {
            case ProtocolCodeConsts.RECEIVE_FROM_GATEWAY:
                if (!containsImei || !messageBodyJson.containsKey(IMEI)||
                        (!messageBodyJson.containsKey(ACTION) && !messageBodyJson.containsKey(ACT))) {
                    throw new RuntimeException("Gateway Params Error.");
                }
                String action = InputDataHelper.getAction(messageBodyJson);
                if (!ArrayUtils.contains(InputDataTypeEnum.GATEWAY_PUSH.getValue(), action)) {
                    return;

                }

                GatewayService gatewayService = (GatewayService) SpringUtil.getBean("gatewayService");
                GatewayRepository gatewayRepository = (GatewayRepository) SpringUtil.getBean("gatewayRepository");

                // 查询该是否已入库
                String imei = messageHeaderJson.getString(IMEI);
                Gateway gateway = gatewayRepository.findFirstByGlIdOrderByIdDesc(imei);
                if (gateway == null) {
                   gatewayRepository.save(new Gateway(imei,imei, messageBodyJson.getString(PIN)));

                    gatewayService.requestGatewayInfo(imei);
                } else {
                    if (StringUtils.isEmpty(gateway.getGlLongitude())
                            || StringUtils.isEmpty(gateway.getGlLatitude())) {
                        gatewayService.requestGatewayInfo(imei);
                    }
                }

                // 检查网关存活状态
                GatewayLiveStatusCache gatewayLiveStatusCache = (GatewayLiveStatusCache) SpringUtil.getBean("gatewayLiveStatusCache");
                int gatewayLiveStatus = gatewayLiveStatusCache.getByGlId(imei);
                if (gatewayLiveStatus != LIVE) {
                    // 记录一条心跳
                    GatewayBeatCache gatewayBeatCache = (GatewayBeatCache) SpringUtil.getBean("gatewayBeatCache");
                    gatewayBeatCache.save(imei, System.currentTimeMillis());
                    // 更新存活状态
                    gatewayLiveStatusCache.save(imei, LIVE);
                }


                break;
            case ProtocolCodeConsts.SEND_TO_GATEWAY:
                break;
            case ProtocolCodeConsts.GATEWAY_BEAT:
                if (!containsImei) {
                    throw new RuntimeException("Gateway imei Params Error.");
                }
                break;
            case ProtocolCodeConsts.PUSH_TO_APP:
                break;
            case ProtocolCodeConsts.DEVICE_INFO:
                if (!containsImei) {
                    throw new RuntimeException("Gateway imei Params Error.");
                }
                break;
            case ProtocolCodeConsts.APP_LOGIN:
                break;
        }

        //
        //        InputDataTypeEnum dataType = InputDataHelper.getInputDataType(messageHeaderJson, messageBodyJson);
        //
        //        switch (dataType) {
        //        case GATEWAY_BEAT:
        //            break;
        //        case GATEWAY_PUSH:
        //            if (StringUtils.isEmpty(messageHeaderJson.getString("gl_id")) ||
        //                    StringUtils.isEmpty(messageBodyJson.getString("imei")) ||
        //                    StringUtils.isEmpty(messageBodyJson.getString("pin"))) {
        //                throw new RuntimeException("Gateway Push Params Error.");
        //            }
        //
        //            GatewayService gatewayService = (GatewayService) SpringUtil.getBean("gatewayService");
        //            GatewayRepository gatewayRepository = (GatewayRepository) SpringUtil.getBean("gatewayRepository");
        //
        //            // 查询该是否已入库
        //            Gateway gateway = gatewayRepository.findFirstByGlIdOrderByIdDesc(messageHeaderJson.getString("gl_id"));
        //            if (gateway == null) {
        //                gateway = gatewayRepository.save(new Gateway(
        //                        messageHeaderJson.getString("gl_id"),
        //                        messageBodyJson.getString("imei"),
        //                        messageBodyJson.getString("pin")));
        //
        //                gatewayService.requestGatewayInfo(messageHeaderJson.getString("gl_id"));
        //            } else {
        //                if (StringUtils.isEmpty(gateway.getGlLongitude()) || StringUtils.isEmpty(gateway.getGlLatitude())) {
        //                    gatewayService.requestGatewayInfo(messageHeaderJson.getString("gl_id"));
        //                }
        //            }
        //
        //            // 检查网关存活状态
        //            GatewayLiveStatusCache gatewayLiveStatusCache = (GatewayLiveStatusCache) SpringUtil.getBean("gatewayLiveStatusCache");
        //            int gatewayLiveStatus = gatewayLiveStatusCache.getByGlId(messageHeaderJson.getString("gl_id"));
        //            if (gatewayLiveStatus != GatewayLiveStatusCache.LIVE) {
        //                // 记录一条心跳
        //                GatewayBeatCache gatewayBeatCache = (GatewayBeatCache) SpringUtil.getBean("gatewayBeatCache");
        //                gatewayBeatCache.save(messageHeaderJson.getString("gl_id"), System.currentTimeMillis());
        //                // 更新存活状态
        //                gatewayLiveStatusCache.save(messageHeaderJson.getString("gl_id"), GatewayLiveStatusCache.LIVE);
        //            }
        //
        //            break;
        //        case GATEWAY_REQUEST:
        //        case GATEWAY_RESPONSE:
        //            break;
        //        case APP_PUSH:
        //        case APP_REQUEST:
        //        case APP_RESPONSE:
        //            break;
        //        case CHANNEL_REQUEST:
        //        case CHANNEL_RESPONSE:
        //            break;
        //        }
    }

    public abstract void run(String message) throws Exception;

}
