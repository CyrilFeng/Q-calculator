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


package cn.qmai.discount.core.aware;
import cn.qmai.discount.core.discount.Calculator;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import java.util.Map;

/**

 * 根据注解路由到目标Calculator
 * @author: CyrilFeng
 * @date: 2022/8
 */
@Component
@SuppressWarnings("all")
public class CalculatorRouter implements ApplicationContextAware {

    private Map<String, Calculator> map;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        map = applicationContext.getBeansOfType(Calculator.class);
    }


    public Map<String, Calculator> getMap() {
        return map;
    }

    public Calculator getService(String key) {
        return map.get(key);
    }

}
