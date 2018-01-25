package com.aiyolo.entity;

import javax.persistence.Entity;
import java.io.Serializable;


/**
 * @author 穆书伟
 * @description 盒子和店铺之间的绑定关系
 */
@Entity
public class Bind  extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer deviceid;

    private Integer shopid;

    private Integer status;

    @Override
    public String toString() {
        return "Bind{" +
                "deviceid=" + deviceid +
                ", shopid=" + shopid +
                ", status=" + status +
                '}';
    }

    public Integer getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(Integer deviceid) {
        this.deviceid = deviceid;
    }

    public Integer getShopid() {
        return shopid;
    }

    public void setShopid(Integer shopid) {
        this.shopid = shopid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}