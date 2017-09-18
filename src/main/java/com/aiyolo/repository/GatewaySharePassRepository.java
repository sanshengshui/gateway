package com.aiyolo.repository;

import com.aiyolo.entity.GatewaySharePass;
import org.springframework.data.repository.CrudRepository;

public interface GatewaySharePassRepository extends CrudRepository<GatewaySharePass, Long> {

    GatewaySharePass findFirstByGlImeiOrderByIdDesc(String glImei);

}
