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


import com.opcooc.storage.StorageClient;
import com.opcooc.storage.aop.DynamicStorageClientAnnotationAdvisor;
import com.opcooc.storage.aop.DynamicStorageClientAnnotationInterceptor;
import com.opcooc.storage.config.StorageProperty;
import com.opcooc.storage.processor.*;
import com.opcooc.storage.support.DynamicRoutingStorageManager;
import com.opcooc.storage.provider.ClientSourceProvider;
import com.opcooc.storage.provider.YmlClientSourceProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

import java.util.Map;

/**
 * @author shenqicheng
 * @since 2020-08-22 10:30
 */
@Slf4j
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(FileClientProperties.class)
@ConditionalOnProperty(prefix = FileClientProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class FileClientAutoConfiguration {

    private final FileClientProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public ClientSourceProvider clientSourceProvider() {
        Map<String, StorageProperty> clientSourceMap = properties.getClient();
        Map<String, FileClientProperties.ExtendRequestProperty> extendClientSourceMap = properties.getExtend();
        return new YmlClientSourceProvider(clientSourceMap, extendClientSourceMap);
    }

    @Bean
    @ConditionalOnMissingBean
    public StorageClient storageRoutingClientSource(DynamicRoutingStorageManager dynamicRoutingStorageManager) {
        return new StorageClient(dynamicRoutingStorageManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public DynamicRoutingStorageManager dynamicRoutingStorageManager(ClientSourceProvider clientProvider, StorageProcessor storageProcessor) {
        DynamicRoutingStorageManager manager = new DynamicRoutingStorageManager();
        manager.setPrimary(properties.getPrimary());
        manager.setClientProvider(clientProvider);
        manager.setProcessor(storageProcessor);
        return manager;
    }

    @Bean
    @ConditionalOnMissingBean
    public ScProcessor dsProcessor() {
        ScHeaderProcessor headerProcessor = new ScHeaderProcessor();
        ScSessionProcessor sessionProcessor = new ScSessionProcessor();
        ScSpelExpressionProcessor spelExpressionProcessor = new ScSpelExpressionProcessor();
        headerProcessor.setNextProcessor(sessionProcessor);
        sessionProcessor.setNextProcessor(spelExpressionProcessor);
        return headerProcessor;
    }


    @Role(value = BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    @ConditionalOnMissingBean
    public DynamicStorageClientAnnotationAdvisor dynamicStorageAnnotationAdvisor(ScProcessor scProcessor) {
        DynamicStorageClientAnnotationInterceptor interceptor = new DynamicStorageClientAnnotationInterceptor(properties.isAllowedPublicOnly(), scProcessor);
        DynamicStorageClientAnnotationAdvisor advisor = new DynamicStorageClientAnnotationAdvisor(interceptor);
        advisor.setOrder(properties.getOrder());
        return advisor;
    }

}
