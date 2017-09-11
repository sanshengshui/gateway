package com.aiyolo.repository;

import java.util.List;

import com.aiyolo.constant.Role4DeviceEnum;
import com.aiyolo.entity.AppUserDevice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface AppUserDeviceRepository extends CrudRepository<AppUserDevice, Long> {

    List<AppUserDevice> findByGlImei(String glImei);

    List<AppUserDevice> findByUserId(String userId);

    // 获取管理员设备
    List<AppUserDevice> findByUserIdAndRole(String userId, Role4DeviceEnum role);

    AppUserDevice findOneByUserIdAndGlImei(String userId, String glImei);

    // 获取设备管理员
    AppUserDevice findOneByGlImeiAndRole(String glImei, Role4DeviceEnum role);

    Page<AppUserDevice> findPageByGlImei(Pageable pageable, String glImei);

    @Modifying
    @Transactional
    @Query("update AppUserDevice a set a.role = ?2 where a.glImei = ?1")
    void updateRoleByGlImei(String glImei, Role4DeviceEnum role);

}
