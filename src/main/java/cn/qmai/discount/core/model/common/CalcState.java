package cn.qmai.discount.core.model.common;

import cn.qmai.discount.core.model.goods.GoodsItem;
import lombok.Data;
import java.io.Serializable;
import java.util.Map;

/**
 * 保存点，用于缓存，入计算到第3个节点时可以将所有状态写入CalcState
 * @author: shiyafeng
 * @date: 2022/8
 */
@Data
public class CalcState<T extends GoodsItem> implements Serializable {

    /**
     * 截止到保存点计算的最终优惠价格
     */
    private long curPrice;

    /**
     * 截止到保存点计算的CalcStage
     */
    private CalcStage[] curStages;

    /**
     * 截止到保存点的商品列表
     */
    private Map<Long, T> goodsItems;

    /**
     * 截止到保存点的已计算商品列表
     */
    private Map<Long,T> records;

    /**
     * 截止到保存点的扩展信息
     */
    private Map<String,Object> extra;
}
