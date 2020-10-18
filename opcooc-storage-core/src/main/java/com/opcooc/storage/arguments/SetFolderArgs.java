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

import com.opcooc.storage.exception.StorageException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static com.opcooc.storage.utils.StorageChecker.validateNotEmptyString;

/**
 * @author shenqicheng
 * @since 2020-09-20 20:20
 */
@Slf4j
public class SetFolderArgs extends BucketArgs {

    private static final String SYMBOL = "/";

    @Getter
    private String folderName;

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends BucketArgs.Builder<SetFolderArgs, Builder> {

        public Builder folderName(String folderName) {
            validateFolderName(folderName);
            operations.add(args -> args.folderName = folderName);
            return this;
        }

        @Override
        protected void validate(SetFolderArgs args) {
            super.validate(args);
            log.debug("opcooc-storage - SetFolderArgs, folderName: [{}]", args.folderName);
            validateFolderName(args.folderName);
        }

        private void validateFolderName(String folderName) {

            validateNotEmptyString(folderName, "folderName");

            if (!folderName.endsWith(SYMBOL)) {
                throw new StorageException("opcooc-storage - [%s] folderName must end with '/' ", folderName);
            }
        }
    }
}
