/*
 * Copyright 2013 the original author or authors.
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

import java.util.ArrayList;
import java.util.List;

/**
 * 根据传入的属性参数 这些参数单独来时是没有关联的 然后通过builder组装成为一个接口的实现 也就是将没有关联的属性构建成为一个真是的属性
 *
 * 多参数的构建 - 利用Builder模式
 */
public class ServiceRegistryBuilder {
    private final List<ServiceRegistry> parents = new ArrayList<ServiceRegistry>();
    private final List<Object> providers = new ArrayList<Object>();
    private String displayName;

    private ServiceRegistryBuilder() {
    }

    public static ServiceRegistryBuilder builder() {
        return new ServiceRegistryBuilder();
    }

    public ServiceRegistryBuilder displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public ServiceRegistryBuilder parent(ServiceRegistry parent) {
        this.parents.add(parent);
        return this;
    }

    public ServiceRegistryBuilder provider(Object provider) {
        this.providers.add(provider);
        return this;
    }

    public ServiceRegistry build() {
        DefaultServiceRegistry registry = new DefaultServiceRegistry(displayName, parents.toArray(new ServiceRegistry[0]));
        for (Object provider : providers) {
            registry.addProvider(provider);
        }
        return registry;
    }
}
