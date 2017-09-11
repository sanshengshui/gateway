package com.aiyolo.entity;

import javax.persistence.Entity;

@Entity
public class News extends BaseEntity {

    private String title = "";
    private String content = "";
    private Integer top = 0;
    private Integer status = 1;

    public News() {}

    public News(String title, String content, Integer top) {
        this.title = title;
        this.content = content;
        this.top = top;
    }

    @Override
    public String toString() {
        return String.format(
                "News[id=%d, title='%s', content='%s', top=%d, status=%d]",
                id, title, content, top, status);
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

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
