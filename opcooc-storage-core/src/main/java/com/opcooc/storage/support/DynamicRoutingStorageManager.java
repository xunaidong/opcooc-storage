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

import cn.hutool.core.util.StrUtil;
import com.opcooc.storage.client.FileClient;
import com.opcooc.storage.exception.StorageException;
import com.opcooc.storage.processor.StorageProcessor;
import com.opcooc.storage.provider.ClientSourceProvider;
import com.opcooc.storage.utils.StorageChecker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shenqicheng
 * @since 2020-09-02 13:01
 */
@Slf4j
public class DynamicRoutingStorageManager {

    private static final String PREFIX = "#";

    @Getter
    private final Map<String, FileClient> clientMap = new LinkedHashMap<>();
    @Setter
    @Getter
    private String primary = "";
    @Setter
    private StorageProcessor processor;
    @Setter
    private ClientSourceProvider clientProvider;

    private String determineParam(MethodInvocation invocation, String attr) {
        //判断参数是否按照规定格式
        boolean isProcessor = attr != null && attr.startsWith(PREFIX);
        return isProcessor ? processor.determineParam(invocation, attr) : attr;
    }

    public Map<String, FileClient> getLoadClientSources(){
        return clientProvider.loadClientSources();
    }

    public StorageAttribute determineStorage(MethodInvocation invocation, StorageAttribute storage) {

        if (storage == null) {
            return null;
        }
        String client = determineClient(invocation, storage);
        log.debug("opcooc-storage - determine Storage client named [{}]", client);

        String bucket = determineBucket(invocation, client, storage);

        return StorageAttribute.builder().client(client).bucket(bucket).build();
    }

    private String determineClient(MethodInvocation invocation, StorageAttribute storage) {
        String client = determineParam(invocation, storage.getClient());
        return clientMap.containsKey(client) ? client : primary;
    }

    private String determineBucket(MethodInvocation invocation, String source, StorageAttribute storage) {
        String bucket = determineParam(invocation, storage.getBucket());
        if (bucket == null) {
            log.debug("opcooc-storage - determine bucket is empty, switch to get client map file client bucket name");
            FileClient client = getClient(source);
            return client == null ? null : client.getBucketName();
        }
        return bucket;
    }

    public FileClient getClient(String source) {

        if (StrUtil.isBlank(source)) {
            log.warn("opcooc-storage - source is empty, switch to primary client");
            return getPrimaryClient();
        }

        if (clientMap.containsKey(source)) {
            log.debug("opcooc-storage - switch to the client named [{}]", source);
            return clientMap.get(source);
        }

        log.warn("opcooc-storage - could not find a client named [{}], switch to primary client", source);
        return getPrimaryClient();
    }

    private FileClient getPrimaryClient() {
        log.debug("opcooc-storage - get the primary client named [{}]", primary);

        FileClient client = clientMap.get(primary);

        if (client == null) {
            throw new StorageException("no primary client");
        }
        return client;
    }

}
