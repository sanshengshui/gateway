package com.aiyolo.repository;

import com.aiyolo.entity.DeviceCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DeviceCategoryRepository extends PagingAndSortingRepository<DeviceCategory, Long> {

    Page<DeviceCategory> findPageByCategory(Pageable pageable, Integer category);

    DeviceCategory findOneByCategoryAndType(Integer category, Integer type);

//    @Cacheable(value="deviceCategories", unless="#result == null")
    DeviceCategory findOneByCode(String code);

//    @Override
//    @SuppressWarnings("unchecked")
//    @Caching(put = { @CachePut(value="deviceCategories", key="#p0.getCode()") })
//    DeviceCategory save(DeviceCategory deviceCategory);

}
