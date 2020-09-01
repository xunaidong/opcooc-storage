package com.opcooc.storage.processor;

import com.opcooc.storage.utils.StorageUtil;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class StorageSessionProcessor implements StorageProcessor {
    /**
     * session开头
     */
    private static final String SESSION_PREFIX = "#session";

    @Override
    public boolean matches(String key) {
        return key.startsWith(SESSION_PREFIX);
    }

    @Override
    public String doDetermineStorage(MethodInvocation invocation, String key) {
        return StorageUtil.getHttpServletRequest().getSession().getAttribute(key.substring(9)).toString();
    }

    @Override
    public Integer order() {
        return 200;
    }
}
