package com.opcooc.storage.provider;

import com.opcooc.storage.support.StorageAttribute;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author shenqicheng
 * @Date: 2020/9/1 20:05
 * @Description:
 */
public interface StorageProvider {

    StorageAttribute process(MethodInvocation invocation, StorageAttribute attr);

    boolean supports(Class<?> authentication);
}
