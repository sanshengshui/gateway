package com.aiyolo.repository;

import com.aiyolo.entity.DeviceAlarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DeviceAlarmRepository extends PagingAndSortingRepository<DeviceAlarm, Long>, JpaSpecificationExecutor<DeviceAlarm> {

    List<DeviceAlarm> findByImeiOrderByIdDesc(String imei);

    Page<DeviceAlarm> findPageByImei(Pageable pageable, String imei);

    List<DeviceAlarm> findByGlImeiOrderByIdDesc(String glImei);

    Page<DeviceAlarm> findPageByGlImei(Pageable pageable, String glImei);

    @Query(
            value = "select a from DeviceAlarm a where regexp(gateway.areaCode, :areaCodePat)=1",
            countQuery = "select count(a) from DeviceAlarm a where regexp(gateway.areaCode, :areaCodePat)=1"
            )
    Page<DeviceAlarm> findPageByGatewayAreaCodeMatch(Pageable pageable, @Param("areaCodePat") String areaCodePat);

    DeviceAlarm findFirstByImeiOrderByIdDesc(String imei);

    DeviceAlarm findFirstByGlImeiOrderByIdDesc(String glImei);

    DeviceAlarm findFirstByImeiAndStatusOrderByIdDesc(String imei, int status);

    @Modifying
    @Transactional
    @Query("update DeviceAlarm a set a.status = ?2 where a.imei = ?1")
    void updateStatusByImei(String imei, int status);

}
