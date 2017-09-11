package com.aiyolo.entity;

import javax.persistence.Entity;

@Entity
public class AppUserSession extends BaseEntity {

    private String userId = "";
    private String mobileId = "";
    private String cid = "";
    private String session = "";

    protected AppUserSession() {}

    public AppUserSession(String userId, String mobileId, String cid, String session) {
        this.userId = userId;
        this.mobileId = mobileId;
        this.cid = cid;
        this.session = session;
    }

    @Override
    public String toString() {
        return String.format(
                "AppUserSession[id=%d, user_id='%s', mobile_id='%s', cid='%s', session='%s']",
                id, userId, mobileId, cid, session);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobileId() {
        return mobileId;
    }

    public void setMobileId(String mobileId) {
        this.mobileId = mobileId;
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

}
