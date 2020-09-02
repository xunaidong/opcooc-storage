package com.opcooc.storage.aop;

import com.opcooc.storage.processor.StorageProcessorManager;
import com.opcooc.storage.support.StorageAttribute;
import com.opcooc.storage.support.StorageClassResolver;
import com.opcooc.storage.utils.StorageAttributeContextHolder;
import lombok.Setter;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

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
