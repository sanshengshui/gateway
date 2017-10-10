package com.aiyolo.repository;

import com.aiyolo.entity.DeviceCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DeviceCategoryRepository extends PagingAndSortingRepository<DeviceCategory, Long> {

    Page<DeviceCategory> findPageByCategory(Pageable pageable, Integer category);

    DeviceCategory findOneByCategoryAndType(Integer category, Integer type);

    DeviceCategory findOneByCode(String code);

}
