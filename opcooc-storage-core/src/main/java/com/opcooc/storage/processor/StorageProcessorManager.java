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

import com.opcooc.storage.exception.StorageException;
import com.opcooc.storage.support.StorageAttribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author shenqicheng
 * @since 2020-09-02 13:01
 */
@NoArgsConstructor
@AllArgsConstructor
public class StorageProcessorManager implements InitializingBean {

    private static final String PREFIX = "#";

    @Getter
    private final List<StorageProcessor> processors = new ArrayList<>(3);

    public void addProcessor(StorageProcessor processor) {
        this.processors.add(processor);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (processors.isEmpty()) {
            throw new IllegalArgumentException("processor list not null");
        }
        processors.sort(Comparator.comparingInt(StorageProcessor::order));
    }

    private String getStorageAttr(MethodInvocation invocation, String key) {
        for (StorageProcessor processor : getProcessors()) {
            if (!processor.matches(key)) {
                continue;
            }
            return processor.doDetermineStorage(invocation, key);
        }
        return null;
    }

    private String convertAttribute(MethodInvocation invocation, String attr, Processor processor) {
        boolean isProcessor = attr != null && attr.startsWith(PREFIX);
        if (processor != null && isProcessor) {
            return processor.doDetermineStorage(invocation, attr);
        }
        return isProcessor ? getStorageAttr(invocation, attr) : attr;
    }

    private Processor getStorageProcessor(Class<? extends Processor> processorClazz) {
        try {
            return (processorClazz != null && processorClazz != DefaultStorageProcessor.class) ? processorClazz.getDeclaredConstructor().newInstance() : null;
        } catch (Exception e) {
            throw new StorageException("can not instance custom processor: [%s]",processorClazz.getName());
        }
    }

    public StorageAttribute determineStorage(MethodInvocation invocation, StorageAttribute storage) {

        if (storage == null) {
            return null;
        }

        Processor processor = getStorageProcessor(storage.getProcessor());
        String client = convertAttribute(invocation, storage.getClient(), processor);
        String bucket = convertAttribute(invocation, storage.getBucket(), processor);

        return StorageAttribute.builder().client(client).bucket(bucket).build();
    }
}
