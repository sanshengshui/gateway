package com.aiyolo.repository;

import com.aiyolo.entity.DeviceSetting;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DeviceSettingRepository extends PagingAndSortingRepository<DeviceSetting, Long> {

//    @Cacheable(value="deviceSettings", key="#areaCode.concat('-').concat(#glImei)", unless="#result == null")
//    @Query("select s from DeviceSetting s where glImei=:glImei or areaCode=:areaCode order by id desc limit 1")
//    DeviceSetting findFirstByGlImeiOrAreaCodeOrderByIdDesc(@Param("glImei") String glImei, @Param("areaCode") String areaCode);

    @Cacheable(value="deviceSettings")
    DeviceSetting findFirstByGlImeiOrAreaCodeOrderByIdDesc(String glImei, String areaCode);

    @Override
    @SuppressWarnings("unchecked")
    @CacheEvict(value="deviceSettings", allEntries=true) // 移除所有缓存数据
    DeviceSetting save(DeviceSetting deviceSetting);

}
