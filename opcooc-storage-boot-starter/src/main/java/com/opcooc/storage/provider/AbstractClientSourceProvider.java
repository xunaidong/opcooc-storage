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
package com.opcooc.storage.provider;

import com.opcooc.storage.autoconfigure.FileClientProperties;
import com.opcooc.storage.client.FileClient;
import com.opcooc.storage.config.ClientSource;
import com.opcooc.storage.config.StorageProperty;
import com.opcooc.storage.creator.FileClientCreator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shenqicheng
 * @since 2020-08-22 10:30
 */
public abstract class AbstractClientSourceProvider implements ClientSourceProvider {


    protected Map<String, FileClient> createClientMap(Map<String, StorageProperty> clientSourceMap) {

        Map<String, FileClient> map = new HashMap<>(clientSourceMap.size());
        for (Map.Entry<String, StorageProperty> item : clientSourceMap.entrySet()) {
            String key = item.getKey().toUpperCase();
            FileClient client = FileClientCreator.getClient(key, item.getValue());
            if (client != null) {
                map.put(key, client);
            }
        }

        return map;
    }

    protected Map<String, FileClient> createExtendClientMap(
            Map<String, FileClientProperties.ExtendRequestProperty> clientSourceMap) {

        Map<String, FileClient> map = new HashMap<>(clientSourceMap.size());
        for (Map.Entry<String, FileClientProperties.ExtendRequestProperty> item : clientSourceMap.entrySet()) {
            String key = item.getKey().toUpperCase();
            FileClient client = FileClientCreator.getExtendClient(item.getValue());
            if (client != null) {
                map.put(key, client);
            }
        }

        return map;
    }

}
