package com.aiyolo.repository;

import com.aiyolo.entity.DeviceAlarmCancel;
import org.springframework.data.repository.CrudRepository;

public interface DeviceAlarmCancelRepository extends CrudRepository<DeviceAlarmCancel, Long> {

    DeviceAlarmCancel findFirstByImeiOrderByIdDesc(String imei);

    DeviceAlarmCancel findFirstByGlImeiOrderByIdDesc(String glImei);

}
