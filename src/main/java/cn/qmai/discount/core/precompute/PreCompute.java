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

package cn.qmai.discount.core.precompute;

import cn.qmai.discount.core.model.common.DiscountWrapper;
import cn.qmai.discount.core.model.goods.GoodsItem;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 预计算，把一些耗时操作放在Context中操作
 * @author: CyrilFeng
 * @date: 2022/8
 */
public interface PreCompute<T extends GoodsItem> {

    /**
     * 判断符合条件的活动类型，符合才会执行preComputeItems
     */
    Set<String> matchTypes();

    /**
     * 对商品做一些复杂集合操作
     * @param items 当前参与优惠的商品
     * @param discount 当前优惠
     * @param preCompute 存储计算的结果
     */
     void preComputeItems(List<T> items, DiscountWrapper discount, Map<String,Object> preCompute);
}
