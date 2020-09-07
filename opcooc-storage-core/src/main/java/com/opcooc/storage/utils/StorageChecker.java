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
package com.opcooc.storage.utils;

import com.opcooc.storage.config.ClientSource;
import com.opcooc.storage.config.StorageProperty;
import com.opcooc.storage.exception.StorageException;

import java.util.Objects;

/**
 * @author shenqicheng
 * @since 2020-08-22 10:30
 */
public class StorageChecker {

    public static void checkS3Config(StorageProperty config, ClientSource source) {
        if (Objects.isNull(config.getAccessKey()) || Objects.isNull(config.getSecretKey()) ||
                Objects.isNull(config.getEndPoint()) || Objects.isNull(config.getBucketName())) {
            throw new StorageException("init storage client [%s] error: incomplete param", source);
        }
    }


}
