/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.internal.service;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 解析出来class的方法属性
 */
class RelevantMethodsBuilder {
    final List<Method> remainingMethods;
    final Class<?> type; // 当前注册的类的类型
    // TODO:LWH  2021/3/4 其主要的作用还是考虑到插入的数据扩容问题
    final LinkedList<Method> decorators = new LinkedList<Method>();
    final LinkedList<Method> factories = new LinkedList<Method>();
    final LinkedList<Method> configurers = new LinkedList<Method>();

    // 所有方法的签名
    private final Set<String> seen = new HashSet<String>();

    /**
     * 根据传入的服务类 非DefaultServiceRegistry 获取所有声明的方法
     * @param type
     */
    RelevantMethodsBuilder(Class<?> type) {
        this.type = type;
        this.remainingMethods = new LinkedList<Method>();

        for (Class<?> clazz = type; clazz != Object.class && clazz != DefaultServiceRegistry.class; clazz = clazz.getSuperclass()) {
            remainingMethods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
        }
    }

    /**
     * 这里默认索引如果添加过了 就会从迭代器里面移除
     * @param iterator
     * @param builder
     * @param method
     */
    void add(Iterator<Method> iterator, List<Method> builder, Method method) {
        StringBuilder signature = new StringBuilder();
        signature.append(method.getName());
        for (Class<?> parameterType : method.getParameterTypes()) {
            signature.append(",");
            signature.append(parameterType.getName());
        }
        if (seen.add(signature.toString())) {
            builder.add(method);
        }
        iterator.remove();
    }

    /**
     * 构造这用来处理这个方法属性的内容
     * @return
     */
    RelevantMethods build() {
        return new RelevantMethods(decorators, factories, configurers);
    }
}
