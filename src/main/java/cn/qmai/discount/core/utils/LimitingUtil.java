package cn.qmai.discount.core.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 统计计算次数的工具，在机器负载较大时，作为放弃后面计算的条件
 * @author: shiyafeng
 * @date: 2022/8
 */
public class LimitingUtil {

    /**
     * 统计计算次数
     * 根据性能压测结果，对于多个共享组来说是必要的
     * @param length 共享组的长度
     */
    public static int count(int length){
        switch (length){
            case 1:return 1;
            //下面的计算公式是 x!*x
            case 2:return 4;
            case 3:return 18;
            //下面的计算公式是 x!*(x-3)+p(x,3)
            case 4:return 48;
            case 5:return 300;
            case 6:return 2280;
            case 7:return 20370;
            default:return 0;
        }
    }
}
