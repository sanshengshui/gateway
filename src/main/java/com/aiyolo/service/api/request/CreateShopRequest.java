package com.aiyolo.service.api.request;

public class CreateShopRequest {
    private String action;
    private String channelid;
    private String brandid;
    private String shop;
    private String province;
    private String city;
    private String area;
    private String address;
    private String shopphone;

    @Override
    public String toString() {
        return "CreateShopRequest{" +
                "action='" + action + '\'' +
                ", channelid='" + channelid + '\'' +
                ", brandid='" + brandid + '\'' +
                ", shop='" + shop + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                ", address='" + address + '\'' +
                ", shopphone='" + shopphone + '\'' +
                '}';
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    public String getBrandid() {
        return brandid;
    }

    public void setBrandid(String brandid) {
        this.brandid = brandid;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShopphone() {
        return shopphone;
    }

    public void setShopphone(String shopphone) {
        this.shopphone = shopphone;
    }
}
