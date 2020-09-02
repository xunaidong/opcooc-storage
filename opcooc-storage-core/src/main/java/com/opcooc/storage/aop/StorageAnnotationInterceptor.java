/*
 * Copyright © 2020-2020 organization opcooc
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

import com.opcooc.storage.processor.StorageProcessorManager;
import com.opcooc.storage.support.StorageAttribute;
import com.opcooc.storage.support.StorageClassResolver;
import com.opcooc.storage.utils.StorageAttributeContextHolder;
import lombok.Setter;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author shenqicheng
 * @since 2020-09-02 13:01
 */
public class StorageAnnotationInterceptor implements MethodInterceptor {


    private static final String PREFIX = "#";
    private static final StorageClassResolver RESOLVER = new StorageClassResolver();

    @Setter
    private StorageProcessorManager processorManager;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            StorageAttributeContextHolder.push(determineStorage(invocation));
            return invocation.proceed();
        } finally {
            StorageAttributeContextHolder.poll();
        }
    }

    private StorageAttribute determineStorage(MethodInvocation invocation) {
        StorageAttribute storage = RESOLVER.getStorageKey(invocation.getMethod(), invocation.getThis());
        storage.setClient(convertAttribute(invocation, storage.getClient()));
        storage.setBucket(convertAttribute(invocation, storage.getBucket()));
        return storage;
    }

    private String convertAttribute(MethodInvocation invocation, String attr) {
        return (attr != null && attr.startsWith(PREFIX)) ? processorManager.determineStorage(invocation, attr) : attr;
    }
}
