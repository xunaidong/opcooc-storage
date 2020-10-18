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
import lombok.extern.slf4j.Slf4j;

import static com.opcooc.storage.utils.StorageChecker.validateNotNull;

/**
 * @author shenqicheng
 * @since 2020-10-18 15:00
 */
@Slf4j
public class GetObjectToPathArgs extends ObjectArgs {

    @Getter
    private String path;

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends ObjectArgs.Builder<GetObjectToPathArgs, Builder> {

        public Builder path(String path) {
            validateNotNull(path, "path");
            operations.add(args -> args.path = path);
            return this;
        }

        @Override
        protected void validate(GetObjectToPathArgs args) {
            log.debug("opcooc-storage - GetObjectToPathArgs, path: [{}]", args.path);
            super.validate(args);
            validatePath(args.path);
        }

        public void validatePath(String path){
            // todo 需要验证filePath是否正确
        }

    }
}
