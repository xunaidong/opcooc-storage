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
package com.opcooc.storage.clientsource;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.opcooc.storage.client.Client;
import com.opcooc.storage.client.S3Client;
import com.opcooc.storage.config.ClientType;
import com.opcooc.storage.config.StorageProperty;
import com.opcooc.storage.exception.ClientSourceException;
import com.opcooc.storage.utils.StorageChecker;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author shenqicheng
 * @since 2020-08-22 10:30
 */
@Slf4j
public class COSClientSource implements ClientSource, Closeable {

    private final StorageProperty config;
    private Client client;

    public COSClientSource(StorageProperty storageProperty) {
        // 校验配置合法性
        StorageChecker.checkS3Config(storageProperty, ClientType.S3);
        this.config = storageProperty;

    }
    public void init() throws ClientSourceException {

        AWSCredentials credentials = new BasicAWSCredentials(config.getAccessKey(), config.getSecretKey());

        AmazonS3 amazonS3 = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        config.getEndPoint(),
                        ClientType.COS.name()))
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
        log.debug("init storage client [{}] ok", ClientType.COS.name());
        this.client = new S3Client(amazonS3);
    }

    @Override
    public Client getClient() throws ClientSourceException {
        return client;
    }

    @Override
    public void close() throws IOException {
        log.debug("opcooc-storage - shutdown [{}] client", ClientType.COS.name());
        try {
            client.shutdown();
        } catch (Exception e) {
            throw new ClientSourceException(e);
        }
    }

}
