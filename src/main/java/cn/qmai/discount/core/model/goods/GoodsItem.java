/*
 * Copyright 2022 CyrilFeng
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

import cn.qmai.discount.core.utils.IdGenerator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 拆分后的商品，比如一个商品GoodsInfo买了2件则会产出2个GoodsItem
 * @author: CyrilFeng
 * @date: 2022/7
 */
@Data
public class GoodsItem extends GoodsInfo implements Serializable, Comparable<GoodsItem>,Cloneable {

    /**
     * 根据基本信息构造具体的计算商品，复制属性，以及单商品唯一id
     * @param calculateId
     * @param goodsInfo
     */
    public GoodsItem (long calculateId, GoodsInfo goodsInfo) {
        this.calculateId = calculateId;
        this.setGoodsId(goodsInfo.getGoodsId());
        this.setCategoryIds(goodsInfo.getCategoryIds());
        this.setName(goodsInfo.getName());
        this.setSkuId(goodsInfo.getSkuId());
        this.setSalePrice(goodsInfo.getSalePrice());
    }

    /**
     * 引擎计算时使用的id，每个单独商品一个
     */
    private Long calculateId;

    /**
     * 商品扩展属性
     */
    private Map<String,Object> extra = Maps.newHashMap();

    /**
     * 根据商品信息（数量），生成计算时候需要的具体商品（单个）
     * @param goodsInfo 商品信息
     * @param idGenerator id生成器
     * @param consumer 在创建商品的同时对extra中的内容进行初始化
     */
    @SuppressWarnings("unchecked")
    public static <T extends GoodsItem> List<T> generateItems(GoodsInfo goodsInfo, IdGenerator idGenerator, Consumer<T> consumer) {
        if (Objects.isNull(goodsInfo)) {
            return Lists.newArrayList();
        }
        List<T> goodsItems = Lists.newArrayList();
        for (int i = 0; i < goodsInfo.getNum(); i++) {
            T item = (T)new GoodsItem(idGenerator.nextId(),goodsInfo);
            consumer.accept(item);
            goodsItems.add(item);
        }
        return goodsItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GoodsItem goodsItem = (GoodsItem) o;
        return Objects.equals(calculateId,goodsItem.calculateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(calculateId);
    }

    @Override
    public int compareTo(GoodsItem o) {
        return this.getCalculateId().compareTo(o.getCalculateId());
    }

    @Override
    public GoodsItem clone() throws CloneNotSupportedException {
        return (GoodsItem)super.clone();
    }

}
