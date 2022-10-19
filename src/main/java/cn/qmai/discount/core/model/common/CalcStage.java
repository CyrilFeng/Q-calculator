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
import java.util.Map;

/**
 * 单个具体优惠计算明细，可以当成日志来用，用于计算过程的追溯
 * @author: CyrilFeng
 * @date: 2022/8
 */
@Data
public class CalcStage implements Serializable {

    /**
     * 优惠前价格
     */
    private long beforeCalcPrice;

    /**
     * 优惠后价格
     */
    private long afterCalcPrice;

    /**
     * 优惠类型
     */
    private String stageType;

    /**
     * 优惠索引
     */
    private byte index;

    /**
     * 扩展属性
     */
    private Map<String,Object> extra;

}
