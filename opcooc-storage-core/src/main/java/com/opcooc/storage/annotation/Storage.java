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
package com.opcooc.storage.annotation;

import com.opcooc.storage.processor.AutoStorageProcessor;
import com.opcooc.storage.processor.StorageProcessor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 动态 切换 client 和 bucketName(或者文件第一个path)
 *
 * @author shenqicheng
 * @since 2020-08-30 10:30
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Storage {

    /**
     * client name
     *
     * @return The name of the client to be switch
     */
    String client() default "";

    /**
     * bucket name
     *
     * @return The name of the bucket to be switch
     */
    String bucket() default "";

    /**
     * processor todo 是否需要分开配置
     *
     * @return StorageProcessor
     */
    Class<? extends StorageProcessor> processor() default AutoStorageProcessor.class;


}
