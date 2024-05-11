package com.github.qcalculator.demo.biz;

import com.github.qcalculator.core.model.common.CalcStage;
import com.github.qcalculator.core.model.common.CalcState;
import com.github.qcalculator.core.model.common.DiscountContext;
import com.github.qcalculator.core.model.goods.GoodsItem;
import com.github.qcalculator.core.permutation.Permutation;
import com.github.qcalculator.demo.biz.constant.Constant;

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
