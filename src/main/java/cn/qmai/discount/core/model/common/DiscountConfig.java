package cn.qmai.discount.core.model.common;


import lombok.Data;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 优惠配置信息
 * @author: longlian
 * @date: 2022/7
 */
@Data
public class DiscountConfig implements Serializable {

    /**
     * 当前优惠的优先级（如10 - 单商品组， 20 - 整单组）
     */
    private int calculateGroup;

    /**
     * 商品限制 (0 - 所有商品可参与，1 - 指定某些商品参与， 2- 指定某些商品不参与)
     */
    private int goodsType;

    /**
     * 下一个属性 itemIds 存放的内容是啥，（0 - 商品ID， 1 - SKUID， 2 - 类目Id）
     */
    private int itemIdType;

    /**
     * 同上
     */
    private List<Long> itemIds;

    /**
     * 活动信息扩展信息
     */
    private Map<String,Object> extra;

}
