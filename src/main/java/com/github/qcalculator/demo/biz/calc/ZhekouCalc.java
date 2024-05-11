package com.github.qcalculator.demo.biz.calc;

import com.github.qcalculator.core.discount.impl.AbstractCalculator;
import com.github.qcalculator.core.model.common.CalcStage;
import com.github.qcalculator.core.model.common.DiscountContext;
import com.github.qcalculator.core.model.common.DiscountWrapper;
import com.github.qcalculator.core.model.goods.GoodsItem;
import com.github.qcalculator.demo.biz.constant.Constant;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component("zhekou")
@SuppressWarnings("unchecked")
public class ZhekouCalc extends AbstractCalculator<GoodsItem> {

    @Override
    public long calc(DiscountContext<GoodsItem> context, DiscountWrapper discountWrapper, Map<Long, GoodsItem> records, long prevStagePrice, CalcStage curStage) {
        List<GoodsItem> items = Lists.newArrayList(context.getDiscountItemGroup().get(discountWrapper.getId()));
        Map<Long, List<GoodsItem>> map = (Map<Long, List<GoodsItem>>)context.getPreCompute().get("GroupBySkuIdPreCompute");
            long maxPrice = 0L;
            Collection<GoodsItem> group = CollectionUtils.EMPTY_COLLECTION;
            for(Collection<GoodsItem> c:map.values()){
                long tmp = (long)c.iterator().next().getExtra().get(Constant.UPDATEABLEPRICE);
                if(tmp>maxPrice){
                    maxPrice=tmp;
                    group = c;
                }
            }
          long discount = (long)(maxPrice*group.size()*0.2);
            //均摊
          for (GoodsItem item : group) {
               item.getExtra().put(Constant.UPDATEABLEPRICE, (long) item.getExtra().get(Constant.UPDATEABLEPRICE) - discount / items.size());
          }
            return prevStagePrice-discount;
    }
}
