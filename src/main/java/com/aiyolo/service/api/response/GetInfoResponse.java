package com.aiyolo.service.api.response;

import com.aiyolo.common.ArrayHelper;
import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.entity.Gateway;
import com.aiyolo.service.api.request.RequestObject;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import net.sf.json.JSONArray;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GetInfoResponse extends Response {

    private String glName;
    private String userName;
    private String[] phones;
    private String imei;
    private String areaCode;
    private String village;
    private String address;

    public  GetInfoResponse(RequestObject request, Gateway gateway) {
        super(request.getAction(), ApiResponseStateEnum.SUCCESS);

        this.glName = gateway.getGlName();
        this.userName = gateway.getUserName();
        this.phones = ArrayHelper.validJsonArray(gateway.getUserPhones()) ?
                ArrayHelper.getStringArray(JSONArray.fromObject(gateway.getUserPhones())) : new String[0];
        this.imei = gateway.getGlImei();
        this.areaCode = gateway.getAreaCode();
        this.village = gateway.getVillage();
        this.address = gateway.getAddress();
    }

    @Override
    public String toString() {
        return super.toString() + ", GetInfoResponse{" +
                "gl_name='" + glName + '\'' +
                ", user_name='" + userName + '\'' +
                ", phones=" + phones +
                ", imei='" + imei + '\'' +
                ", area_code='" + areaCode + '\'' +
                ", village='" + village + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public String getGlName() {
        return glName;
    }

    public void setGlName(String glName) {
        this.glName = glName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String[] getPhones() {
        return phones;
    }

    public void setPhones(String[] phones) {
        this.phones = phones;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
