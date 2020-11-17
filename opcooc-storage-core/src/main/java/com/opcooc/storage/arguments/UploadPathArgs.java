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
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Paths;

import static com.opcooc.storage.utils.StorageChecker.validateNotEmptyString;

/**
 * @author shenqicheng
 * @since 2020-09-20 20:20
 */
@Slf4j
public class UploadPathArgs extends ObjectArgs {

    @Getter
    private String filename;

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends ObjectArgs.Builder<UploadPathArgs, Builder> {

        public Builder filename(String filename) {
            validateFilename(filename);

            operations.add(args -> args.filename = filename);
            return this;
        }

        @Override
        protected void validate(UploadPathArgs args) {
            log.debug("opcooc-storage - UploadPathArgs, filename: [{}]", args.filename);
            super.validate(args);
            validateFilename(args.filename);
        }

        private void validateFilename(String filename) {

            validateNotEmptyString(filename, "filename");

            if (!Files.exists(Paths.get(filename))) {
                throw new ClientSourceException("opcooc-storage - [%s] the file does not exist", filename);
            }

            if (!Files.isRegularFile(Paths.get(filename))) {
                throw new ClientSourceException("opcooc-storage - [%s] not a regular file", filename);
            }

        }
    }
}
