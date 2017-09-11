package com.aiyolo.controller;

import com.aiyolo.common.ArrayHelper;
import com.aiyolo.common.BaiduMapHelper;
import com.aiyolo.entity.Device;
import com.aiyolo.entity.DeviceSta;
import com.aiyolo.repository.DeviceRepository;
import com.aiyolo.repository.DeviceStaRepository;
import com.aiyolo.service.AreaCodeService;
import com.aiyolo.service.DeviceService;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/device")
public class DeviceRestController {

    @Autowired DeviceRepository deviceRepository;
    @Autowired DeviceStaRepository deviceStaRepository;

    @Autowired DeviceService deviceService;
    @Autowired AreaCodeService areaCodeService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map<String, Object> list(
            @RequestParam(value = "draw", defaultValue = "0") Integer draw,
            @RequestParam(value = "start", defaultValue = "0") Integer start,
            @RequestParam(value = "length", defaultValue = "20") Integer length,
            @RequestParam(value = "areaCode", defaultValue = "0") String areaCode,
            @RequestParam(value = "address", defaultValue = "") String address) {
        if (! areaCodeService.checkAreaCode(areaCode, new String[] {"0"})) {
            return null;
        }

        length = length > 0 ? length : 20;
        Sort sort = new Sort(Direction.DESC, "glLogintime");
        Pageable pageable = new PageRequest((start / length), length, sort);
        Page<Device> page = deviceService.getPageDeviceByAreaCodeAndAddress(pageable, areaCode, address);

        List<Device> records = page.getContent();
        List<Device> _rec_y = new ArrayList<Device>();
        List<Device> _rec_n = new ArrayList<Device>();
        for (int i = 0; i < records.size(); i++) {
            if (StringUtils.isEmpty(records.get(i).getLocationAreaCode()) && !StringUtils.isEmpty(records.get(i).getGlLongitude())) {
                Map<String, String> locationAddress = BaiduMapHelper.getAddressByLocation(records.get(i).getGlLongitude(), records.get(i).getGlLatitude());
                if (locationAddress != null) {
                    records.get(i).setLocationAreaCode(locationAddress.get("areaCode"));
                    records.get(i).setLocationAddress(locationAddress.get("address"));
                    deviceRepository.save(records.get(i));
                }
            }

            if (StringUtils.isEmpty(records.get(i).getLocationAreaCode()) || StringUtils.isEmpty(records.get(i).getLocationAddress())) {
                // 读取基站地址
                DeviceSta deviceSta = deviceStaRepository.findFirstByGlIdOrderByIdDesc(records.get(i).getGlId());
                if (deviceSta != null && !StringUtils.isEmpty(deviceSta.getLocationAreaCode()) && !StringUtils.isEmpty(deviceSta.getLocationAddress())) {
                    records.get(i).setLocationAreaCode(deviceSta.getLocationAreaCode());
                    records.get(i).setLocationAddress(deviceSta.getLocationAddress());
                    deviceRepository.save(records.get(i));
                }
            }

            // 添加设备管理员手机至userPhones
            String managerPhone = deviceService.getManagerPhone(records.get(i).getGlImei());
            if (! StringUtils.isEmpty(managerPhone)) {
                String[] userPhones = ArrayHelper.getStringArray(records.get(i).getUserPhones());
                String[] newUserPhones = (String[]) ArrayUtils.addAll(new String[]{managerPhone}, userPhones);
                records.get(i).setUserPhones(ArrayHelper.getArrayString(newUserPhones));
            }

            if (! records.get(i).getLocationAreaCode().equals(records.get(i).getAreaCode())) {
                _rec_n.add(records.get(i));
            } else {
                _rec_y.add(records.get(i));
            }
        }
        List<Device> _rec_final = new ArrayList<Device>();
        _rec_final.addAll(_rec_n);
        _rec_final.addAll(_rec_y);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("draw", draw);
        response.put("recordsTotal", page.getTotalElements());
        response.put("recordsFiltered", page.getTotalElements());
        response.put("data", _rec_final);
        return response;
    }

}
