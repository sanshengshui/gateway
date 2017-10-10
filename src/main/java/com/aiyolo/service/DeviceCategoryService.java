package com.aiyolo.service;

import com.aiyolo.entity.DeviceCategory;
import com.aiyolo.repository.DeviceCategoryRepository;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceCategoryService {

    @Autowired
    DeviceCategoryRepository deviceCategoryRepository;

    public String getDeviceValueDesc(String deviceTypeCode, Integer value) {
        if (deviceTypeCode == null || value == null) {
            return null;
        }

        DeviceCategory deviceCategory = deviceCategoryRepository.findOneByCode(deviceTypeCode);
        if (deviceCategory != null && StringUtils.isNotEmpty(deviceCategory.getValues())) {
            JSONObject valuesJson = JSONObject.fromObject(deviceCategory.getValues());
            if (!valuesJson.isNullObject()) {
                return (String) valuesJson.get(String.valueOf(value));
            }
        }

        return null;
    }

}
