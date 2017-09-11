package com.aiyolo.data;

import java.util.Map;

/**
 * Created by xujun on 2017/4/10.
 */
public interface TableRequest {

    public int getPage();

    public int getSize();

    public Map<String, String> getOrder();

    public <T extends AbstractTableResponse> T getResponse();

}
