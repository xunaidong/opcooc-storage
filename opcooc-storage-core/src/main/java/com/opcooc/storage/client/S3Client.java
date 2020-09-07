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
package com.opcooc.storage.client;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.opcooc.storage.config.ClientSource;
import com.opcooc.storage.config.StorageProperty;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shenqicheng
 * @since 2020-08-22 10:30
 */
@Slf4j
public class S3Client extends AbstractS3Client {

    public S3Client(StorageProperty config) {
        super(config, ClientSource.S3);
    }

    @Override
    public AmazonS3 init(StorageProperty config) {

        AWSCredentials credentials = new BasicAWSCredentials(config.getAccessKey(), config.getSecretKey());

        AmazonS3 client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withPathStyleAccessEnabled("path-style".equals(config.getPathStyle()))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        config.getEndPoint(),
                        source.name()))
                .build();

        log.debug("init storage client [{}] ok", source.name());

        return client;
    }


}
