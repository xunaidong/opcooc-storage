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
package com.opcooc.storage;

import cn.hutool.core.util.StrUtil;
import com.opcooc.storage.client.FileClient;
import com.opcooc.storage.exception.StorageException;
import com.opcooc.storage.support.DynamicRoutingStorageManager;
import com.opcooc.storage.utils.DynamicStorageContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;

/**
 * @author shenqicheng
 * @since 2020-08-22 10:30
 */
@Slf4j
public class StorageClient implements InitializingBean, DisposableBean {

    private final DynamicRoutingStorageManager storageManager;

    public StorageClient(DynamicRoutingStorageManager dynamicRoutingStorageManager) {
        this.storageManager = dynamicRoutingStorageManager;
    }

    public FileClient op() {
        return getClient(DynamicStorageContextHolder.client());
    }

    public Map<String, FileClient> getCurrentClients() {
        return storageManager.getClientMap();
    }

    public String getPrimary() {
        return storageManager.getPrimary();
    }

    public FileClient getClient(String source) {
        return storageManager.getClient(source);
    }

    public synchronized void addClient(String source, FileClient client) {
        if (!getCurrentClients().containsKey(source)) {
            getCurrentClients().put(source, client);
            log.info("opcooc-storage - load named [{}] storage client source success", source);
        } else {
            log.warn("opcooc-storage - load named [{}] storage client source failed, because it already exist", source);
        }
    }

    public synchronized void removeClient(String source) {
        if (StrUtil.isBlank(source)) {
            throw new StorageException("remove parameter could not be empty");
        }
        if (getPrimary().equals(source)) {
            throw new StorageException("could not be remove primary client");
        }
        if (getCurrentClients().containsKey(source)) {
            FileClient client = getCurrentClients().get(source);
            try {
                closeClient(client);
                log.debug("opcooc-storage - the client named [{}] closed success", source);
            } catch (Exception e) {
                log.error("opcooc-storage - emove the client named [{}] failed", source, e);
            }
            getCurrentClients().remove(source);
            log.info("opcooc-storage - remove the client named [{}] success", source);
        } else {
            log.warn("opcooc-storage - could not find a client named [{}]", source);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, FileClient> clientSources = storageManager.getLoadClientSources();

        //添加客户端信息
        clientSources.forEach(this::addClient);

        //检查是否设置了默认客户端
        if (getCurrentClients().containsKey(getPrimary())) {
            log.info("opcooc-storage - a total of [{}] clients were success loaded, primary client named [{}]", getCurrentClients().size(), getPrimary());
        } else {
            log.warn("opcooc-storage - a total of [{}] clients were success loaded, no primary client available", getCurrentClients().size());
        }
    }

    @Override
    public void destroy() throws Exception {
        log.info("opcooc-storage - client start closing ....");
        for (Map.Entry<String, FileClient> item : getCurrentClients().entrySet()) {
            closeClient(item.getValue());
            log.debug("opcooc-storage - the client named [{}] closed success", item.getKey());
        }
        log.info("opcooc-storage - client all closed success,bye");
    }

    private void closeClient(FileClient client) {
        client.shutdown();
    }

}
