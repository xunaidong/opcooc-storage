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
import java.sql.SQLException;

@Slf4j
public class S3ClientSource implements ClientSource, Closeable {

    private final StorageProperty config;
    private Client client;

    public S3ClientSource(StorageProperty storageProperty) {
        // 校验配置合法性
        StorageChecker.checkS3Config(storageProperty, ClientType.S3);
        this.config = storageProperty;

    }

    public void init() throws ClientSourceException {

        AWSCredentials credentials = new BasicAWSCredentials(config.getAccessKey(), config.getSecretKey());

        AmazonS3 amazonS3 = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withPathStyleAccessEnabled("path-style".equals(config.getPathStyle()))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        config.getEndPoint(),
                        ClientType.S3.name()))
                .build();

        log.debug("init storage client [{}] ok", ClientType.S3.name());

        this.client = new S3Client(amazonS3);
    }

    @Override
    public Client getClient() throws ClientSourceException {
        return client;
    }

    @Override
    public void close() throws IOException {
        log.debug("opcooc-storage - shutdown [{}] client", ClientType.S3.name());
        try {
            client.shutdown();
        } catch (Exception e) {
            throw new ClientSourceException(e);
        }
    }
}
