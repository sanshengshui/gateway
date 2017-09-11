package com.aiyolo.service.api.response;

import java.util.Map;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.service.api.request.RequestObject;

public class LocalInfoResponse extends Response {

    private String temperature;
    private String weather;
    private String humidity;
    private String city;

    public LocalInfoResponse() {
    }

    public LocalInfoResponse(RequestObject request, Map<String, String> localInfo) {
        super(request.getAction(), ApiResponseStateEnum.SUCCESS);

        this.temperature = localInfo.get("temperature");
        this.weather = localInfo.get("weather");
        this.humidity = localInfo.get("humidity");
        this.city = localInfo.get("city");
    }

    @Override
    public String toString() {
        return super.toString() + ", LocalInfoResponse{" +
                "temperature='" + temperature + '\'' +
                ", weather='" + weather + '\'' +
                ", humidity='" + humidity + '\'' +
                ", city='" + city + '\'' +
                '}';
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
