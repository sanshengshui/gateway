package com.aiyolo.repository;

import java.util.List;

import com.aiyolo.entity.DeviceStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface DeviceStatusRepository extends CrudRepository<DeviceStatus, Long> {

    List<DeviceStatus> findByGlImei(String glImei);

    DeviceStatus findFirstByGlIdOrderByIdDesc(String glId);

    DeviceStatus findFirstByGlImeiOrderByIdDesc(String glImei);

    @Query("select s from DeviceStatus s where s.glImei = ?1 and s.date = ?2")
    List<DeviceStatus> findByGlImeiDate(String glImei, String date);

    @Query("select s from DeviceStatus s where s.glImei = ?1 and s.date = ?2 and s.hour in ?3")
    List<DeviceStatus> findByGlImeiDateHours(String glImei, String date, String[] hours);

}
