package com.aiyolo.repository;

import com.aiyolo.entity.GatewayStatus;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GatewayStatusRepository extends CrudRepository<GatewayStatus, Long> {

    List<GatewayStatus> findByGlImei(String glImei);

    @Query("select s from GatewayStatus s where s.glImei = ?1 and s.date = ?2")
    List<GatewayStatus> findByGlImeiDate(String glImei, String date);

    @Query("select s from GatewayStatus s where s.glImei = ?1 and s.date = ?2 and s.hour in ?3")
    List<GatewayStatus> findByGlImeiDateHours(String glImei, String date, String[] hours);

    @Cacheable(value="gatewayStatuses", unless="#result == null")
    GatewayStatus findFirstByGlIdOrderByIdDesc(String glId);

    @Cacheable(value="gatewayStatuses", unless="#result == null")
    GatewayStatus findFirstByGlImeiOrderByIdDesc(String glImei);

    @Override
    @SuppressWarnings("unchecked")
    @Caching(put = { @CachePut(value="gatewayStatuses", key="#p0.getGlId()"), @CachePut(value="gatewayStatuses", key="#p0.getGlImei()") })
    GatewayStatus save(GatewayStatus gatewayStatus);

}
