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
package com.opcooc.storage.arguments;

import lombok.Getter;

/**
 * @author shenqicheng
 * @since 2020-10-18 15:00
 */
public class ListObjectsArgs extends BucketArgs {

    @Getter
    private String prefix;

    @Getter
    private boolean recursive;

    @Getter
    private int maxKeys = 1000;

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends BucketArgs.Builder<ListObjectsArgs, Builder> {

        public Builder prefix(String prefix) {
            operations.add(args -> args.prefix = (prefix == null ? "" : prefix));
            return this;
        }

        public Builder recursive(boolean recursive) {
            operations.add(args -> args.recursive = recursive);
            return this;
        }

        public Builder maxKeys(int maxKeys) {
            operations.add(args -> args.maxKeys = maxKeys);
            return this;
        }
    }
}
