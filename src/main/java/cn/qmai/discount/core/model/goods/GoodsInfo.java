package cn.qmai.discount.core.model.goods;

import com.google.common.collect.Maps;
import lombok.Data;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 商品信息
 * @author: longlian
 * @date: 2022/7
 */
@Data
public class GoodsInfo implements Serializable,Cloneable {

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品的SKUID
     */
    private Long skuId;

    /**
     * 商品分类ID,一个商品可能出现在多个品类中
     */
    private List<Long> categoryIds;

    /**
     * 购买数量
     */
    private int num;

    /**
     * 商品价格，用于计算（单位：分）
     */
    private long salePrice;

    /**
     * 商品名
     */
    private String name;

    /**
     * 商品扩展属性
     */
    private Map<String,Object> goodsExtra = Maps.newHashMap();


    public static GoodsInfo of(Long goodsId,Long skuId,List<Long> categoryIds,int num,long salePrice,String name,Map<String,Object> goodsExtra){
        GoodsInfo goodsInfo = new GoodsInfo();
        goodsInfo.setGoodsId(goodsId);
        goodsInfo.setCategoryIds(categoryIds);
        goodsInfo.setNum(num);
        goodsInfo.setSkuId(skuId);
        goodsInfo.setSalePrice(salePrice);
        goodsInfo.setName(name);
        goodsInfo.setGoodsExtra(goodsExtra);
        return goodsInfo;
    }

    @Override
    public GoodsInfo clone() throws CloneNotSupportedException {
        return (GoodsInfo)super.clone();
    }
}
