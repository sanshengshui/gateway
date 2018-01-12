package com.aiyolo.repository;

import com.aiyolo.entity.DeviceAlarm;
import com.aiyolo.entity.GatewayAlarm;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GatewayAlarmRepository extends CrudRepository<GatewayAlarm, Long> {

    List<GatewayAlarm> findByImeiOrderByIdDesc(String imei);

    GatewayAlarm findFirstByImeiAndStatusOrderByIdDesc(String imei, int status);

    @Modifying
    @Transactional
    @Query("update GatewayAlarm a set a.status = ?2 where a.imei = ?1")
    void updateStatusByImei(String imei, int status);

}
