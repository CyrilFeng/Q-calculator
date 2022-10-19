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

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.io.Serializable;


/**
 * 优惠计算结果
 * @author: CyrilFeng
 * @date: 2022/8
 */
@Data
public class CalcResult implements Serializable {

    private CalcResult() {
    }

    /**
     * 最优实付金额
     */
    private long finalPrice;

    /**
     * 最优优惠计算过程
     */
    private  CalcStage[] stages;

    /**
     * 当前序列计算的实付金额
     */
    @JsonIgnore
    private transient long curFinalPrice;

    /**
     * 当前序列优惠计算过程
     */
    @JsonIgnore
    private transient  CalcStage[] curStages;

    /**
     *  实时计算价格
     */
    @JsonIgnore
    private transient  long curPrice;

    public static CalcResult create(int n){
        CalcResult c=new CalcResult();
        c.stages=new CalcStage[n];
        c.curStages=new CalcStage[n];
        return c;
    }

}
