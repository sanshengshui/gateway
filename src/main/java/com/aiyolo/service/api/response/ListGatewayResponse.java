package com.aiyolo.service.api.response;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.service.api.request.RequestObject;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ListGatewayResponse extends Response {

    private List<GatewayObject> gateways;

    public ListGatewayResponse(RequestObject request, List<GatewayObject> gateways) {
        super(request.getAction(), gateways.size() == 0 ? ApiResponseStateEnum.ERROR_NONE_DATA : ApiResponseStateEnum.SUCCESS);

        this.gateways = gateways;
    }

    @Override
    public String toString() {
        return super.toString() + ", ListGatewayResponse{" +
                "gateways='" + gateways.toString() + '\'' +
                '}';
    }

    public List<GatewayObject> getGateways() {
        return gateways;
    }

    public void setGateways(List<GatewayObject> gateways) {
        this.gateways = gateways;
    }

}
