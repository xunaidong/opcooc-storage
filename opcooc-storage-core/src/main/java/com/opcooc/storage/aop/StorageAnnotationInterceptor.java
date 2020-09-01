package com.opcooc.storage.aop;

import com.opcooc.storage.support.StorageClassResolver;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class StorageAnnotationInterceptor implements MethodInterceptor {


    private static final String PREFIX = "#";
    private static final StorageClassResolver RESOLVER = new StorageClassResolver();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        return null;
    }
}
