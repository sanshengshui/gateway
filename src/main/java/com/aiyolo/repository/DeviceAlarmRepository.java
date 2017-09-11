package com.aiyolo.repository;

import java.util.List;

import com.aiyolo.entity.DeviceAlarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface DeviceAlarmRepository extends PagingAndSortingRepository<DeviceAlarm, Long> {

    List<DeviceAlarm> findByGlIdOrderByIdDesc(String glId);

    List<DeviceAlarm> findByGlImeiOrderByIdDesc(String glImei);

    Page<DeviceAlarm> findPageByGlImei(Pageable pageable, String glImei);

    @Query(
            value = "select a from DeviceAlarm a where regexp(device.areaCode, :areaCodePat)=1",
            countQuery = "select count(a) from DeviceAlarm a where regexp(device.areaCode, :areaCodePat)=1"
            )
    Page<DeviceAlarm> findPageByDeviceAreaCodeMatch(Pageable pageable, @Param("areaCodePat") String areaCodePat);

    DeviceAlarm findFirstByGlIdOrderByIdDesc(String glId);

    DeviceAlarm findFirstByGlImeiOrderByIdDesc(String glImei);

    DeviceAlarm findFirstByGlIdAndStatusOrderByIdDesc(String glId, int status);

    @Modifying
    @Transactional
    @Query("update DeviceAlarm a set a.status = ?2 where a.glId = ?1")
    void updateStatusByGlId(String glId, int status);

}
