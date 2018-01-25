package com.aiyolo.repository;

import com.aiyolo.entity.Bind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BindRepository extends JpaRepository<Bind,Long> {
    Bind findByShopidAndDeviceid(Integer shopid, Integer deviceid);
}