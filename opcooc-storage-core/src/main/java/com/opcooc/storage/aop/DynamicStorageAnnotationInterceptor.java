/**
 * Copyright © 2018 organization baomidou
 * <pre>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <pre/>
 */
package com.opcooc.storage.aop;

import com.opcooc.storage.processor.StorageProcessorHolder;
import com.opcooc.storage.support.StorageAttribute;
import com.opcooc.storage.support.StorageClassResolver;
import com.opcooc.storage.utils.DynamicStorageContextHolder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Core Interceptor of Dynamic Datasource
 *
 * @author shenqicheng
 * @since 1.2.0
 * https://gitee.com/baomidou/dynamic-datasource-spring-boot-starter
 */
public class DynamicStorageAnnotationInterceptor implements MethodInterceptor {

    private final StorageClassResolver storageClassResolver;
    private final StorageProcessorHolder processorHolder;

    public DynamicStorageAnnotationInterceptor(Boolean allowedPublicOnly, StorageProcessorHolder processorHolder) {
        storageClassResolver = new StorageClassResolver(allowedPublicOnly);
        this.processorHolder = processorHolder;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            StorageAttribute storageAttribute = determineStorage(invocation);
            DynamicStorageContextHolder.push(storageAttribute);
            return invocation.proceed();
        } finally {
            DynamicStorageContextHolder.poll();
        }
    }

    private StorageAttribute determineStorage(MethodInvocation invocation) {
        StorageAttribute storage = storageClassResolver.findStorageKey(invocation.getMethod(), invocation.getThis());
        return processorHolder.determineStorage(invocation, storage);
    }

}