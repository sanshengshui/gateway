package com.aiyolo.data;

import com.aiyolo.entity.BaseEntity;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * Created by xujun on 2017/3/15.
 */
public class SimplePageResponse {

    public static <R extends AbstractTableRequest, E extends BaseEntity, T extends AbstractTableResponse> Map<String, Object> data(
            R request,
            Page<E> page) {
        T t = request.getResponse();
        t.render(request, page);
        return t.toMap();
    }

    public static <R extends AbstractTableRequest, E extends BaseEntity, T extends AbstractTableResponse> Map<String, Object> data(
            R request,
            Page<E> page,
            Object[] records) {
        T t = request.getResponse();
        t.render(request, page, records);
        return t.toMap();
    }

}
