package com.opcooc.storage.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StorageClassResolver {

    /**
     * 缓存方法对应的存储信息
     */
    private final Map<Object, StorageAttribute> storageCache = new ConcurrentHashMap<>();


}
