package cn.qmai.discount.demo.biz;

import cn.qmai.discount.core.model.common.CalcStage;
import cn.qmai.discount.core.model.common.CalcState;
import cn.qmai.discount.core.model.common.DiscountContext;
import cn.qmai.discount.core.model.goods.GoodsItem;
import cn.qmai.discount.core.permutation.Permutation;
import cn.qmai.discount.demo.biz.constant.Constant;

import java.util.List;

public class Flowable extends Permutation<GoodsItem> {
    @Override
    protected boolean enableOptimize(List<Byte> a) {
        return false;
    }

    @Override
    protected boolean sameOptimumCondition(CalcStage[] curStages) {
        return false;
    }

    @Override
    protected void resetContext(DiscountContext<GoodsItem> context) {

    }

    @Override
    protected void resetItems(GoodsItem item) {
        item.getExtra().put(Constant.UPDATEABLEPRICE,item.getSalePrice());
    }

    @Override
    protected void makeSnapshot(CalcState<GoodsItem> state, DiscountContext<GoodsItem> context) {

    }

    @Override
    protected void backToSnapshot(CalcState<GoodsItem> state, DiscountContext<GoodsItem> context) {

    }


}
