package com.aiyolo.repository;

import com.aiyolo.entity.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DeviceRepository extends PagingAndSortingRepository<Device, Long> {

    List<Device> findByGlImei(String GlImei);

    Device findByImei(String imei);

    Page<Device> findPageByGlImei(Pageable pageable, String glImei);

    Device findFirstByImeiOrderByIdDesc(String imei);

}
