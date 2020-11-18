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
package com.opcooc.storage.creator;

import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ReflectUtil;
import com.opcooc.storage.autoconfigure.FileClientProperties;
import com.opcooc.storage.client.*;
import com.opcooc.storage.config.ClientType;
import com.opcooc.storage.config.StorageProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shenqicheng
 * @since 2020-08-22 10:30
 */
@Getter
@Setter
@Slf4j
public class FileClientCreator {

    public static Client getClient(String source, StorageProperty property) {
        ClientType clientType;

        try {
            clientType = EnumUtil.fromString(ClientType.class, source);
        } catch (IllegalArgumentException e) {
            // 无自定义匹配
            return null;
        }

        switch (clientType) {
//            case S3:
//                return new S3Client(property);
//            case OSS:
//                return new OSSClientSource(property);
//            case COS:
//                return new COSClientSource(property);
//            case MINIO:
//                return new MinIOClientSource(property);
//            case QINIU:
//                return new QinIuClientSource(property);
            default:
                return null;
        }
    }

    public static Client getExtendClient(FileClientProperties.ExtendRequestProperty property) {
        // 反射获取 Request 对象，所以必须实现 1 个参数的构造方法
        try {
            return ReflectUtil.newInstance(property.getClient(), (StorageProperty) property);
        } catch (IllegalArgumentException e) {
            // 无自定义匹配
            return null;
        }
    }

}
