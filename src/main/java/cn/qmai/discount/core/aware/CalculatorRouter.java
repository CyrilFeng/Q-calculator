package cn.qmai.discount.core.aware;

import cn.qmai.discount.core.discount.Calculator;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * 根据注解路由到目标Calculator
 * @author: shiyafeng
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
