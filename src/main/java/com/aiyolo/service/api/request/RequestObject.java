package com.aiyolo.service.api.request;

public class RequestObject {

    private String action;
    private String cid;
    private String session;
    private String system;

    public RequestObject() {
    }

    @Override
    public String toString() {
        return "RequestObject{" +
                "action='" + action + '\'' +
                ", cid='" + cid + '\'' +
                ", session='" + session + '\'' +
                ", system='" + system + '\'' +
                '}';
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

}
