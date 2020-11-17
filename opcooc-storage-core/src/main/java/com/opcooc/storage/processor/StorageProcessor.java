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
package com.opcooc.storage.processor;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author shenqicheng
 * @since 2020-09-02 13:01
 */
public interface StorageProcessor {

    /**
     * 匹配条件
     *
     * @return 匹配类型
     */
    String getProcessorType();

    /**
     * 决定参数
     *
     * @param invocation 方法执行信息
     * @param key        注解内容
     * @return 参数
     */
    String determineParam(MethodInvocation invocation, String key);


}
