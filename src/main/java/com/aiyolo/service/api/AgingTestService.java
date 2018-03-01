package com.aiyolo.service.api;

import com.aiyolo.entity.GatewaySta;
import com.aiyolo.entity.GatewayStatus;
import com.aiyolo.repository.GatewayStaRepository;
import com.aiyolo.repository.GatewayStatusRepository;
import com.aiyolo.service.api.request.GetInfoRequest;
import com.aiyolo.service.api.request.RequestObject;
import com.aiyolo.service.api.response.AgingTestResponse;
import com.aiyolo.service.api.response.ResponseObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgingTestService
        //        extends ApiService
{

    @Autowired
    GatewayStaRepository gatewayStaRepository;
    @Autowired
    GatewayStatusRepository gatewayStatusRepository;

    //    public static final int BEAT_TIME_OUT = 40 * 60;
    private static final long BEAT_TIME_OUT = 40 * 60 * 1000L;
    private static final String FORMAT_TIME = "%d小时%d分钟";

    public AgingTestResponse doExecute(String imei) {
        //    @Override
        //    @SuppressWarnings("unchecked")
        //    public <Req extends RequestObject, Res extends ResponseObject> Res doExecute(Req request) throws Exception {

        //        GetInfoRequest getInfoRequest = (GetInfoRequest) request;
        //        String imei = getInfoRequest.getImei();


        long staUpdatedAtTime = gatewayStaRepository.findFirstByGlImeiOrderByIdDesc(imei).getUpdatedAt().getTime();
        //        int staUpdatedAtTimeInt = (int) (staUpdatedAtTime / 1000L);
        List<GatewayStatus> gatewayStatusList = gatewayStatusRepository.findByGlImei(imei);
        gatewayStatusList = gatewayStatusList.stream()
                //                .filter(gatewayStatus -> gatewayStatus.getTimestamp() > staUpdatedAtTimeInt)
                .filter(gatewayStatus -> gatewayStatus.getCreatedAt().getTime() > staUpdatedAtTime)
                .sorted(Comparator.comparing(GatewayStatus::getTimestamp))
                .collect(Collectors.toList());

        //        int lastStatus = 0;
        long maxContinuousTime = 0L, lastStatusTime = staUpdatedAtTime, maxTime = 0L, createdTime, startTime, continuousTime = 0L;
        GatewayStatus gatewayStatusLast = null;

        if (!gatewayStatusList.isEmpty()) {
            gatewayStatusLast = gatewayStatusList.get(gatewayStatusList.size() - 1);

        }

        if (gatewayStatusList.size() > 1) {
            startTime = lastStatusTime = maxTime = gatewayStatusList.get(0).getCreatedAt().getTime();
            for (int i = 1; i < gatewayStatusList.size(); i++) {
                createdTime = gatewayStatusList.get(i).getCreatedAt().getTime();
                //                maxTime = Math.max(createdTime, maxTime);
                long abs = Math.abs(createdTime - lastStatusTime);
                if (abs > BEAT_TIME_OUT) {//两次间隔超过40分钟
                    //                    maxContinuousTime = Math.max(Math.abs(createdTime - startTime), maxContinuousTime);
                    maxContinuousTime = Math.max(continuousTime, maxContinuousTime);
                    continuousTime = 0L;
                    //                    startTime = createdTime;
                } else {
                    continuousTime += abs;
                }
                lastStatusTime = createdTime;
            }
            maxContinuousTime = Math.max(continuousTime, maxContinuousTime);

        }


        String ext = "";

        if (gatewayStatusLast != null) {
            lastStatusTime = gatewayStatusLast.getCreatedAt().getTime();
            ext = gatewayStatusLast.toString1();
        }

        long currentTimeMillis = System.currentTimeMillis();
        String staStart = parseTimeLongToString(currentTimeMillis - staUpdatedAtTime);
        String statusStart = parseTimeLongToString(currentTimeMillis - lastStatusTime);
        String statusContinuous = parseTimeLongToString(maxContinuousTime);

        boolean pass = maxContinuousTime >= 24 * 60 * 60 * 1000L;

        if (pass) {
            pass = gatewayStatusLast.getChecked() == 0
                    && gatewayStatusLast.getSos() == 0
                    && gatewayStatusLast.getStatus() == 0
                    && gatewayStatusLast.getHtmp() == 0
            ;
        }

        return new AgingTestResponse(staStart, statusStart, statusContinuous, pass, ext);
    }

    private String parseTimeLongToString(long time) {

        int timeInt = (int) (time / 1000L);
        int minute = timeInt / 60;
        int hour = minute / 60;
        minute = minute % 60;

        return String.format(FORMAT_TIME, hour, minute);

    }


}
