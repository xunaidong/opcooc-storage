/**
 * Copyright (C), 2015-2019, 华规软件（上海）有限公司
 * FileName: StorageProcessorHolder
 * Author:   shenqicheng
 * Date:     2020/11/17 18:11
 * Description:
 * History:
 */
package com.opcooc.storage.processor;

import cn.hutool.core.util.StrUtil;
import com.opcooc.storage.client.FileClient;
import com.opcooc.storage.support.DynamicRoutingStorageManager;
import com.opcooc.storage.support.StorageAttribute;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shenqicheng
 * @Date: 2020/11/17 18:11
 * @Description:
 */
@Slf4j
public class StorageProcessorHolder implements InitializingBean {

    /**
     * The identification of SPEL.
     */
    private static final String DYNAMIC_FIRST_PREFIX = "#";
    private static final String DYNAMIC_LAST_PREFIX = ":";

    private Map<String, StorageProcessor> processors;

    private String defaultProcessorType;

    @Autowired
    private GenericApplicationContext applicationContext;

    private final DynamicRoutingStorageManager storageManager;

    public StorageProcessorHolder(DynamicRoutingStorageManager storageManager) {
        this.storageManager = storageManager;
    }

    public void addProcessor(String key, StorageProcessor processor) {
        processors.put(key, processor);
    }

    private StorageProcessor findStorageProcessor(String type) {
        type = StrUtil.subBetween(type, DYNAMIC_FIRST_PREFIX, DYNAMIC_LAST_PREFIX);

        StorageProcessor processor = this.processors.get(type.toLowerCase());
        if (processor == null) {
            return this.processors.get(defaultProcessorType);
        }

        return processor;
    }

    public String determineParam(MethodInvocation invocation, String attr) {
        //判断参数是否按照规定格式
        if ((attr != null && attr.startsWith(DYNAMIC_FIRST_PREFIX))) {
            StorageProcessor processor = findStorageProcessor(attr);
            return processor.determineParam(invocation, attr);
        }
        return attr;
    }


    public StorageAttribute determineStorage(MethodInvocation invocation, StorageAttribute storage) {

        String client = determineClient(invocation, storage);
        log.debug("opcooc-storage - determine Storage client named [{}]", client);

        String bucket = determineBucket(invocation, client, storage);

        return StorageAttribute.builder().client(client).bucket(bucket).build();
    }

    private String determineClient(MethodInvocation invocation, StorageAttribute storage) {
        String client = determineParam(invocation, storage.getClient());
        return clientMap.containsKey(client) ? client : primary;
    }

    private String determineBucket(MethodInvocation invocation, StorageAttribute storage) {
        String bucket = determineParam(invocation, storage.getBucket());
        if (bucket == null) {
            log.debug("opcooc-storage - determine bucket is empty, switch to get client map file client bucket name");
            FileClient client = getClient(source);
            return client == null ? null : client.getBucketName();
        }
        return bucket;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 解决循环应用问题
        Map<String, StorageProcessor> stringStorageProcessorMap = applicationContext.getBeansOfType(StorageProcessor.class);
        Collection<StorageProcessor> values = stringStorageProcessorMap.values();
        processors = new HashMap<>(values.size());

        addProcessor(StorageHeaderProcessor.HEADER_PREFIX, new StorageHeaderProcessor());
        addProcessor(StorageSessionProcessor.SESSION_PREFIX, new StorageSessionProcessor());
        addProcessor(StorageSpelExpressionProcessor.SPEL_PREFIX, new StorageSpelExpressionProcessor());
        // add default processor
        defaultProcessorType = StorageSpelExpressionProcessor.SPEL_PREFIX;

        for (StorageProcessor value : values) {
            processors.put(value.getProcessorType().toLowerCase(), value);
        }
    }
}
