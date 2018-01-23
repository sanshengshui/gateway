package com.aiyolo.channel.data.processor;

import com.aiyolo.common.BaiduMapHelper;
import com.aiyolo.common.SpringUtil;
import com.aiyolo.entity.GatewaySta;
import com.aiyolo.repository.GatewayStaRepository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

import static com.aiyolo.constant.ProtocolFieldConsts.IMEI;

public class GatewayStaProcessor extends Processor {

    private static Log gatewayLogger = LogFactory.getLog("gatewayLog");

    @Override
    public void run(String message) {
        try {
            init(message);

            int mcc = messageBodyJson.getInt("mcc");
            int mnc = messageBodyJson.getInt("mnc");
            int lac = messageBodyJson.getInt("lac");
            int cell = messageBodyJson.getInt("cell");

            String glImei = messageHeaderJson.getString(IMEI);
            GatewayStaRepository gatewayStaRepository = (GatewayStaRepository) SpringUtil.getBean("gatewayStaRepository");

            // 查询记录是否存在
            GatewaySta gatewaySta = gatewayStaRepository.findFirstByGlIdOrderByIdDesc(glImei);
            if (gatewaySta != null) {
                gatewaySta.setMcc(mcc);
                gatewaySta.setMnc(mnc);
                gatewaySta.setLac(lac);
                gatewaySta.setCell(cell);
            } else {
                gatewaySta = new GatewaySta(
                        glImei,
                        glImei,
                        mcc,
                        mnc,
                        lac,
                        cell);
            }
            Map<String, String> staLocation = BaiduMapHelper.getLocationBySta(mcc, mnc, lac, cell);
            if (staLocation != null) {
                gatewaySta.setStaLocation(staLocation.get("longitude") + "," + staLocation.get("latitude"));

                Map<String, String> locationAddress = BaiduMapHelper.getAddressByLocation(staLocation.get("longitude"), staLocation.get("latitude"));
                if (locationAddress != null) {
                    gatewaySta.setLocationAreaCode(locationAddress.get("areaCode"));
                    gatewaySta.setLocationAddress(locationAddress.get("address"));
                }
            }
            gatewayStaRepository.save(gatewaySta);

            // 写入文件待后续处理
            gatewayLogger.info(message);
        } catch (Exception e) {
            errorLogger.error("GatewayStaProcessor异常！message:" + message, e);
        }
    }

}
