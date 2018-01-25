package com.aiyolo.controller;

import com.aiyolo.channel.data.response.Resp;
import com.aiyolo.common.HttpUtils;
import com.aiyolo.constant.Constants;
import com.aiyolo.entity.Shop;
import com.aiyolo.repository.ShopRepository;
import com.aiyolo.service.api.request.CreateShopRequest;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

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
    ) throws Exception {
        if (shopRepository.findByName(shop.getName()).size() != 0) {
            return Resp.error(action);
        }
        CreateShopRequest shopRequest = new CreateShopRequest();
        shopRequest.setAction(action);
        shopRequest.setChannelid(String.valueOf(shop.getChannelid()));
        shopRequest.setBrandid(shop.getBrandid());
        shopRequest.setShop(shop.getName());
        shopRequest.setProvince(shop.getProvince());
        shopRequest.setCity(shop.getCity());
        shopRequest.setArea(shop.getArea());
        shopRequest.setAddress(shop.getAddress());
        shopRequest.setShopphone(shop.getShopphone());
        JSONObject data = JSONObject.fromObject(shopRequest);
        byte[] dataStr = data.toString().getBytes();
        String response = HttpUtils.postHttpOrHttps("http://test.igelian.com:8090/gf/s/u", dataStr, Constants.HTTP_TIMEOUT);
        System.out.println(JSONObject.fromObject(response).get("shopid").toString());
        if (JSONObject.fromObject(response).get("des").toString().equals("成功")) {
            Long shopid = Long.parseLong(JSONObject.fromObject(response).get("shopid").toString());
            shop.setId(shopid);
            shop.setUpdatedAt(new Date(System.currentTimeMillis()));
            shopRepository.save(shop);
            return Resp.ok(action, shopid);

        }else {
            return Resp.error(action);
        }

    }

}

