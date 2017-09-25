package com.aiyolo.repository;

import com.aiyolo.entity.PushSetting;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PushSettingRepository extends CrudRepository<PushSetting, Long> {

    List<PushSetting> findByType(int type);

    List<PushSetting> findByTarget(String target);

}
