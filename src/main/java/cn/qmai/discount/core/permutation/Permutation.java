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

package cn.qmai.discount.core.permutation;

import cn.qmai.discount.core.aware.CalculatorRouter;
import cn.qmai.discount.core.discount.Calculator;
import cn.qmai.discount.core.model.common.*;
import cn.qmai.discount.core.model.goods.GoodsItem;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * 全排列计算引擎
 * @author: CyrilFeng
 * @date: 2022/8
 */

@SuppressWarnings("all")
public abstract class Permutation<T extends GoodsItem> {

    /**
     * 根据注解路由到目标Calculator
     */
    private CalculatorRouter calculatorRouter;

    /**
     * 上下文
     */
    private DiscountContext<T> context;

    /**
     * 必须使用的优惠，比如用户手动选择的优惠
     */
    private final Set<Byte> mustUseSet = Sets.newHashSet();

    /**
     * 缓存，key是calcKey生成的数字，value是保存点
     */
    private final Map<Integer, CalcState<T>> cache = Maps.newHashMap();

    /**
     * 将calculatorRouter绑定到当前Permutation
     * @param calculatorRouter 路由器
     * @return
     */
    public Permutation<T> build(CalculatorRouter calculatorRouter){
        this.calculatorRouter=calculatorRouter;
        return this;
    }

    /**
     *  1-SUPPORTEDSIZE 之间所有排列组合的记录
     */
    private final static Map<Integer,Collection<List<Byte>>> PERMUTATIONS = Maps.newHashMap();

    /**
     * 最大支持的共享组长度
     */
    public final static int SUPPORTEDSIZE = 7;

    static{
        //前置计算 1-SUPPORTEDSIZE 之间所有排列组合
        for(byte i=1;i<=SUPPORTEDSIZE;i++){
            PERMUTATIONS.put((int)i,Collections2.permutations(IntStream.range(0,i).boxed()
                    .map(x->(byte)x.intValue()).collect(Collectors.toList())));
        }
    }

    /**
     * 计算用户选择的优惠组合是否有可行解
     * @param context 上下文
     * @return
     */
    public boolean findSolution(DiscountContext<T> context){
        int size=context.getDiscountWrappers().size();
        this.context=context;
        if(size==0){
            return false;
        }
        Collection<List<Byte>> list = PERMUTATIONS.get(size);
        for(List<Byte> a:list){
            if(findSolution(context,a)){
                return true;
            }
        }
        cache.clear();
        mustUseSet.clear();
        return false;
    }

    /**
     * 计算最优解
     * @param context 上下文
     * @return
     */
    public void perm(DiscountContext<T> context){
        int size=context.getDiscountWrappers().size();
        this.context = context;
        loadMustUseDiscount();
        if(size==0){
            return ;
        }
        Collection<List<Byte>> list = PERMUTATIONS.get(size);
        for(List<Byte> a:list) {
            boolean isBetter = executeCalc(this.context, a);
            if (isBetter) {
                //若出现比当前结果更优的结果则替换
                updateRecord(this.context.getCalcResult());
            }
        }
        cache.clear();
        mustUseSet.clear();
    }

    private void loadMustUseDiscount(){
        for(int i=0;i<context.getDiscountWrappers().size();i++){
            if(context.getDiscountWrappers().get(i).isMustUse()){
                this.mustUseSet.add((byte)i);
            }
        }
    }

    /**
     * 根据byte数组生成唯一数字作为Map的key，避免字符串拼接等低效操作
     */
    private static Integer calcKey(List<Byte> a){
        return  a.size()>=3?(a.get(0) << 6)+ (a.get(1) << 3) + a.get(2):0;
    }

    /**
     * 若出现比当前结果更优的结果则替换
     * @param result 当前的计算结果
     */
    private void updateRecord(CalcResult result){
        result.setFinalPrice(result.getCurFinalPrice());
        System.arraycopy(result.getCurStages(),0,result.getStages(),0,result.getStages().length);
    }

