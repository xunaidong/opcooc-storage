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
package com.opcooc.storage.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * @author shenqicheng
 * @since 2020-08-22 10:30
 */
@Getter
@Setter
@ToString
public class FileBasicInfo {

    /**
     * bucketName
     */
    private String bucketName;

    /**
     * 最后修改时间
     */
    private ZonedDateTime latestUpdateTime;

    /**
     * 上级路径
     */
    private String parentPath;

    /**
     * 文件名称 ({文件名称}.{文件类型})
     */
    private String name;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 是否为文件夹
     */
    private boolean isDir;

    /**
     * 对象元数据
     */
    private Map<String, Object> metadata = new HashMap<>();

}
