package com.aiyolo.repository;

import java.util.List;

import com.aiyolo.entity.PushSetting;
import org.springframework.data.repository.CrudRepository;

public interface PushSettingRepository extends CrudRepository<PushSetting, Long> {

    List<PushSetting> findByType(String type);

    List<PushSetting> findByLevel(int level);

}
