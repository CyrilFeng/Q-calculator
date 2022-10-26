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
import lombok.*;
import java.io.Serializable;

/**
 * 共享互斥关系中的元素
 * @author: CyrilFeng
 * @date: 2022/8
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Item implements Serializable {
    /**
     * 优惠类型，和DiscountWrapper中的type保持一致
     */
    private String type;

    /**
     * 优惠的ID，和DiscountWrapper中的id保持一致 
     */
    private String id;
}
