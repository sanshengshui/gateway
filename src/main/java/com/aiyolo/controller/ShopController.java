package com.aiyolo.controller;

import com.aiyolo.channel.data.response.Resp;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description 新建门店 测试类 尚未完成逻辑处理
 * @date 2017年1月17日 下午15:02
 */
@RestController
@RequestMapping("/shop")
public class ShopController {

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public Resp createShop(
            @RequestParam("action") String action,
            @RequestParam("channelid") String channelid,
            @RequestParam("brandid") String brandid,
            @RequestParam("shop") String shop,
            @RequestParam("province") String province,
            @RequestParam("city") String city,
            @RequestParam("area") String area,
            @RequestParam("address") String address,
            @RequestParam("shopphone") String shopphone
    ){
        /**
         * shopid为新建门店之后，从数据库返回的此店铺的唯一标识
         */
        String shopid = "456";

        try {
            return Resp.ok(action,shopid);
        }catch (Exception e){
            return Resp.error(action,shopid);
        }

    }
}
