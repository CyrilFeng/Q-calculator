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

package cn.qmai.discount.core.utils;
import cn.qmai.discount.core.model.common.DiscountGroup;
import cn.qmai.discount.core.model.common.DiscountWrapper;
import cn.qmai.discount.core.model.common.Item;
import cn.qmai.discount.core.permutation.Permutation;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.AtomicLongMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.tuple.Pair;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ID生成器，用于生成每次计算的商品id
 * 非线程安全，不可以在多线程共享。 每次计算创建一个，用于生成具体每一个商品的唯一ID
 * @author: shiyafeng
 * @date: 2022/7
 */
public class DiscountGroupUtil {

    private static final String EXCLUDE="exclude";

    /**
     * 返回所有共享组
     * Pair的left是长度不超过 Permutation.SUPPORTEDSIZE 的内容，right是第 Permutation.SUPPORTEDSIZE+1
     * 种优惠即其他优惠（若有），即不参与全排列的优惠
     * @param groups
     * @param inMap
     * @return
     */
    public static List<Pair<Set<DiscountWrapper>,Set<DiscountWrapper>>> transform(List<List<DiscountGroup>> groups, Map<String, Map<String,DiscountWrapper>> inMap) {
        List<Pair<Set<DiscountWrapper>,Set<DiscountWrapper>>> resultList = Lists.newArrayList();
        List<List<Item>> list = mergeGroups(groups,inMap);
        if(CollectionUtils.isEmpty(list)){
            return Lists.newArrayList();
        }
        for(List<Item> items:list){
            Set<DiscountWrapper> discountWrappers = items.stream().map(x->{
                if(inMap.containsKey(x.getType())){
                    Map<String,DiscountWrapper> m=inMap.get(x.getType());
                    if(m.containsKey(x.getId())){
                        return m.get(x.getId());
                    }
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toSet());
            if(CollectionUtils.isNotEmpty(discountWrappers) && discountWrappers.size()>=2){
                if(discountWrappers.size()> Permutation.SUPPORTEDSIZE) {
                    resultList.add(balanceLR(discountWrappers));
                }else{
                    resultList.add(Pair.of(discountWrappers, Sets.newHashSet()));
                }
            }
        }
        //优先计算短的共享组
        return resultList.stream().sorted(Comparator.comparing(x->x.getLeft().size(),Collections.reverseOrder())).collect(Collectors.toList());
    }

    /**
     * 保证mustuse的优惠进入left集合
     * @param discountWrappers 当前可用优惠
     */
    private static Pair<Set<DiscountWrapper>,Set<DiscountWrapper>> balanceLR(Set<DiscountWrapper> discountWrappers){
        Set<DiscountWrapper> left=Sets.newHashSet();
        Set<DiscountWrapper> right=Sets.newHashSet();
        for(DiscountWrapper wrapper:discountWrappers){
            if(wrapper.isMustUse()){
                left.add(wrapper);
            }else{
                right.add(wrapper);
            }
        }
        if(left.size()<Permutation.SUPPORTEDSIZE){
            int c = Permutation.SUPPORTEDSIZE-left.size();
            Iterator<DiscountWrapper> it = right.iterator();
            while (c>0&&it.hasNext()){
                DiscountWrapper w = it.next();
                left.add(w);
                it.remove();
                c--;
            }
        }else if(left.size()>Permutation.SUPPORTEDSIZE){
            int c = left.size()-Permutation.SUPPORTEDSIZE;
            Iterator<DiscountWrapper> it = left.iterator();
            while (c>0&&it.hasNext()){
                DiscountWrapper w = it.next();
                right.add(w);
                it.remove();
                c--;
            }
        }
        return Pair.of(left,right);
    }

    /**
     * 合并规则转化为共享组
     * @param groups 所有规则大组
     * @param inMap 实际可用优惠
     */
    private static List<List<Item>> mergeGroups(List<List<DiscountGroup>> groups,Map<String, Map<String,DiscountWrapper>> inMap){
        if(CollectionUtils.isEmpty(groups) || MapUtils.isEmpty(inMap)){
            return null;
        }
        //resultList 接收最终的结果
        List<List<Item>> resultList = Lists.newArrayList();
        //ctx 全局计数器，对于独立共享组多次被使用进行删除（唯一需要去重的场景）
        AtomicLongMap<String> ctx = AtomicLongMap.create();
        //索引，存放可能需要删除的key
        Map<String,Integer> idxMap = Maps.newHashMap();
        for(List<DiscountGroup> list:groups){
            mergeGroup(list,inMap,ctx,idxMap,resultList);
        }
        Map<String,Long> map = ctx.asMap();
        List<Integer> orderedList = Lists.newArrayList();
        for(Map.Entry<String,Long> e:map.entrySet()){
            Integer idx = idxMap.get(e.getKey());
            if(Objects.nonNull(idx)&&e.getValue()>1){
                orderedList.add(idxMap.get(e.getKey()));
            }
        }
        orderedList.sort(Collections.reverseOrder());
        //从后往前删除，否则索引会出问题
        for(Integer i:orderedList){
            resultList.remove(i.intValue());
        }
        return resultList.stream().filter(CollectionUtils::isNotEmpty).collect(Collectors.toList());
    }

    private static void mergeGroup(List<DiscountGroup> groups,Map<String, Map<String, DiscountWrapper>> inMap,
                                              AtomicLongMap<String> ctx,Map<String,Integer> idxMap,
                                              List<List<Item>> resultList){
        if(CollectionUtils.isEmpty(groups)){
            return ;
        }
        List<Item> xList = groups.get(0).filterItems(inMap);
        if(CollectionUtils.isEmpty(xList)){
            return ;
        }
        if(groups.size()==1){
            //必然是共享组
            //存入全局上下文，去重时使用，这里累计次数，若次数大于1，需要在最外层移除此共享组
            String key = uniqueKey(xList);
            ctx.incrementAndGet(key);
            //记录索引,以便删除
            idxMap.put(key,resultList.size());
            resultList.add(xList);
        }else{
            //yList必然是互斥的
            List<Item> yList = groups.get(1).filterItems(inMap);
            if(Objects.equals(EXCLUDE,groups.get(0).getRelation())){
                //产品设计规避了重复的情况，无需去重
                for(Item item:xList){
                    for(Item item1:yList){
                        resultList.add(Lists.newArrayList(item,item1));
                    }
                }
            }else{
                //存入全局上下文，去重时使用，这里累计次数，若次数大于1，需要在最外层移除此共享组
                String k = uniqueKey(xList);
                for(Item item:yList){
                    ctx.incrementAndGet(k);
                    List<Item> xCopy = Lists.newArrayList(xList);
                    xCopy.add(item);
                    resultList.add(xCopy);
                   }
            }
        }
    }

    private static String uniqueKey(List<Item> list){
        return list.stream().map(i->i.getType()+i.getId()).sorted().collect(Collectors.joining());
    }

}
