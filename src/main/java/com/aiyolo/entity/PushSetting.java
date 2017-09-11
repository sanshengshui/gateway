package com.aiyolo.entity;

import javax.persistence.Entity;

@Entity
public class PushSetting extends BaseEntity {

    private String title = "";
    private String content = "";
    private String type = "app";
    private Integer level = 0;

    protected PushSetting() {}

    public PushSetting(String title, String content, String type, Integer level) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.level = level;
    }

    @Override
    public String toString() {
        return String.format(
                "PushSetting[id=%d, title='%s', content='%s', type='%s', level=%d]",
                id, title, content, type, level);
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

}
