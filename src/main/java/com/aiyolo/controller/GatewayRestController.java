package com.aiyolo.controller;

import com.aiyolo.common.ArrayHelper;
import com.aiyolo.common.BaiduMapHelper;
import com.aiyolo.entity.Gateway;
import com.aiyolo.entity.GatewaySta;
import com.aiyolo.repository.GatewayRepository;
import com.aiyolo.repository.GatewayStaRepository;
import com.aiyolo.service.AreaCodeService;
import com.aiyolo.service.GatewayService;
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
@RequestMapping("/gateway")
public class GatewayRestController {

    @Autowired GatewayRepository gatewayRepository;
    @Autowired GatewayStaRepository gatewayStaRepository;

    @Autowired GatewayService gatewayService;
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
        Page<Gateway> page = gatewayService.getPageGatewayByAreaCodeAndAddress(pageable, areaCode, address);

        List<Gateway> records = page.getContent();
        List<Gateway> _rec_y = new ArrayList<Gateway>();
        List<Gateway> _rec_n = new ArrayList<Gateway>();
        for (int i = 0; i < records.size(); i++) {
            if (StringUtils.isEmpty(records.get(i).getLocationAreaCode()) && !StringUtils.isEmpty(records.get(i).getGlLongitude())) {
                Map<String, String> locationAddress = BaiduMapHelper.getAddressByLocation(records.get(i).getGlLongitude(), records.get(i).getGlLatitude());
                if (locationAddress != null) {
                    records.get(i).setLocationAreaCode(locationAddress.get("areaCode"));
                    records.get(i).setLocationAddress(locationAddress.get("address"));
                    gatewayRepository.save(records.get(i));
                }
            }

            if (StringUtils.isEmpty(records.get(i).getLocationAreaCode()) || StringUtils.isEmpty(records.get(i).getLocationAddress())) {
                // 读取基站地址
                GatewaySta gatewaySta = gatewayStaRepository.findFirstByGlImeiOrderByIdDesc(records.get(i).getGlImei());
                if (gatewaySta != null && !StringUtils.isEmpty(gatewaySta.getLocationAreaCode()) && !StringUtils.isEmpty(gatewaySta.getLocationAddress())) {
                    records.get(i).setLocationAreaCode(gatewaySta.getLocationAreaCode());
                    records.get(i).setLocationAddress(gatewaySta.getLocationAddress());
                    gatewayRepository.save(records.get(i));
                }
            }

            // 添加网关管理员手机至userPhones
            String managerPhone = gatewayService.getManagerPhone(records.get(i).getGlImei());
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
        List<Gateway> _rec_final = new ArrayList<Gateway>();
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
