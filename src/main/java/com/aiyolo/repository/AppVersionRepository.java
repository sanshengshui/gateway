package com.aiyolo.repository;

import com.aiyolo.entity.AppVersion;
import org.springframework.data.repository.CrudRepository;

public interface AppVersionRepository extends CrudRepository<AppVersion, Long> {

    AppVersion findFirstBySystemOrderByIdDesc(String system);

}
