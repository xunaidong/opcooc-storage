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

/**
 * 文件信息转换器
 *
 * @author shenqicheng
 * @since 2020-08-22 10:30
 */
@FunctionalInterface
public interface ResultConverter<T> {

    /**
     * 对文件信息进行处理 .
     *
     * @param info 基础文件信息.
     * @return 处理后的信息.
     */
    T convert(FileBasicInfo info);

}
