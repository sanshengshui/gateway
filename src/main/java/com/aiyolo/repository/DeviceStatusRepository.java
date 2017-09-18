package com.aiyolo.repository;

import com.aiyolo.entity.DeviceStatus;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.repository.CrudRepository;

public interface DeviceStatusRepository extends CrudRepository<DeviceStatus, Long> {

    @Cacheable(value="deviceStatuses", unless="#result == null")
    DeviceStatus findFirstByImeiOrderByIdDesc(String imei);

    @Override
    @SuppressWarnings("unchecked")
    @Caching(put = { @CachePut(value="deviceStatuses", key="#p0.getImei()") })
    DeviceStatus save(DeviceStatus deviceStatus);

}
