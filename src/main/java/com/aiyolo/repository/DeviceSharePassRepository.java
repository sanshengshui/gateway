package com.aiyolo.repository;

import com.aiyolo.entity.DeviceSharePass;
import org.springframework.data.repository.CrudRepository;

public interface DeviceSharePassRepository extends CrudRepository<DeviceSharePass, Long> {

    DeviceSharePass findFirstByGlImeiOrderByIdDesc(String glImei);

}
