package com.opcooc.storage.processor;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author shenqicheng
 * @Date: 2020/9/1 20:05
 * @Description:
 */
public interface StorageProcessor {

    boolean matches(String key);

    String doDetermineStorage(MethodInvocation invocation, String key);

    Integer order();
}
