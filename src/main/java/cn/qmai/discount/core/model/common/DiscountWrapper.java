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
import lombok.Data;
import java.io.Serializable;
import java.util.Objects;

/**
 * 优惠类
 * @author: CyrilFeng
 * @date: 2022/7
 */
@Data
public class DiscountWrapper implements Serializable,Comparable<DiscountWrapper> {

    /**
     * 优惠的具体类型，CalculatorRouter会根据这个type在Calculator的实现类中扫描
     */
    private String type;

    /**
     * 优惠的ID
     */
    private String id;

    /**
     * 优惠的名称
     */
    private String name;

    /**
     * 是否是必须使用的优惠
     */
    private boolean mustUse;

    /**
     * 优惠的配置信息
     */
    private DiscountConfig discountConfig;


    public static DiscountWrapper of(String type,String id,String name,boolean mustUse,DiscountConfig discountConfig){
        DiscountWrapper wrapper = new DiscountWrapper();
        wrapper.setType(type);
        wrapper.setId(id);
        wrapper.setName(name);
        wrapper.setMustUse(mustUse);
        wrapper.setDiscountConfig(discountConfig);
        return wrapper;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountWrapper wrapper = (DiscountWrapper) o;
        return type.equals(wrapper.type) && id.equals(wrapper.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id);
    }

    @Override
    public int compareTo(DiscountWrapper o) {
        return (this.type+this.id).compareTo(o.getType()+o.getId());
    }

}
