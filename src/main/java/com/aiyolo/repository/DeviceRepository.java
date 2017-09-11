package com.aiyolo.repository;

import java.util.List;

import com.aiyolo.entity.Device;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface DeviceRepository extends PagingAndSortingRepository<Device, Long> {

    List<Device> findByAreaCode(String areaCode);

    List<Device> findByAreaCodeStartingWith(String areaCode);

    @Query("select d from Device d where regexp(areaCode, :areaCodePat)=1")
    List<Device> findByAreaCodeMatch(@Param("areaCodePat") String areaCodePat);

    Page<Device> findPageByAddressLike(Pageable pageable, String address);

    @Query(
            value = "select d from Device d where regexp(areaCode, :areaCodePat)=1",
            countQuery = "select count(d) from Device d where regexp(areaCode, :areaCodePat)=1"
            )
    Page<Device> findPageByAreaCodeMatch(Pageable pageable, @Param("areaCodePat") String areaCodePat);

    @Query(
            value = "select d from Device d where regexp(areaCode, :areaCodePat)=1 and address like %:address%",
            countQuery = "select count(d) from Device d where regexp(areaCode, :areaCodePat)=1 and address like %:address%"
            )
    Page<Device> findPageByAreaCodeAndAddressMatch(
            Pageable pageable,
            @Param("areaCodePat") String areaCodePat,
            @Param("address") String address);

    @Cacheable(value="devices", unless="#result == null")
    Device findFirstByGlIdOrderByIdDesc(String glId);

    @Cacheable(value="devices", unless="#result == null")
    Device findFirstByGlImeiOrderByIdDesc(String glImei);

    @Override
    @SuppressWarnings("unchecked")
    @Caching(put = { @CachePut(value="devices", key="#p0.getGlId()"), @CachePut(value="devices", key="#p0.getGlImei()") })
    Device save(Device device);

}
