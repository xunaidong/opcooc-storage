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
import com.opcooc.storage.config.ClientSource;
import com.opcooc.storage.exception.ClientException;
import com.opcooc.storage.provider.ClientSourceProvider;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 *
 * @author shenqicheng
 * @since 2020-08-22 10:30
 */
@Slf4j
public class StorageClient implements InitializingBean, DisposableBean {

    private final Map<String, FileClient> clientMap = new LinkedHashMap<>();

    @Setter
    private String defaultClient = "MINIO";
    @Setter
    private ClientSourceProvider clientProvider;


    public FileClient op() {
        return getDefaultClient();
    }

    public FileClient op(String source) {
        return getClient(source);
    }

    public FileClient op(ClientSource source) {
        return op(source.name());
    }

    public Map<String, FileClient> getCurrentClients() {
        return clientMap;
    }

    public synchronized void addClient(String source, FileClient client) {
        if (!clientMap.containsKey(source)) {
            clientMap.put(source, client);
            log.info("add named [{}] storage client source success", source);
        } else {
            log.warn("add named [{}] storage client source failed, because it already exist", source);
        }
    }

    public synchronized void removeClient(String source) {
        if (StrUtil.isBlank(source)) {
            throw new ClientException("remove parameter could not be empty");
        }
        if (defaultClient.equals(source)) {
            throw new ClientException("could not be remove default client");
        }
        if (clientMap.containsKey(source)) {
            FileClient client = clientMap.get(source);
            try {
                closeClient(client);
            } catch (Exception e) {
                log.error("remove the client named [{}]  failed", source, e);
            }
            clientMap.remove(source);
        } else {
            log.warn("could not find a client named [{}]", source);
        }
    }

    public FileClient getClient(String source) {

        if (StrUtil.isBlank(source)) {
            return getDefaultClient();
        }

        if (clientMap.containsKey(source)) {
            log.debug("get the client named is [{}]", source);
            return clientMap.get(source);
        }
        return getDefaultClient();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, FileClient> clientSources = clientProvider.loadClientSources();

        for (Map.Entry<String, FileClient> dsItem : clientSources.entrySet()) {
            addClient(dsItem.getKey(), dsItem.getValue());
        }

        log.info("a total of [{}] clients were success loaded", clientMap.size());
    }

    @Override
    public void destroy() throws Exception {
        log.info("file client start closing ....");
        for (Map.Entry<String, FileClient> item : clientMap.entrySet()) {
            log.info("file client [{}] closed success", item.getKey());
            closeClient(item.getValue());
        }
        log.info("file client all closed success,bye");
    }

    private void closeClient(FileClient client) throws Exception {
        client.shutdown();
    }

    private FileClient getDefaultClient() {
        log.debug("get default client the client named is [{}]", defaultClient);
        return clientMap.get(defaultClient);
    }


}
