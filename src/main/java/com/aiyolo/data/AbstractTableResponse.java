package com.aiyolo.data;

import com.aiyolo.entity.BaseEntity;
import org.springframework.data.domain.Page;

/**
 * Created by xujun on 2017/4/11.
 */
public abstract class AbstractTableResponse implements TableResponse {

    @Override
    public <R extends AbstractTableRequest, E extends BaseEntity> void render(R request, Page<E> page) {
        render(request, page, page.getContent().toArray());
    }

}
