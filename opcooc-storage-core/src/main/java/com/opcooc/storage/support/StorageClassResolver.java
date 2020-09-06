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
package com.opcooc.storage.support;

import com.opcooc.storage.annotation.Storage;
import com.opcooc.storage.processor.StorageProcessor;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.MethodClassKey;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.ClassUtils;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shenqicheng
 * @since 2020-09-02 13:01
 */
public class StorageClassResolver {

    /**
     * 缓存方法对应的存储信息
     */
    private final Map<Object, StorageAttribute> storageCache = new ConcurrentHashMap<>();


    public StorageAttribute getStorageKey(Method method, Object targetObject) {

        if (method.getDeclaringClass() == Object.class) {
            return null;
        }

        Object cacheKey = getCacheKey(method, targetObject.getClass());
        StorageAttribute storageAttr = this.storageCache.get(cacheKey);
        if (storageAttr == null) {
            storageAttr = computeStorage(method, targetObject);
            if (storageAttr == null) {
                return null;
            }
            this.storageCache.put(cacheKey, storageAttr);
        }

        return storageAttr;
    }

    protected Object getCacheKey(Method method, Class<?> clazz) {
        return new MethodClassKey(method, clazz);
    }

    private StorageAttribute computeStorage(Method method, Object targetObject) {
        if (!Modifier.isPublic(method.getModifiers())) {
            return null;
        }
        Class<?> targetClass = targetObject.getClass();
        // JDK代理时,  获取实现类的方法声明.  method: 接口的方法, specificMethod: 实现类方法
        Class<?> userClass = ClassUtils.getUserClass(targetClass);
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, userClass);
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
        // 从当前方法查找
        StorageAttribute storageAttr = findStorageAttribute(specificMethod);
        if (storageAttr != null) {
            return storageAttr;
        }
        // 从当前方法声明的类查找
        storageAttr = findStorageAttribute(specificMethod.getDeclaringClass());
        if (storageAttr != null && ClassUtils.isUserLevelMethod(method)) {
            return storageAttr;
        }
        // 如果存在桥接方法
        if (specificMethod != method) {
            // 从桥接方法查找
            storageAttr = findStorageAttribute(method);
            if (storageAttr != null) {
                return storageAttr;
            }
            // 从桥接方法声明的类查找
            storageAttr = findStorageAttribute(method.getDeclaringClass());
            if (storageAttr != null && ClassUtils.isUserLevelMethod(method)) {
                return storageAttr;
            }
        }

        return getDefaultStorageAttr(targetObject);
    }

    protected StorageAttribute getDefaultStorageAttr(Object targetObject) {
        Class<?> targetClass = targetObject.getClass();
        // 如果不是代理类, 从当前类开始, 不断的找父类的声明
        if (!Proxy.isProxyClass(targetClass)) {
            Class<?> currentClass = targetClass;
            while (currentClass != Object.class) {
                StorageAttribute storageAttr = findStorageAttribute(currentClass);
                if (storageAttr != null) {
                    return storageAttr;
                }
                currentClass = currentClass.getSuperclass();
            }
        }
        return null;
    }

    private StorageAttribute findStorageAttribute(AnnotatedElement ae) {
        AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(ae, Storage.class);
        if (attributes == null) {
            return null;
        }
        String client = attributes.getString("client");
        String bucket = attributes.getString("bucket");
        Class<? extends StorageProcessor> processor = attributes.getClass("processor");
        return StorageAttribute.builder().client(client).bucket(bucket).processor(processor).build();
    }
}
