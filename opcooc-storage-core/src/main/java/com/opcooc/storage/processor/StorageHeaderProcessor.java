package com.opcooc.storage.processor;

import com.opcooc.storage.utils.StorageUtil;
import org.aopalliance.intercept.MethodInvocation;

public class StorageHeaderProcessor implements StorageProcessor {
    /**
     * header prefix
     */
    private static final String HEADER_PREFIX = "#header";

    @Override
    public boolean matches(String key) {
        return key.startsWith(HEADER_PREFIX);
    }

    @Override
    public String doDetermineStorage(MethodInvocation invocation, String key) {
        return StorageUtil.getHttpServletRequest().getHeader(key.substring(8));
    }

    @Override
    public Integer order() {
        return 100;
    }
}
