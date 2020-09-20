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

import static com.opcooc.storage.utils.StorageChecker.CHECK_BUCKET_NAME;
import static com.opcooc.storage.utils.StorageChecker.validateNotNull;
import static com.opcooc.storage.utils.StorageChecker.validateNullOrNotEmptyString;

/**
 * @author shenqicheng
 * @since 2020-09-20 20:20
 */
public abstract class BucketArgs extends BaseArgs {

    private static final Integer MIN_LENGTH = 3;

    private static final Integer MAX_LENGTH = 63;

    private static final String POINTS = "..";

    /**
     * 存储桶名称
     */
    @Getter
    protected String bucketName;

    /**
     * 地区
     */
    @Getter
    protected String region;

    public abstract static class Builder<A extends BucketArgs, B extends Builder<A, B>>
            extends BaseArgs.Builder<A, B> {

        protected void validateBucketName(String name) {
            validateNotNull(name, "bucket name");

            if (name.length() < MIN_LENGTH || name.length() > MAX_LENGTH) {
                throw new StorageException(
                        "opcooc-storage - [%s] bucket name must be at least 3 and no more than 63 characters long", name);
            }
            if (name.contains(POINTS)) {
                throw new StorageException("opcooc-storage - [%s] bucket name cannot contain successive periods. For more information refer", name);
            }

            if (!CHECK_BUCKET_NAME.test(name)) {
                throw new StorageException(
                        "opcooc-storage - [%s] bucket name does not follow Amazon S3 standards. For more information refer", name);
            }
        }

        @Override
        protected void validate(A args) {
            validateBucketName(args.bucketName);
        }

        @SuppressWarnings("unchecked")
        public B bucket(String name) {
            validateBucketName(name);
            operations.add(args -> args.bucketName = name);
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B region(String region) {
            validateNullOrNotEmptyString(region, "region");
            operations.add(args -> args.region = region);
            return (B) this;
        }
    }
}
