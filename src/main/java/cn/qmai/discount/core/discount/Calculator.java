package cn.qmai.discount.core.discount;

import cn.qmai.discount.core.model.common.DiscountContext;
import cn.qmai.discount.core.model.common.DiscountWrapper;
import cn.qmai.discount.core.model.goods.GoodsItem;
import java.util.Map;

/**
 * 优惠计算器接口
 * @author: shiyafeng
 * @date: 2022/8
 */
public interface Calculator<T extends GoodsItem> {

   /**
    * 优惠计算引擎的内部方法
    * @param context  上下文
    * @param discountWrapper 当前优惠信息
    * @param records  参与优惠的商品记录，用于过滤
    * @param idx  活动的index
    * @param i  当前计算的索引下标，它和idx的区别在于比如有数组[9,4,6,5],则i为数组下标 i=1 时对应的idx是4
    */
    long calcWarp(DiscountContext<T> context, DiscountWrapper discountWrapper, Map<Long, T> records, byte idx, int i);
}
