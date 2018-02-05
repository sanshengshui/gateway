package com.aiyolo.repository;

import com.aiyolo.entity.DeviceAlarmNow;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeviceAlarmNowRepository extends CrudRepository<DeviceAlarmNow, Long> {

    DeviceAlarmNow findOneByAreaCodeAndImeiAndType(String areaCode, String imei, String type);

    DeviceAlarmNow findOneByGlImeiAndType(String glImei, String type);

    List<DeviceAlarmNow> findByAreaCodeAndImei(String areaCode, String imei);

    @Query(value = "select n.areaCode as areaCode, n.type as type, count(n.glImei) as num from DeviceAlarmNow n where " +
            "regexp(n.areaCode, ?1)=1 group by n.areaCode,n.type")
    List<Object> findTotalByAreaCode(String areaCode);

}
