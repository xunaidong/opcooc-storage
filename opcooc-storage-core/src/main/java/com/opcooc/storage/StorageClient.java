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

import com.opcooc.storage.client.FileClient;
import com.opcooc.storage.support.DynamicRoutingStorageManager;
import com.opcooc.storage.utils.DynamicStorageClientContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author shenqicheng
 * @since 2020-08-22 10:30
 */
@Slf4j
public class StorageClient {

    private final DynamicRoutingStorageManager storageManager;

    public StorageClient(DynamicRoutingStorageManager dynamicRoutingStorageManager) {
        this.storageManager = dynamicRoutingStorageManager;
    }

    public FileClient op() {
        return getClient(DynamicStorageClientContextHolder.client());
    }

}
