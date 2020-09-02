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

    @Getter
    private List<StorageProcessor> processors = new ArrayList<>(3);

    public void addProcessor(StorageProcessor processor) {
        this.processors.add(processor);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (processors == null || processors.isEmpty()) {
            throw new IllegalArgumentException("processor list not null");
        }
        processors.sort(Comparator.comparingInt(StorageProcessor::order));
    }

    public String determineStorage(MethodInvocation invocation, String key) {
        for (StorageProcessor processor : getProcessors()) {
            if (!processor.matches(key)) {
                continue;
            }
            return processor.doDetermineStorage(invocation, key);
        }
        return null;
    }
}
