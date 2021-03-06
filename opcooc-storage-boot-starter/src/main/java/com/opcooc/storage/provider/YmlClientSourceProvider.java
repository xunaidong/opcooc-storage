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
import com.opcooc.storage.config.StorageProperty;
import lombok.AllArgsConstructor;

import java.util.Map;

/**
 * @author shenqicheng
 * @since 2020-08-22 10:30
 */
@AllArgsConstructor
public class YmlClientSourceProvider extends AbstractClientSourceProvider {

    /**
     * 所有 client
     */
    private final Map<String, StorageProperty> storagePropertiesMap;

    private final Map<String, FileClientProperties.ExtendRequestProperty> extendStoragePropertiesMap;

    @Override
    public Map<String, FileClient> loadClientSources() {
        // todo 是否需要区分先后顺序
        Map<String, FileClient> map = createClientMap(storagePropertiesMap);
        map.putAll(createExtendClientMap(extendStoragePropertiesMap));
        return map;
    }
}
