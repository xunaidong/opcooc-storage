/**
 * Copyright © 2018 organization baomidou
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
 * https://gitee.com/baomidou/dynamic-datasource-spring-boot-starter
 */
public abstract class OsProcessor {

    private OsProcessor nextProcessor;

    public void setNextProcessor(OsProcessor osProcessor) {
        this.nextProcessor = osProcessor;
    }

    /**
     * 抽象匹配条件 匹配才会走当前执行器否则走下一级执行器
     *
     * @param key DS注解里的内容
     * @return 是否匹配
     */
    public abstract boolean matches(String key);

    /**
     * 决定数据源
     * <pre>
     *     调用底层doDetermineDatasource，
     *     如果返回的是null则继续执行下一个，否则直接返回
     * </pre>
     *
     * @param invocation 方法执行信息
     * @param key        DS注解里的内容
     * @return 数据源名称
     */
    public String determineStorageClient(MethodInvocation invocation, String key) {
        if (matches(key)) {
            String datasource = doDetermineStorageClient(invocation, key);
            if (datasource == null && nextProcessor != null) {
                return nextProcessor.determineStorageClient(invocation, key);
            }
            return datasource;
        }
        if (nextProcessor != null) {
            return nextProcessor.determineStorageClient(invocation, key);
        }
        return null;
    }

    /**
     * 抽象最终决定数据源
     *
     * @param invocation 方法执行信息
     * @param key        DS注解里的内容
     * @return 数据源名称
     */
    public abstract String doDetermineStorageClient(MethodInvocation invocation, String key);
}
