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
public abstract class StorageProcessor {

    private StorageProcessor nextProcessor;

    public void setNextProcessor(StorageProcessor processor) {
        this.nextProcessor = processor;
    }

    /**
     * 抽象匹配条件 匹配才会走当前执行器否则走下一级执行器
     *
     * @param key 注解内容
     * @return 是否匹配
     */
    public abstract boolean matches(String key);

    /**
     * 决定参数
     *
     * <pre>
     *     调用底层doDetermineParam
     *     如果返回的是null则继续执行下一个，否则直接返回
     * </pre>
     *
     * @param invocation 方法执行信息
     * @param key        注解内容
     * @return 参数
     */
    public String determineParam(MethodInvocation invocation, String key) {
        if (matches(key)) {
            String param = doDetermineParam(invocation, key);
            if (param == null && nextProcessor != null) {
                return nextProcessor.determineParam(invocation, key);
            }
            return param;
        }
        if (nextProcessor != null) {
            return nextProcessor.determineParam(invocation, key);
        }
        return null;
    }

    /**
     * 抽象最终决定参数
     *
     * @param invocation 方法执行信息
     * @param key        注解内容
     * @return 参数
     */
    public abstract String doDetermineParam(MethodInvocation invocation, String key);


}
