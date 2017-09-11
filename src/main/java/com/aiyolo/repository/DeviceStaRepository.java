package com.aiyolo.repository;

import com.aiyolo.entity.DeviceSta;
import org.springframework.data.repository.CrudRepository;

public interface DeviceStaRepository extends CrudRepository<DeviceSta, Long> {

    DeviceSta findFirstByGlIdOrderByIdDesc(String glId);

}
