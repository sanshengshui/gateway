package com.aiyolo.repository;

import com.aiyolo.constant.Role4GatewayEnum;
import com.aiyolo.entity.AppUserGateway;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AppUserGatewayRepository extends CrudRepository<AppUserGateway, Long> {

    List<AppUserGateway> findByGlImei(String glImei);

    List<AppUserGateway> findByUserId(String userId);

    // 获取管理员设备
    List<AppUserGateway> findByUserIdAndRole(String userId, Role4GatewayEnum role);

    AppUserGateway findOneByUserIdAndGlImei(String userId, String glImei);

    // 获取设备管理员
    AppUserGateway findOneByGlImeiAndRole(String glImei, Role4GatewayEnum role);

    Page<AppUserGateway> findPageByGlImei(Pageable pageable, String glImei);

    @Modifying
    @Transactional
    @Query("update AppUserGateway a set a.role = ?2 where a.glImei = ?1")
    void updateRoleByGlImei(String glImei, Role4GatewayEnum role);

}