    /**
     * 找到可行解，这种情况list中所有优惠必须被使用
     * @param context 上下文
     * @param a 当前计算的排列
     */
    private boolean findSolution(DiscountContext<T> context,List<Byte> a){
        Integer k = calcKey(a);
        boolean canOptimize = enableOptimize(a)&&cache.containsKey(k);
        initInner(canOptimize,k);
        for(int i=canOptimize?3:0;i<a.size();i++){
            DiscountWrapper wrapper = context.getDiscountWrappers().get(a.get(i));
            Calculator<T> calculator = (Calculator<T>)calculatorRouter.getService(wrapper.getType());
            //路由目标的计算器实现
            if(canOptimize&&checkIfWakeUpJump(context.getDiscountWrappers().get(a.get(2)),wrapper)){
                //还原保存点后，比较保存点的最后一个优惠节点和当前优惠的优先级，如不符合则跳出
                break;
            }
            if (Objects.nonNull(calculator)) {
                //执行计算器
                if(!calcInner(calculator,wrapper,a,i)){
                    return false;
                }
                CalcStage cs = context.getCalcResult().getCurStages()[i];
                if(Objects.isNull(cs)){
                    //执行当前排列，若存在未使用的优惠则证明当前排列不可行
                    return false;
                }
                //优惠长度为5、6、7 将开启优化，只缓存走到第3个节点的部分
                cacheSnapshot(a,i,k);
            }
        }
        return true;
    }

    private void cacheSnapshot(List<Byte> a,int i,Integer k){
        if(enableOptimize(a)&&i==2&&!cache.containsKey(k)){
            cache.put(k,makeSnapshot(context.getGoodsItems()));
        }
    }

    private void initInner(boolean canOptimize,Integer k){
        if(canOptimize){
            //若可优化则还原之前的保存点
            backToSnapshot(k);
        }else{
            //若不可优化则老老实实初始化
            initCtx();
        }
    }

    private boolean calcInner(Calculator<T> calculator,DiscountWrapper wrapper,List<Byte> a,int i){
        long price = calculator.calcWarp(context, wrapper, context.getRecords(), a.get(i), i);
        if (price < 0) {
            return false;
        }
        if(i<a.size()-1) {
            int order = wrapper.getDiscountConfig().getCalculateGroup();
            int nextOrder = context.getDiscountWrappers().get(a.get(i+1)).getDiscountConfig().getCalculateGroup();
            if(order>nextOrder){
                //优先级不符合则跳出
                return false;
            }
        }
        return true;
    }

    /**
     * 根据数组顺序执行计算器
     */
    public boolean executeCalc(DiscountContext<T> context,List<Byte> a){
        Integer k = calcKey(a);
        boolean canOptimize = enableOptimize(a)&&cache.containsKey(k);
        initInner(canOptimize,k);
        for(int i=canOptimize?3:0;i<a.size();i++){
            DiscountWrapper wrapper = context.getDiscountWrappers().get(a.get(i));
            Calculator<T> calculator = (Calculator<T>) calculatorRouter.getService(wrapper.getType());
            //路由目标的计算器实现
            if(canOptimize&&checkIfWakeUpJump(context.getDiscountWrappers().get(a.get(2)),wrapper)){
                //还原保存点后，比较保存点的最后一个优惠节点和当前优惠的优先级，如不符合则跳出
                break;
            }
            if(Objects.nonNull(calculator)){
                //执行计算器
                if(!calcInner(calculator,wrapper,a,i)){
                    return false;
                }
                //优惠长度为5、6、7 将开启优化，只缓存走到第3个节点的部分
                cacheSnapshot(a,i,k);
            }
        }
        long curPrice = context.getCalcResult().getCurPrice();
        context.getCalcResult().setCurFinalPrice(curPrice);
        CalcStage[] stages = context.getCalcResult().getCurStages();
        return curPrice<context.getCalcResult().getFinalPrice()&&(CollectionUtils.isEmpty(this.mustUseSet) || isMustUse(stages, this.mustUseSet))||
                //相同最优解处理逻辑
                curPrice==context.getCalcResult().getFinalPrice()&&(CollectionUtils.isEmpty(this.mustUseSet) || isMustUse(stages, this.mustUseSet))&&sameOptimumCondition(stages);
    }

