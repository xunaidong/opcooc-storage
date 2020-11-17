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

import com.opcooc.storage.exception.ClientSourceException;
import lombok.Getter;

import java.io.File;

import static com.opcooc.storage.utils.StorageChecker.validateNotNull;

/**
 * @author shenqicheng
 * @since 2020-10-18 15:00
 */
public class GetObjectToFileArgs extends ObjectArgs {

    @Getter
    private File file;

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends ObjectArgs.Builder<GetObjectToFileArgs, Builder> {

        public Builder file(File file) {
            validateFile(file);
            operations.add(args -> args.file = file);
            return this;
        }

        @Override
        protected void validate(GetObjectToFileArgs args) {
            super.validate(args);
            validateFile(args.file);
        }

        private void validateFile(File file) {
            validateNotNull(file, "file");
            if (!file.exists()) {
                throw new ClientSourceException("opcooc-storage - [%s] the file does not exist", file);
            }
        }
    }
}
