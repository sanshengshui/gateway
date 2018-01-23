package com.aiyolo.controller;

import com.aiyolo.channel.data.response.Resp;
import com.aiyolo.entity.Shop;
import com.aiyolo.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description 新建门店 测试类 初步完成逻辑处理
 * @date 2017年1月17日 下午15:02
 */
@RestController
@RequestMapping("/shop")
public class ShopController {
    @Autowired
    ShopRepository shopRepository;

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public Resp createShop(
            @RequestParam("action") String action,
            Shop shop
    ){
        try {
            shopRepository.save(shop);
            String shopid = String.valueOf(shop.getId());
            System.out.println(shop.toString());
            return Resp.ok(action,shopid);
        }catch (Exception e){
            return Resp.error(action);
        }

    }
}
