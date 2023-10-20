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

package cn.qmai.discount.core.model.common;
import cn.qmai.discount.core.model.goods.GoodsItem;
import cn.qmai.discount.core.precompute.PreCompute;
import cn.qmai.discount.core.precompute.PreComputeHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 优惠上下文，包含一些初始化工作、预计算等等
 * @author: CyrilFeng
 * @date: 2022/8
 */
@Data
public class DiscountContext<T extends GoodsItem> implements Serializable {

    private DiscountContext() {
    }

    /**
     * 原始价格
     */
    private long originalPrice;

    /**
     * 优惠的计算结果
     */
    private CalcResult calcResult;


    /**
     * 当前订单参与计算的商品
     */
    private List<T> goodsItems;

    /**
     * 当前订单可用优惠的配置
     */
    private List<DiscountWrapper> discountWrappers;

    /**
     * 优惠维度进行商品分组，这样每个计算器只取自己相关的商品
     */
    private Map<String,List<T>> discountItemGroup;

    /**
     * 记录享受过优惠的单品，key是calculateId
     */
    private Map<Long,T> records;

    /**
     * 存储预计算的结果
     */
    private Map<String,Object> preCompute;

    /**
     * 扩展字段
     */
    private Map<String,Object> extra;


    /**
     * 构造一个上下文
     * @param originalPrice 订单原始价格
     * @param goodsItems    可用商品列表
     * @param discountWrappers 可用优惠列表
     * @return
     */
    public static <T extends GoodsItem> DiscountContext<T> create(long originalPrice, List<T> goodsItems, List<DiscountWrapper> discountWrappers) {
        DiscountContext<T> c=new DiscountContext<>();
        c.originalPrice = originalPrice;
        c.records = Maps.newHashMap();
        c.discountItemGroup= Maps.newHashMap();
        c.calcResult=CalcResult.create(discountWrappers.size());
        c.calcResult.setFinalPrice(originalPrice);
        c.goodsItems = goodsItems;
        c.discountWrappers=discountWrappers;
        c.preCompute = Maps.newHashMap();
        //预处理优惠
        for(DiscountWrapper discount:discountWrappers) {
                 initDiscount(c, discount);
        }
        return c;
    }


    /**
     * 对某个优惠进行初始化工作，包括商品的筛选、预计算逻辑
     * @param c 上下文
     * @param discount 当前优惠的信息
     */
    private static <T extends GoodsItem> void initDiscount(DiscountContext<T> c,DiscountWrapper discount){
        DiscountConfig conf =  discount.getDiscountConfig();
        List<T> list = Lists.newArrayList(c.goodsItems);
        if(0==conf.getGoodsType()){
            //不限制
            list=list.stream().sorted().collect(Collectors.toList());
            c.discountItemGroup.put(discount.getId(),list);
        }else if(1==conf.getGoodsType()){
            if(0==conf.getItemIdType()){
                list = list.stream().filter(x->conf.getItemIds().
                        contains(x.getGoodsId())).sorted().collect(Collectors.toList());
                c.discountItemGroup.put(discount.getId(), list);
            } else if (1==conf.getItemIdType()) {
                list = list.stream().filter(x->conf.getItemIds().
                        contains(x.getSkuId())).sorted().collect(Collectors.toList());
                c.discountItemGroup.put(discount.getId(),list);
            } else{
                list = list.stream().filter(x-> CollectionUtils.intersection(
                        conf.getItemIds(), x.getCategoryIds()).size()>0).sorted().collect(Collectors.toList());
                c.discountItemGroup.put(discount.getId(), list);
            }
        }else{
            //指定不参与
            if(0==conf.getItemIdType()){
                list=list.stream().filter(x->!conf.getItemIds().
                        contains(x.getGoodsId())).sorted().collect(Collectors.toList());
                c.discountItemGroup.put(discount.getId(), list);
            } else if (1==conf.getItemIdType()) {
                list = list.stream().filter(x->!conf.getItemIds().
                        contains(x.getSkuId())).sorted().collect(Collectors.toList());
                c.discountItemGroup.put(discount.getId(), list);
            } else{
                list=list.stream().filter(x-> CollectionUtils.intersection(
                        conf.getItemIds(), x.getCategoryIds()).size()==0).sorted().collect(Collectors.toList());
                c.discountItemGroup.put(discount.getId(), list);
            }
        }
        runPreCompute(c,discount,list);
    }

    /**
     * 预计算
     * @param c 上下文
     * @param discount 当前的优惠信息
     * @param list 当前优惠匹配的商品列表
     */
    @SuppressWarnings("unchecked")
    private static <T extends GoodsItem> void runPreCompute(DiscountContext<T> c,DiscountWrapper discount,List<T> list){
        list = Lists.newArrayList(list);
        for(PreCompute<T> e: PreComputeHolder.COMPUTES){
            if(e.matchTypes().contains(discount.getType())){
                 e.preComputeItems(list,discount,c.preCompute);
            }
        }
    }

}
