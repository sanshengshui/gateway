package com.aiyolo.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xujun on 2017/4/11.
 */
public abstract class AbstractTableRequest implements TableRequest {

    @Override
    public int getPage() {
        return 0;
    }

    @Override
    public int getSize() {
        return 20;
    }

    @Override
    public Map<String, String> getOrder() {
        Map<String, String> order = new HashMap<String, String>();
        order.put("direction", "DESC");
        order.put("property", "createdAt");
        return order;
    }

}
