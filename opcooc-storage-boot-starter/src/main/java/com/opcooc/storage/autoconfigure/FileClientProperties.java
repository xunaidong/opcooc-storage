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
package com.opcooc.storage.autoconfigure;

import com.opcooc.storage.client.FileClient;
import com.opcooc.storage.config.ClientSource;
import com.opcooc.storage.config.StorageProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shenqicheng
 * @since 2020-08-22 10:30
 */
@Getter
@Setter
@ConfigurationProperties(prefix = FileClientProperties.PREFIX)
public class FileClientProperties {

    public static final String PREFIX = "opcooc";

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 默认的客户端类型
     */
    private String primary = "";

    /**
     * 内置配置
     */
    private Map<String, StorageProperty> client = new HashMap<>();

    /**
     * 自定义配置
     */
    private Map<String, ExtendRequestProperty> extend = new HashMap<>();

    /**
     * aop切面顺序，默认优先级最高
     */
    private Integer order = Ordered.HIGHEST_PRECEDENCE;

    /**
     * aop 切面是否只允许切 public 方法
     */
    private boolean allowedPublicOnly = true;

    @Getter
    @Setter
    public static class ExtendRequestProperty extends StorageProperty {
        /**
         * client对应全路径
         */
        private Class<? extends FileClient> client;
    }

}
