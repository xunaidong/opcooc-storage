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
package com.opcooc.storage.client;

import com.opcooc.storage.config.ClientSource;
import com.opcooc.storage.config.StorageProperty;
import com.opcooc.storage.utils.StorageChecker;
import lombok.extern.slf4j.Slf4j;


/**
 * @author shenqicheng
 * @since 2020-08-22 10:30
 */
@Slf4j
public abstract class AbstractClient implements FileClient {

    /**
     * client 配置
     */
    protected final StorageProperty config;

    /**
     * client 来源
     */
    protected final ClientSource source;

    public AbstractClient(StorageProperty config, ClientSource source) {
        this.config = config;
        this.source = source;
        // 校验配置合法性
        StorageChecker.checkConfig(config, source);
        // 初始化client
        init(config);
    }

    /**
     * 初始化client
     *
     * @param config 配置
     */
    protected abstract void init(StorageProperty config);

}
