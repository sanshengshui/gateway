package com.aiyolo.repository;

import com.aiyolo.entity.GatewaySetting;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GatewaySettingRepository extends PagingAndSortingRepository<GatewaySetting, Long> {

//    @Cacheable(value="gatewaySettings", key="#areaCode.concat('-').concat(#glImei)", unless="#result == null")
//    @Query("select s from GatewaySetting s where glImei=:glImei or areaCode=:areaCode order by id desc limit 1")
//    GatewaySetting findFirstByGlImeiOrAreaCodeOrderByIdDesc(@Param("glImei") String glImei, @Param("areaCode") String areaCode);

    @Cacheable(value="gatewaySettings")
    GatewaySetting findFirstByGlImeiOrAreaCodeOrderByIdDesc(String glImei, String areaCode);

    @Override
    @SuppressWarnings("unchecked")
    @CacheEvict(value="gatewaySettings", allEntries=true) // 移除所有缓存数据
    GatewaySetting save(GatewaySetting gatewaySetting);

}
