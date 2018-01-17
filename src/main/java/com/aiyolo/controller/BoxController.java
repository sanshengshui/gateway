package com.aiyolo.controller;

import com.aiyolo.channel.data.response.Resp;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description 绑定盒子 解绑盒子 测试类 尚未完成逻辑处理
 * @date 2017年1月17日 下午14:43
 */
@RestController
@RequestMapping("/box")
public class BoxController {

    @RequestMapping(value = "/bind",method = RequestMethod.POST)
    public Resp bindBox(
            @RequestParam("action") String action,
            @RequestParam("shopid") String shopid,
            @RequestParam ("imeis[]") List<String> imeis
    ){
        return Resp.ok(action,"");

    }


    @RequestMapping(value = "/unbind",method= RequestMethod.POST)
    public Resp unbindBox(
            @RequestParam("action") String action,
            @RequestParam("shopid") String shopid,
            @RequestParam("imeis[]") List<String> imeis
    ){
        return Resp.ok(action,"");
    }
}

