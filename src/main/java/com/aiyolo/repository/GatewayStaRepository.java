package com.aiyolo.repository;

import com.aiyolo.entity.GatewaySta;
import org.springframework.data.repository.CrudRepository;

public interface GatewayStaRepository extends CrudRepository<GatewaySta, Long> {

    GatewaySta findFirstByGlIdOrderByIdDesc(String glId);

}
