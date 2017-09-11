package com.aiyolo.data;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Created by xujun on 2017/3/15.
 */
public class SimplePageRequest {

    public static <T extends AbstractTableRequest> Pageable getPageable(T request) {
        return new PageRequest(
                request.getPage(),
                request.getSize(),
                request.getOrder().get("direction").equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC,
                request.getOrder().get("property"));
    }

}
