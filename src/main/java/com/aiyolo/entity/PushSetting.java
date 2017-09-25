package com.aiyolo.entity;

import javax.persistence.Entity;

@Entity
public class PushSetting extends BaseEntity {

    private String title = "";
    private String content = "";
    private Integer type = 1;
    private String target = "";

    protected PushSetting() {}

    public PushSetting(String title, String content, Integer type, String target) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.target = target;
    }

    @Override
    public String toString() {
        return String.format(
                "PushSetting[id=%d, title='%s', content='%s', type=%d, target='%s']",
                id, title, content, type, target);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

}
