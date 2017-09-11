package com.aiyolo.service.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Request extends RequestObject {

    public Request() {
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
