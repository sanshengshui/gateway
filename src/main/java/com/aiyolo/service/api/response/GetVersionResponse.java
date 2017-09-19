package com.aiyolo.service.api.response;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.entity.AppVersion;
import com.aiyolo.service.api.request.RequestObject;

public class GetVersionResponse extends Response {

    private String ver = "";
    private String url = "";
    private String md5 = "";
    private Boolean force = false;

    public GetVersionResponse() {
    }

    public GetVersionResponse(RequestObject request, AppVersion appVersion) {
        super(request.getAction(), appVersion == null ? ApiResponseStateEnum.ERROR_NONE_DATA : ApiResponseStateEnum.SUCCESS);

        if (appVersion != null) {
            this.ver = appVersion.getVersion();
            this.url = appVersion.getUrl();
            this.md5 = appVersion.getMd5();
            this.force = appVersion.getForce() == 1 ? true : false;
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", GetVersionResponse{" +
                "ver='" + ver + '\'' +
                ", url='" + url + '\'' +
                ", md5='" + md5 + '\'' +
                ", force=" + force +
                '}';
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Boolean getForce() {
        return force;
    }

    public void setForce(Boolean force) {
        this.force = force;
    }

}
