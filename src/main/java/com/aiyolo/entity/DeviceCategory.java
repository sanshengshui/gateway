package com.aiyolo.entity;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class DeviceCategory extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer category = 0;
    private Integer type = 0;
    private String code = "";
    private String name = "";
    private String values = "";
    private String extra = "";

    protected DeviceCategory() {}

    public DeviceCategory(Integer category, Integer type, String code, String name, String values, String extra) {
        this.category = category;
        this.type = type;
        this.code = code;
        this.name = name;
        this.values = values;
        this.extra = extra;
    }

    @Override
    public String toString() {
        return String.format(
                "DeviceCategory[id=%d, category=%d, type=%d, code='%s', name='%s', values='%s', extra='%s']",
                id, category, type, code, name, values, extra);
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

}
