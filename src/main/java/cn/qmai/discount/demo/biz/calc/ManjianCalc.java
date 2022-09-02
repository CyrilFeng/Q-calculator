package cn.qmai.discount.demo.biz.calc;

import cn.qmai.discount.core.discount.impl.AbstractCalculator;
import cn.qmai.discount.core.model.common.CalcStage;
import cn.qmai.discount.core.model.common.DiscountContext;
import cn.qmai.discount.core.model.common.DiscountWrapper;
import cn.qmai.discount.core.model.goods.GoodsItem;
import cn.qmai.discount.demo.biz.constant.Constant;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component("manjian")
public class ManjianCalc extends AbstractCalculator<GoodsItem> {

    @Override
    public long calc(DiscountContext<GoodsItem> context, DiscountWrapper discountWrapper, Map<Long, GoodsItem> records, long prevStagePrice, CalcStage curStage) {
        List<GoodsItem> items = Lists.newArrayList(context.getDiscountItemGroup().get(discountWrapper.getId()));
        if(prevStagePrice>=100*100) {
            prevStagePrice = prevStagePrice - 20 * 100;
        }
        //均摊
        for(GoodsItem item:items){
            item.getExtra().put(Constant.UPDATEABLEPRICE,(long)item.getExtra().get(Constant.UPDATEABLEPRICE)-(long)(20*100/items.size()));
        }
        return prevStagePrice;
    }
}
