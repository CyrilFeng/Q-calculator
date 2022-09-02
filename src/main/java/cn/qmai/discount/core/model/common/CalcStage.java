package cn.qmai.discount.core.model.common;

import lombok.*;
import java.io.Serializable;
import java.util.Map;

/**
 * 单个具体优惠计算明细，可以当成日志来用，用于计算过程的追溯
 * @author: shiyafeng
 * @date: 2022/8
 */
@Data
public class CalcStage implements Serializable {

    /**
     * 优惠前价格
     */
    private long beforeCalcPrice;

    /**
     * 优惠后价格
     */
    private long afterCalcPrice;

    /**
     * 优惠类型
     */
    private String stageType;

    /**
     * 优惠索引
     */
    private byte index;

    /**
     * 扩展属性
     */
    private Map<String,Object> extra;

}
