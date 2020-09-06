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
public class AutoStorageProcessor implements StorageProcessor{

    @Override
    public boolean matches(String key) {
        return false;
    }

    @Override
    public String doDetermineStorage(MethodInvocation invocation, String key) {
        return "";
    }

    @Override
    public Integer order() {
        return Integer.MAX_VALUE;
    }
}
