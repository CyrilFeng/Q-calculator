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

package cn.qmai.discount.core.discount.impl;
import cn.qmai.discount.core.discount.Calculator;
import cn.qmai.discount.core.model.common.*;
import cn.qmai.discount.core.model.goods.GoodsItem;
import java.util.Map;

/**
 * 抽象计算器类，每种类型优惠做一个实现类，负责创建 stage，维护CalcStage[]数组等内部工作，这对使用者是透明的
 * @author: CyrilFeng
 * @date: 2022/8
 */
public abstract class AbstractCalculator<T extends GoodsItem> implements Calculator<T> {

    public  long calcWarp(DiscountContext<T> context, DiscountWrapper discountWrapper, Map<Long, T> records, byte idx, int i) {
        CalcStage stage = new CalcStage();
        CalcResult cr = context.getCalcResult();
        long price= cr.getCurPrice();
        stage.setBeforeCalcPrice(price);
        price = calc(context, discountWrapper,records, price, stage);
        if(price<0){
            return price;
        }
        stage.setAfterCalcPrice(price);
        stage.setIndex(idx);
        stage.setStageType(discountWrapper.getType());
        cr.setCurPrice(price);
        if(stage.getBeforeCalcPrice()>stage.getAfterCalcPrice()) {
            cr.getCurStages()[i] = stage;
        }
        return price;
    }

    /**
     * 返回该优惠下的最终要支付的金额,若不符合则返回 prevStagePrice
     * @param context 上下文
     * @param discountWrapper 优惠信息
     * @param records 记录享受过优惠的单品，key是calculateId，这里只提供容器，添加和判断规则由使用者自行决定
     * @param prevStagePrice 上一步计算出的订单的价格
     * @param curStage 当前stage
     * @return
     */
    public abstract  long calc(DiscountContext<T> context, DiscountWrapper discountWrapper, Map<Long,T> records, long prevStagePrice, CalcStage curStage);

}
