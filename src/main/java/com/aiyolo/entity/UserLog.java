package com.aiyolo.entity;

import javax.persistence.Entity;

@Entity
public class UserLog extends BaseEntity {

    private String username = "";
    private String uri = "";
    private String args = "";
    private String ip = "";

    public UserLog() {}

    public UserLog(String username, String uri, String args, String ip) {
        this.username = username;
        this.uri = uri;
        this.args = args;
        this.ip = ip;
    }

    @Override
    public String toString() {
        return String.format(
                "UserLog[id=%d, username='%s', uri='%s', args='%s', ip='%s']",
                id, username, uri, args, ip);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

}
