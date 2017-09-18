package com.aiyolo.repository;

import com.aiyolo.entity.Device;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DeviceRepository extends PagingAndSortingRepository<Device, Long> {

    List<Device> findByGlImei(String GlImei);

    Page<Device> findPageByGlImei(Pageable pageable, String glImei);

    @Cacheable(value="devices", unless="#result == null")
    Device findFirstByImeiOrderByIdDesc(String imei);

    @Override
    @SuppressWarnings("unchecked")
    @Caching(put = { @CachePut(value="devices", key="#p0.getImei()") })
    Device save(Device device);

}
