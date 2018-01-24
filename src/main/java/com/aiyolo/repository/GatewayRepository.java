package com.aiyolo.repository;

import com.aiyolo.entity.Gateway;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GatewayRepository extends PagingAndSortingRepository<Gateway, Long> {

    List<Gateway> findByAreaCode(String areaCode);

    List<Gateway> findByAreaCodeStartingWith(String areaCode);

    @Query("select d from Gateway d where regexp(areaCode, :areaCodePat)=1")
    List<Gateway> findByAreaCodeMatch(@Param("areaCodePat") String areaCodePat);

    List<Gateway> findByVillageLike(String village);

    @Query("select d from Gateway d where regexp(areaCode, :areaCodePat)=1 and village like %:village%")
    List<Gateway> findByAreaCodeAndVillageMatch(
            @Param("areaCodePat") String areaCodePat,
            @Param("village") String village);

    Page<Gateway> findPageByAddressLike(Pageable pageable, String address);

    @Query(
            value = "select d from Gateway d where regexp(areaCode, :areaCodePat)=1",
            countQuery = "select count(d) from Gateway d where regexp(areaCode, :areaCodePat)=1"
    )
    Page<Gateway> findPageByAreaCodeMatch(Pageable pageable, @Param("areaCodePat") String areaCodePat);

    @Query(
            value = "select d from Gateway d where regexp(areaCode, :areaCodePat)=1 and address like %:address%",
            countQuery = "select count(d) from Gateway d where regexp(areaCode, :areaCodePat)=1 and address like %:address%"
    )
    Page<Gateway> findPageByAreaCodeAndAddressMatch(
            Pageable pageable,
            @Param("areaCodePat") String areaCodePat,
            @Param("address") String address);


    @Cacheable(value = "gateways", unless = "#result == null")
    Gateway findFirstByGlImeiOrderByIdDesc(String glImei);

    @Override
    @SuppressWarnings("unchecked")
    @Caching(put = {@CachePut(value = "gateways", key = "#p0.getGlImei()")})
    Gateway save(Gateway gateway);

}
