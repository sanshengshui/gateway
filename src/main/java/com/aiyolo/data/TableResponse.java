package com.aiyolo.data;

import com.aiyolo.entity.BaseEntity;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * Created by xujun on 2017/4/10.
 */
public interface TableResponse {

    public <R extends AbstractTableRequest, E extends BaseEntity> void render(R request, Page<E> page);

    public <R extends AbstractTableRequest, E extends BaseEntity> void render(R request, Page<E> page, Object[] records);

    public Map<String, Object> toMap();

}
