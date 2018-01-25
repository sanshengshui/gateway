package com.aiyolo.controller;

import com.aiyolo.channel.data.response.Resp;
import com.aiyolo.entity.Bind;
import com.aiyolo.repository.BindRepository;
import com.aiyolo.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @description 绑定盒子 解绑盒子 测试类 尚未完成逻辑处理
 * @date 2017年1月17日 下午14:43
 */
@RestController
@RequestMapping("/box")
public class BoxController {
    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    BindRepository bindRepository;


    @RequestMapping(value = "/bind",method = RequestMethod.POST)
    public Resp bindBox(
            @RequestParam("action") String action,
            @RequestParam("shopid") Integer shopid,
            @RequestParam("imeis[]") List<String> imeis
    ){

        for (int i =0;i<imeis.size();i++){
            Bind bind = new Bind();
            bind.setShopid(shopid);
            Integer deviceid = deviceRepository.findByImei(imeis.get(i)).getId().intValue();
            bind.setDeviceid(deviceid);
            bind.setCreatedAt(new Date(System.currentTimeMillis()));
            bind.setStatus(1);
            bindRepository.saveAndFlush(bind);
        }
        return Resp.ok(action,"");

    }


    @RequestMapping(value = "/unbind",method= RequestMethod.POST)
    public Resp unbindBox(
            @RequestParam("action") String action,
            @RequestParam("shopid") Integer shopid,
            @RequestParam("imeis[]") List<String> imeis
    ){

        for (int i =0;i<imeis.size();i++){
            Integer deviceid = deviceRepository.findByImei(imeis.get(i)).getId().intValue();
            Long bindId = bindRepository.findByShopidAndDeviceid(shopid,deviceid).getId();
            bindRepository.delete(bindId);
        }
        return Resp.ok(action,"");
    }
}


