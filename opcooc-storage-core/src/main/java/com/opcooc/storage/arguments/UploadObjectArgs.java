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

import com.opcooc.storage.utils.StorageConstant;
import com.opcooc.storage.utils.StorageUtil;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;

import static com.opcooc.storage.utils.StorageChecker.validateNotEmptyString;
import static com.opcooc.storage.utils.StorageChecker.validateNotNull;

/**
 * @author shenqicheng
 * @since 2020-09-20 20:20
 */
public class UploadObjectArgs extends ObjectArgs {

    @Getter
    private InputStream stream;

    @Override
    public String contentType() throws IOException {
        String contentType = super.contentType();
        if (contentType != null) {
            return contentType;
        }

        contentType = StorageUtil.TIKA.detect(objectName);
        return (contentType != null && !contentType.isEmpty())
                ? contentType
                : "application/octet-stream";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends ObjectArgs.Builder<UploadObjectArgs, Builder> {

        public Builder stream(InputStream stream, long objectSize) {
            validateStream(stream, objectSize);
            operations.add(args -> args.stream = stream);
            operations.add(args -> args.objectSize = objectSize);
            return this;
        }

        public Builder contentType(String contentType) {
            validateNotEmptyString(contentType, "content type");
            operations.add(args -> args.contentType = contentType);
            return this;
        }

        @Override
        protected void validate(UploadObjectArgs args) {
            super.validate(args);
            validateNotNull(args.stream, "stream");
        }

        private void validateStream(InputStream stream, long objectSize) {

            validateNotNull(stream, "stream");

            if (objectSize > StorageConstant.MAX_OBJECT_SIZE) {
                throw new IllegalArgumentException(
                        "object size " + objectSize + " is not supported; maximum allowed 5TiB");
            }

        }
    }
}
