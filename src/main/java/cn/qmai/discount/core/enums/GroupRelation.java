package cn.qmai.discount.core.enums;

/**
 * 组内关系枚举
 * @author: shiyafeng
 * @date: 2022/8
 */
public enum GroupRelation {

    SHARE("share"),
    EXCLUDE("exclude");

    private String type;

    GroupRelation(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
