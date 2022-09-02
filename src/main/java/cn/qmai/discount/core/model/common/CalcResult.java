package cn.qmai.discount.core.model.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.io.Serializable;


/**
 * 优惠计算结果
 * @author: shiyafeng
 * @date: 2022/8
 */
@Data
public class CalcResult implements Serializable {

    private CalcResult() {
    }

    /**
     * 最优实付金额
     */
    private long finalPrice;

    /**
     * 最优优惠计算过程
     */
    private  CalcStage[] stages;

    /**
     * 当前序列计算的实付金额
     */
    @JsonIgnore
    private transient long curFinalPrice;

    /**
     * 当前序列优惠计算过程
     */
    @JsonIgnore
    private transient  CalcStage[] curStages;

    /**
     *  实时计算价格
     */
    @JsonIgnore
    private transient  long curPrice;

    public static CalcResult create(int n){
        CalcResult c=new CalcResult();
        c.stages=new CalcStage[n];
        c.curStages=new CalcStage[n];
        return c;
    }

}
