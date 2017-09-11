package com.aiyolo.channel.data.processor;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aiyolo.common.BaiduMapHelper;
import com.aiyolo.common.SpringUtil;
import com.aiyolo.entity.DeviceSta;
import com.aiyolo.repository.DeviceStaRepository;

public class DeviceStaProcessor extends Processor {

    private static Log deviceStaLogger = LogFactory.getLog("deviceStaLog");

    @Override
    public void run(String message) {
        try {
            init(message);

            int mcc = messageBodyJson.getInt("mcc");
            int mnc = messageBodyJson.getInt("mnc");
            int lac = messageBodyJson.getInt("lac");
            int cell = messageBodyJson.getInt("cell");

            DeviceStaRepository deviceStaRepository = (DeviceStaRepository) SpringUtil.getBean("deviceStaRepository");

            // 查询记录是否存在
            DeviceSta deviceSta = deviceStaRepository.findFirstByGlIdOrderByIdDesc(messageHeaderJson.getString("gl_id"));
            if (deviceSta != null) {
                deviceSta.setMcc(mcc);
                deviceSta.setMnc(mnc);
                deviceSta.setLac(lac);
                deviceSta.setCell(cell);
            } else {
                deviceSta = new DeviceSta(
                        messageHeaderJson.getString("gl_id"),
                        messageBodyJson.getString("imei"),
                        mcc,
                        mnc,
                        lac,
                        cell);
            }
            Map<String, String> staLocation = BaiduMapHelper.getLocationBySta(mcc, mnc, lac, cell);
            if (staLocation != null) {
                deviceSta.setStaLocation(staLocation.get("longitude") + "," + staLocation.get("latitude"));

                Map<String, String> locationAddress = BaiduMapHelper.getAddressByLocation(staLocation.get("longitude"), staLocation.get("latitude"));
                if (locationAddress != null) {
                    deviceSta.setLocationAreaCode(locationAddress.get("areaCode"));
                    deviceSta.setLocationAddress(locationAddress.get("address"));
                }
            }
            deviceStaRepository.save(deviceSta);

            // 写入文件待后续处理
            deviceStaLogger.info(message);
        } catch (Exception e) {
            errorLogger.error("DeviceStaProcessor异常！message:" + message, e);
        }
    }

}
