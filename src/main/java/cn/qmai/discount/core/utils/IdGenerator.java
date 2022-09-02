package cn.qmai.discount.core.utils;

/**
 * ID生成器，用于生成每次计算的商品id
 * 非线程安全，不可在多线程共享。 每次计算创建一个，用于生成具体每一个商品的唯一ID
 * @author: longlian
 * @date: 2022/7
 */
public class IdGenerator {

    /**
     * 新建一个对象，默认构造
     */
    public static IdGenerator getInstance() {
        return new IdGenerator();
    }

    /**
     * 当前ID，默认从1开始
     */
    private long currentId = 1;

    /**
     * 按序列生成一个ID
     */
    public long nextId() {
        return currentId++;
    }
}
