package com.github.qcalculator.core.enums;

public enum ItemIdType {
    ITEM(0,"商品ID"),
    SKU(1,"SKUID"),
    CATEGORY(2,"类目Id");

    private  int code;
    private String desc;

    ItemIdType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
