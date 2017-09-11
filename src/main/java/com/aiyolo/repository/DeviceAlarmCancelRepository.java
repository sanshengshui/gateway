package com.aiyolo.repository;

import com.aiyolo.entity.DeviceAlarmCancel;
import org.springframework.data.repository.CrudRepository;

public interface DeviceAlarmCancelRepository extends CrudRepository<DeviceAlarmCancel, Long> {

    DeviceAlarmCancel findFirstByGlIdOrderByIdDesc(String glId);

    DeviceAlarmCancel findFirstByGlImeiOrderByIdDesc(String glImei);

}
