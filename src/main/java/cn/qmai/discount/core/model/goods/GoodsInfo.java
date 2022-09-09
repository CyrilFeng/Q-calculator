/*
 * Copyright 2022 Shiyafeng
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
