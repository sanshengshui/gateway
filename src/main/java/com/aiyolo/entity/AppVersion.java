package com.aiyolo.entity;

import javax.persistence.Entity;

@Entity
public class AppVersion extends BaseEntity {

    private String system = "";
    private String version = "";
    private String url = "";
    private String md5 = "";
    private Integer force = 0;

    protected AppVersion() {}

    public AppVersion(String system, String version, Integer force) {
        this.system = system;
        this.version = version;
        this.force = force;
    }

    public AppVersion(String system, String version, String url, String md5, Integer force) {
        this.system = system;
        this.version = version;
        this.url = url;
        this.md5 = md5;
        this.force = force;
    }

    @Override
    public String toString() {
        return String.format(
                "AppVersion[id=%d, system='%s', version='%s', url='%s', md5='%s', force=%d]",
                id, system, version, url, md5, force);
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public Integer getForce() {
        return force;
    }

    public void setForce(Integer force) {
        this.force = force;
    }

}
