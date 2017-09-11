package com.aiyolo.data;

import com.aiyolo.entity.BaseEntity;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xujun on 2017/3/15.
 */
public class DataTablesResponse extends AbstractTableResponse {

    private Integer draw = 0;
    private Long recordsTotal = 0L;
    private Long recordsFiltered = 0L;
    private Object[] data = new Object[0];

    public DataTablesResponse() {}

    public Integer getDraw() {
        return draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public Long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(Long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public Long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(Long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public Object[] getData() {
        return data;
    }

    public void setData(Object[] data) {
        this.data = data;
    }

    @Override
    public <R extends AbstractTableRequest, E extends BaseEntity> void render(R request, Page<E> page, Object[] records) {
        DataTablesRequest dataTablesRequest = (DataTablesRequest) request;
        this.draw = dataTablesRequest.getDraw();
        this.recordsTotal = page.getTotalElements();
        this.recordsFiltered = page.getTotalElements();
        this.data = records;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("draw", draw);
        result.put("recordsTotal", recordsTotal);
        result.put("recordsFiltered", recordsFiltered);
        result.put("data", data);

        return result;
    }

}
