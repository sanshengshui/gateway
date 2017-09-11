package com.aiyolo.constant;

public enum ProductEnum {

    /**
     * 测试环境：40001040010001
     * 生产环境：40001070010003
     */
    ALARM("40001070010003", "智能报警器");

    private String id;
    private String name;

    private ProductEnum(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format(
                "Product[id='%s', name='%s']",
                id, name);
    }

}
