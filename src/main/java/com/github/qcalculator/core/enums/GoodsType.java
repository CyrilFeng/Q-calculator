package com.github.qcalculator.core.enums;

public enum GoodsType {
    ALL(0,"所有商品可参与"),
    SELECT(1,"指定某些商品参与"),
    INVERT(2,"指定某些商品不参与");

    private  int code;
    private String desc;

    GoodsType(int code, String desc) {
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
