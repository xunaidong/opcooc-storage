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
