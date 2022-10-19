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
import com.google.common.collect.Lists;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 共享互斥组
 * @author: CyrilFeng
 * @date: 2022/8
 */
@Data
public class DiscountGroup implements Serializable {

    private  String relation;
    private  List<Item> items;

    /**
     * 根据用户可用的优惠，对组内信息进行过滤
     * @param inMap
     * @return
     */
    public List<Item> filterItems(Map<String, Map<String,DiscountWrapper>> inMap){
        //inMap 外层key为type，内层key为id，value为DiscountWrapper
        if(CollectionUtils.isEmpty(items)|| MapUtils.isEmpty(inMap)){
            return null;
        }
        //构建items副本
        List<Item> itemsCopy = Lists.newArrayList(items);
        Iterator<Item> it = itemsCopy.iterator();
        while(it.hasNext()){
            Item item = it.next();
            if(!inMap.containsKey(item.getType())){
                it.remove();
                continue;
            }
            if(StringUtils.isNotBlank(item.getId())){
                //理论上m不可能为空
                Map<String,DiscountWrapper> m = inMap.get(item.getType());
                if(MapUtils.isNotEmpty(m)){
                      if(!m.containsKey(item.getId())){
                          it.remove();
                      }
                }
            }
        }
       return itemsCopy;
    }
}
