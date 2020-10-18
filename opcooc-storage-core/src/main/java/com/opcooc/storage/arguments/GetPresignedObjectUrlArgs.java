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

import com.amazonaws.HttpMethod;
import lombok.Getter;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.opcooc.storage.utils.StorageChecker.validateNotNull;

/**
 * @author shenqicheng
 * @since 2020-10-18 15:00
 */
public class GetPresignedObjectUrlArgs extends ObjectArgs {
    //defaults to 15 minutes
    public static final long DEFAULT_EXPIRY_TIME = TimeUnit.MINUTES.toMillis(15);
    //max to 7 days
    public static final long MAX_EXPIRY_TIME = TimeUnit.DAYS.toMillis(7);

    @Getter
    private HttpMethod method;
    @Getter
    private boolean specType;
    @Getter
    private Date expiry = new Date(System.currentTimeMillis() + DEFAULT_EXPIRY_TIME);

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends ObjectArgs.Builder<GetPresignedObjectUrlArgs, Builder> {

        public Builder method(HttpMethod method) {
            validateNotNull(method, "method");
            operations.add(args -> args.method = method);
            return this;
        }

        public Builder specType(boolean specType) {
            operations.add(args -> args.specType = specType);
            return this;
        }

        public Builder expiry(Date expiry) {
            validateExpiry(expiry.getTime() - System.currentTimeMillis());
            operations.add(args -> args.expiry = expiry);
            return this;
        }

        public Builder expiry(long expiry) {
            validateExpiry(expiry);
            operations.add(args -> args.expiry = new Date(expiry));
            return this;
        }

        public Builder expiry(long duration, TimeUnit unit) {
            return expiry(unit.toSeconds(duration));
        }

        private void validateExpiry(long expiry) {
            if (expiry < 1 || expiry > MAX_EXPIRY_TIME) {
                throw new IllegalArgumentException(
                        "expiry must be minimum 1 second to maximum "
                                + TimeUnit.MILLISECONDS.toDays(MAX_EXPIRY_TIME)
                                + " days");
            }
        }

    }
}
