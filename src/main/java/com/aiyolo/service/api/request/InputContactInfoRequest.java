package com.aiyolo.service.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.stereotype.Component;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class InputContactInfoRequest extends Request {

    private String glName = "";
    private String userName = "";
    private String[] phones = new String[0];
    private String imei = "";
    private String areaCode = "";
    private String address = "";

    public InputContactInfoRequest() {
    }

    @Override
    public String toString() {
        return "InputContactInfoRequest{" +
                "gl_name='" + glName + '\'' +
                ", user_name='" + userName + '\'' +
                ", phones=" + phones +
                ", imei='" + imei + '\'' +
                ", area_code='" + areaCode + '\'' +
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
