/*
 * Copyright 2022 Shiyafeng
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
