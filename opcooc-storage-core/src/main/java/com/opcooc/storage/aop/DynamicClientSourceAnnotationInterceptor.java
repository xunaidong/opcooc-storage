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

import com.opcooc.storage.processor.OsProcessor;
import com.opcooc.storage.support.ClientSourceClassResolver;
import com.opcooc.storage.utils.DynamicStorageClientContextHolder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Core Interceptor of Dynamic Datasource
 *
 * @author shenqicheng
 * @since 1.2.0
 * https://gitee.com/baomidou/dynamic-datasource-spring-boot-starter
 */
public class DynamicClientSourceAnnotationInterceptor implements MethodInterceptor {

    /**
     * The identification of SPEL.
     */
    private static final String DYNAMIC_PREFIX = "#";

    private final ClientSourceClassResolver clientSourceClassResolver;
    private final OsProcessor osProcessor;

    public DynamicClientSourceAnnotationInterceptor(Boolean allowedPublicOnly, OsProcessor osProcessor) {
        clientSourceClassResolver = new ClientSourceClassResolver(allowedPublicOnly);
        this.osProcessor = osProcessor;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            String scKey = determineStorageClientKey(invocation);
            DynamicStorageClientContextHolder.push(scKey);
            return invocation.proceed();
        } finally {
            DynamicStorageClientContextHolder.poll();
        }
    }

    private String determineStorageClientKey(MethodInvocation invocation) {
        String key = clientSourceClassResolver.findScKey(invocation.getMethod(), invocation.getThis());
        return (!key.isEmpty() && key.startsWith(DYNAMIC_PREFIX)) ? osProcessor.determineStorageClient(invocation, key) : key;
    }

}