    /**
     * 还原保存点后，比较保存点的最后一个优惠节点和当前优惠的优先级，如不符合则跳出
     * @param cachedWrap 保存点的最后一个优惠节点
     * @param wrapper 当前优惠节点
     */
    private boolean checkIfWakeUpJump(DiscountWrapper cachedWrap,DiscountWrapper wrapper){
        return cachedWrap.getDiscountConfig().getCalculateGroup()>wrapper.getDiscountConfig().getCalculateGroup();
    }


    /**
     * 全排列计算后判断位置是否匹配
     * @param stages 当前stage数组
     * @param pos 必须使用的优惠索引
     * @return
     */
    private boolean isMustUse(CalcStage[] stages,Set<Byte> pos){
        int c=0;
        for(CalcStage stage:stages){
            if(Objects.isNull(stage)){
                continue;
            }
            if(pos.contains(stage.getIndex())){
                c++;
            }
        }
        return c==pos.size();
    }


    /**
     * 初始化上下文，清理上一排列留下的脏数据
     */
    private void initCtx(){
        context.getCalcResult().setCurPrice(context.getOriginalPrice());
        CalcStage[] curArr =context.getCalcResult().getCurStages();
        //每次排列计算前初始化curStages为null
        Arrays.fill(curArr,null);
        //每次排列计算前reset usedList 中的单品价格，并清空
        context.getGoodsItems().forEach(this::resetItems);
        //清空黑名单
        context.getRecords().clear();
        //初始化Context
        resetContext(context);
    }

    /**
     * 构建保存点
     * @param goods 商品列表
     */
    private CalcState<T> makeSnapshot(List<T> goods){
        CalcState<T> state = new CalcState<>();
        state.setCurPrice(context.getCalcResult().getCurPrice());
        state.setCurStages(copyStage(context.getCalcResult().getCurStages()));
        state.setRecords(copyRecord(context.getRecords()));
        makeSnapshot(state,context);
        return state;
    }


    /**
     * 返回保存点
     * @param k 缓存的key
     */
    private void backToSnapshot(Integer k){
        CalcState<T> state=(CalcState<T>)cache.get(k);
        context.getCalcResult().setCurPrice(state.getCurPrice());
        context.getCalcResult().setCurStages(copyStage(state.getCurStages()));
        context.setRecords(copyRecord(state.getRecords()));
        backToSnapshot(state,context);
    }


    /**
     * 复制stages
     * @param curStages 当前stages
     */
    private CalcStage[] copyStage(CalcStage[] curStages){
        CalcStage[] cs=new CalcStage[curStages.length];
        System.arraycopy(curStages,0,cs,0,curStages.length);
        return cs;
    }

    /**
     * clone商品当前状态，转成map便于查找
     * @param goods 商品列表
     * @return
     */
    protected Map<Long,T> copyGoods(List<T> goods){
        Map<Long,T> map=Maps.newHashMap();
        for(T good:goods){
            try {
                map.put(good.getCalculateId(),(T)good.clone());
            } catch (Exception ignore) {
            }
        }
        return map;
    }

    /**
     * 复制享受过优惠的商品记录，只用到calculateId，因此不对对象进行clone
     * @param records 享受过优惠的商品记录
     */
    private Map<Long,T> copyRecord(Map<Long,T> records){
        return Maps.newHashMap(records);
    }

    /**
     * 相同最优解的处理逻辑，交给业务来实现
     * @param stages 最终的stage数组
     * @param curStages 当前的stage数组
     * @return
     */
    protected abstract boolean sameOptimumCondition(CalcStage[] curStages);

    /**
     * context参数的重置逻辑，交给业务来实现
     * @param context 上下文
     */
    protected abstract void resetContext(DiscountContext<T> context);

    /**
     * 开启缓存的条件,如 a.size>4
     * @param a 优惠排列
     */
    protected abstract boolean enableOptimize(List<Byte> a);

    /**
     * 商品参数的重置逻辑，交给业务来实现
     * @param item 单品
     */
    protected abstract void resetItems(T item);

    /**
     * 业务将状态记录到保存点
     * @param state 保存点对象
     */
    protected abstract void makeSnapshot(CalcState<T> state,DiscountContext<T> context);

    /**
     * 业务返回保存点状态
     * @param state 保存点对象
     */
    protected abstract void backToSnapshot(CalcState<T> state,DiscountContext<T> context);
}
