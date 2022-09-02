package cn.qmai.discount.core.model.common;

import lombok.*;
import java.io.Serializable;

/**
 * 共享互斥关系中的元素
 * @author: shiyafeng
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
