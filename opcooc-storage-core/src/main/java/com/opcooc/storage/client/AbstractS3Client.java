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

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.opcooc.storage.arguments.*;
import com.opcooc.storage.config.ResultConverter;
import com.opcooc.storage.config.StorageProperty;
import com.opcooc.storage.exception.*;
import com.opcooc.storage.config.ClientSource;
import com.opcooc.storage.config.FileBasicInfo;
import com.opcooc.storage.utils.*;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author shenqicheng
 * @since 2020-08-22 10:30
 */
@Slf4j
public abstract class AbstractS3Client implements FileClient {

    private static final String ERROR_MESSAGE = "bucketName: [%s], file [%s] does not exist";

    /**
     * client
     */
    protected final AmazonS3 client;

    /**
     * client 配置
     */
    protected final StorageProperty config;

    /**
     * client 来源
     */
    protected final ClientSource source;

    public AbstractS3Client(StorageProperty config, ClientSource source) {
        this.config = config;
        this.source = source;
        // 校验配置合法性
        StorageChecker.checkS3Config(config, source);
        // 初始化client
        this.client = init(config);
    }

    /**
     * 初始化client
     *
     * @param config 配置
     * @return s3 client
     */
    protected abstract AmazonS3 init(StorageProperty config);

    @Override
    public void createFolder(SetFolderArgs args) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);
        PutObjectRequest putObjectRequest =
                new PutObjectRequest(args.getBucketName(), StorageUtil.checkFolder(args.getFolderName()), new ByteArrayInputStream(new byte[]{}), metadata);
        try {
            client.putObject(putObjectRequest);
        } catch (Exception e) {
            throw new ClientSourceException(e);
        }
    }

    @Override
    public String getBucketName() {
        String bucketName = config.getBucketName();
        if (config.getAutoAddBucketName()) {
            return createBucket(CreateBucketArgs.builder().bucket(bucketName).build());
        }
        return bucketName;
    }

    @Override
    public String createBucket(CreateBucketArgs args) {
        try {
            if (client.doesBucketExistV2(args.getBucketName())) {
                log.debug("opcooc-storage - exist [{}] bucketNamed ", args.getBucketName());
                return args.getBucketName();
            }
            Bucket bucket = client.createBucket(args.getBucketName());
            log.debug("opcooc-storage - create [{}] bucketName success", bucket.getName());
            return bucket.getName();
        } catch (Exception e) {
            throw new ClientSourceException(e);
        }
    }

    @Override
    public void deleteBucket(DeleteBucketArgs args) {
        try {
            ObjectListing objectListing = client.listObjects(args.getBucketName());
            while (true) {
                for (S3ObjectSummary s3ObjectSummary : objectListing.getObjectSummaries()) {
                    client.deleteObject(args.getBucketName(), s3ObjectSummary.getKey());
                    log.debug("opcooc-storage - bucketName: [{}], delete object: [{}] success", args.getBucketName(), s3ObjectSummary.getKey());
                }

                if (objectListing.isTruncated()) {
                    objectListing = client.listNextBatchOfObjects(objectListing);
                } else {
                    break;
                }
            }
            client.deleteBucket(args.getBucketName());
        } catch (Exception e) {
            throw new ClientSourceException(e);
        }

    }

    @Override
    public List<String> listBuckets() {
        try {
            List<Bucket> buckets = client.listBuckets();
            List<String> result = buckets.stream().map(Bucket::getName).collect(toList());
            log.debug("opcooc-storage - bucketNames: [{}]", result);
            return result;
        } catch (Exception e) {
            throw new ClientSourceException(e);
        }
    }

    @Override
    public boolean doesBucketExist(DoesBucketExistArgs args) {
        try {
            return client.doesBucketExistV2(args.getBucketName());
        } catch (Exception e) {
            throw new ClientSourceException(e);
        }
    }

    @Override
    public FileBasicInfo uploadObject(UploadObjectArgs args) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(args.getStream().available());
            metadata.setContentType(args.contentType());
            client.putObject(args.getBucketName(), args.getObjectName(), args.getStream(), metadata);
            return getObjectMetadata(ObjectMetadataArgs.builder().bucket(args.getBucketName()).object(args.getObjectName()).build());
        } catch (Exception e) {
            throw new ClientSourceException(e);
        }
    }

    @Override
    public FileBasicInfo uploadFile(UploadFileArgs args) {
        try {
            client.putObject(args.getBucketName(), args.getObjectName(), args.getFile());
            return getObjectMetadata(ObjectMetadataArgs.builder().bucket(args.getBucketName()).object(args.getObjectName()).build());
        } catch (Exception e) {
            throw new ClientSourceException(e);
        }
    }

    @Override
    public FileBasicInfo uploadPath(UploadPathArgs args) {
        try {
            client.putObject(args.getBucketName(), args.getObjectName(), FileUtil.touch(args.getFilename()));
            return getObjectMetadata(ObjectMetadataArgs.builder().bucket(args.getBucketName()).object(args.getObjectName()).build());
        } catch (Exception e) {
            throw new ClientSourceException(e);
        }
    }

    @Override
    public void copyObject(CopyObjectArgs args) {
        CopySource source = args.getSource();
        log.debug("opcooc-storage - sourceBucketName: [{}], sourceKey: [{}], destinationBucketName: [{}], destinationKey: [{}] copy success",
                source.getBucketName(), source.getObjectName(), args.getBucketName(), args.getObjectName());
        try {
            client.copyObject(new CopyObjectRequest(source.getBucketName(), source.getObjectName(), args.getBucketName(), args.getObjectName()));
        } catch (Exception e) {
            throw new ClientSourceException(e);
        }
    }

    @Override
    public List<FileBasicInfo> listObjects(ListObjectsArgs args) {
        return listConvertObjects(args, info -> info);
    }

    @Override
    public <T> List<T> listObjects(ListObjectsArgs args, ResultConverter<T> resultConverter) {
        Assert.notNull(resultConverter, "'resultConverter' cannot be null");
        return listConvertObjects(args, resultConverter);
    }

    private <T> List<T> listConvertObjects(ListObjectsArgs args, ResultConverter<T> resultConverter) {
        ListObjectsV2Request req = new ListObjectsV2Request()
                .withBucketName(args.getBucketName())
                .withPrefix(args.getPrefix())
                .withMaxKeys(args.getMaxKeys());

        ListObjectsV2Result result;
        List<T> objectList = new ArrayList<>();
        do {
            result = client.listObjectsV2(req);

            for (S3ObjectSummary object : result.getObjectSummaries()) {
                FileBasicInfo info = new FileBasicInfo();
                info.setKey(object.getKey());
                info.setSize(object.getSize());
                info.setBucketName(args.getBucketName());
                info.setLastModified(object.getLastModified());
                objectList.add(resultConverter.convert(info));
            }
            // If there are more than maxKeys keys in the bucket, get a continuation token
            // and list the next objects.
            req.setContinuationToken(result.getNextContinuationToken());
        } while (result.isTruncated());
        return objectList;
    }

    @Override
    public FileBasicInfo getObjectMetadata(ObjectMetadataArgs args) {
        FileBasicInfo info = new FileBasicInfo();
        try {
            ObjectMetadata object = client.getObjectMetadata(args.getBucketName(), args.getObjectName());
            if (object == null) {
                throw new ClientSourceException(ERROR_MESSAGE, args.getBucketName(), args.getObjectName());
            }
            info.setKey(args.getObjectName());
            info.setSize(object.getContentLength());
            info.setBucketName(args.getBucketName());
            info.setLastModified(object.getLastModified());
            log.debug("opcooc-storage - objectMetadata: [{}]", info.toString());
            return info;
        } catch (Exception e) {
            throw new ClientSourceException(e);
        }
    }

    @Override
    public <T> T getObjectMetadata(ObjectMetadataArgs args, ResultConverter<T> resultConverter) {
        Assert.notNull(resultConverter, "'resultConverter' cannot be null");
        FileBasicInfo info = getObjectMetadata(args);
        return resultConverter.convert(info);
    }

    @Override
    public boolean objectExist(DoesObjectExistArgs args) {
        try {
            return client.doesObjectExist(args.getBucketName(), args.getObjectName());
        } catch (Exception e) {
            throw new ClientSourceException(e);
        }
    }

    @Override
    public InputStream getObjectToStream(GetObjectToStreamArgs args) {
        try {
            //判断对象是否存在
            if (!objectExist(DoesObjectExistArgs.builder().bucket(args.getBucketName()).object(args.getObjectName()).build())) {
                throw new ClientSourceException(ERROR_MESSAGE, args.getBucketName(), args.getObjectName());
            }
            S3Object s3Object = client.getObject(args.getBucketName(), args.getObjectName());
            return s3Object.getObjectContent();
        } catch (Exception e) {
            throw new ClientSourceException(e);
        }
    }

    @Override
    public File geObjectToFile(GetObjectToFileArgs args) {
        try {
            //判断对象是否存在
            if (!objectExist(DoesObjectExistArgs.builder().bucket(args.getBucketName()).object(args.getObjectName()).build())) {
                throw new ClientSourceException(ERROR_MESSAGE, args.getBucketName(), args.getObjectName());
            }
            client.getObject(new GetObjectRequest(args.getBucketName(), args.getObjectName()), args.getFile());
            return args.getFile();
        } catch (Exception e) {
            throw new ClientSourceException(e);
        }
    }

    @Override
    public String getObjectToPath(GetObjectToPathArgs args) {
        try {
            //判断对象是否存在
            if (!objectExist(DoesObjectExistArgs.builder().bucket(args.getBucketName()).object(args.getObjectName()).build())) {
                throw new ClientSourceException(ERROR_MESSAGE, args.getBucketName(), args.getObjectName());
            }
            GetObjectToFileArgs toFileArgs = GetObjectToFileArgs.builder()
                    .bucket(args.getBucketName()).object(args.getObjectName()).file(FileUtil.touch(args.getPath())).build();
            geObjectToFile(toFileArgs);
            return args.getPath();
        } catch (Exception e) {
            throw new ClientSourceException(e);
        }
    }

    @Override
    public byte[] getObjectToBytes(GetObjectToBytesArgs args) {
        GetObjectToStreamArgs toStreamArgs = GetObjectToStreamArgs.builder().bucket(args.getBucketName()).object(args.getObjectName()).build();
        try (InputStream inputStream = getObjectToStream(toStreamArgs)) {
            return IoUtils.toByteArray(inputStream);
        } catch (Exception e) {
            throw new ClientSourceException(e);
        }
    }

    @Override
    public void deleteObject(DeleteObjectArgs args) {
        try {
            client.deleteObject(args.getBucketName(), args.getObjectName());
        } catch (Exception e) {
            throw new ClientSourceException(e);
        }
    }

    @Override
    public void deleteObjects(DeleteObjectsArgs args) {
        try {
            List<DeleteObjectsRequest.KeyVersion> objects = args.getObjects().stream().map(DeleteObjectsRequest.KeyVersion::new).collect(toList());
            DeleteObjectsRequest request = new DeleteObjectsRequest(args.getBucketName()).withKeys(objects);
            client.deleteObjects(request);
        } catch (Exception e) {
            throw new ClientSourceException(e);
        }
    }

    @Override
    public String generatePresignedUrl(GetPresignedObjectUrlArgs args) {
        try {
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(args.getBucketName(), args.getObjectName())
                    .withMethod(args.getMethod())
                    .withExpiration(args.getExpiry());

            if (args.isSpecType() && (args.getMethod() == HttpMethod.PUT || args.getMethod() == HttpMethod.POST)) {
                //强制前端需要在的上传方法添加对应的 Request Header( key: Content-Type, value: {fileType} )
                //不开启需要前端自行添加没有强制要求
                //用于解决文件上传到文件服务器之后没有对应的文件类型问题
                String fileType = StorageUtil.TIKA.detect(args.getObjectName());
                request.putCustomRequestHeader(StorageConstant.CONTENT_TYPE, fileType);
            }

            URL url = client.generatePresignedUrl(request);
            return url.toExternalForm();
        } catch (Exception e) {
            throw new ClientSourceException(e);
        }
    }

    @Override
    public void shutdown() {
        log.debug("opcooc-storage - shutdown [{}] client", source.name());
        try {
            client.shutdown();
        } catch (Exception e) {
            throw new ClientSourceException(e);
        }
    }
}
