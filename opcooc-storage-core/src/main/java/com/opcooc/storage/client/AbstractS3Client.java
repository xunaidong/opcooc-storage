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
import cn.hutool.core.util.StrUtil;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * @author shenqicheng
 * @since 2020-08-22 10:30
 */
@Slf4j
public abstract class AbstractS3Client implements FileClient {

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
        StorageChecker.checkConfig(config, source);
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
    public void createFolder(String bucketName, String path) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);
        PutObjectRequest putObjectRequest =
                new PutObjectRequest(bucketName, StorageUtil.checkFolder(path), new ByteArrayInputStream(new byte[]{}), metadata);
        try {
            client.putObject(putObjectRequest);
            log.debug("create folder [{}] path success", path);
        } catch (Exception e) {
            throw new BucketException(e.getMessage());
        }
    }

    @Override
    public String getBucketName() {
        String bucket = StorageAttributeContextHolder.bucket();
        return createBucket(StrUtil.isBlank(bucket) ? config.getBucketName() : bucket);
    }

    @Override
    public String createBucket(String bucketName) {
        if (doesBucketExist(bucketName)) {
            log.debug("exists [{}] bucket name ", bucketName);
            return bucketName;
        }

        try {
            Bucket bucket = client.createBucket(bucketName);
            log.debug("create [{}] bucket name success", bucket.getName());
            return bucket.getName();
        } catch (Exception e) {
            throw new BucketException(e.getMessage());
        }
    }

    @Override
    public void deleteBucket(String bucketName) {
        ObjectListing objectListing = client.listObjects(bucketName);
        while (true) {
            for (S3ObjectSummary s3ObjectSummary : objectListing.getObjectSummaries()) {
                client.deleteObject(bucketName, s3ObjectSummary.getKey());
                log.debug("delete bucket name: [{}], object: [{}] success", bucketName, s3ObjectSummary.getKey());
            }

            if (objectListing.isTruncated()) {
                objectListing = client.listNextBatchOfObjects(objectListing);
            } else {
                break;
            }
        }
        client.deleteBucket(bucketName);
    }

    @Override
    public List<String> listBuckets() {
        try {
            List<Bucket> buckets = client.listBuckets();
            return buckets.stream().map(Bucket::getName).collect(toList());
        } catch (Exception e) {
            throw new BucketException(e.getMessage());
        }
    }

    @Override
    public boolean doesBucketExist(String bucketName) {
        try {
            return client.doesBucketExistV2(bucketName);
        } catch (Exception e) {
            throw new BucketException(e.getMessage());
        }
    }

    @Override
    public FileBasicInfo uploadObject(String bucketName, String objectName, InputStream stream) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(StorageUtil.TIKA.detect(stream));
            client.putObject(bucketName, objectName, stream, metadata);
            return getObjectMetadata(bucketName, objectName);
        } catch (Exception e) {
            throw new UploadException(e.getMessage());
        }
    }

    @Override
    public FileBasicInfo uploadObject(String objectName, InputStream stream) {
        return uploadObject(getBucketName(), objectName, stream);
    }

    @Override
    public FileBasicInfo uploadObject(String bucketName, String objectName, File file) {
        try {
            client.putObject(bucketName, objectName, file);
            return getObjectMetadata(bucketName, objectName);
        } catch (Exception e) {
            throw new UploadException(e.getMessage());
        }
    }

    @Override
    public FileBasicInfo uploadObject(String objectName, File file) {
        return uploadObject(getBucketName(), objectName, file);
    }

    @Override
    public FileBasicInfo uploadObject(String bucketName, String objectName, String fullFilePath) {
        try {
            return uploadObject(bucketName, objectName, FileUtil.touch(fullFilePath));
        } catch (Exception e) {
            throw new UploadException(e.getMessage());
        }
    }

    @Override
    public FileBasicInfo uploadObject(String objectName, String fullFilePath) {
        return uploadObject(getBucketName(), objectName, fullFilePath);
    }

    @Override
    public void copyObject(String bucketName, String objectName, String srcBucketName, String srcObjectName) {
        try {
            // Copy the object into a new object in the same bucket.
            client.copyObject(new CopyObjectRequest(bucketName, objectName, srcBucketName, srcObjectName));
        } catch (Exception e) {
            throw new ObjectException(e.getMessage());
        }
    }

    @Override
    public void copyObject(String objectName, String srcObjectName) {
        copyObject(objectName, getBucketName(), srcObjectName);
    }

    @Override
    public List<FileBasicInfo> listObjects(String bucketName, String prefix, boolean recursive) {
        return listConvertObjects(bucketName, prefix, recursive, info -> info);
    }

    @Override
    public List<FileBasicInfo> listObjects(String prefix, boolean recursive) {
        return listObjects(getBucketName(), prefix, recursive);
    }

    @Override
    public <T> List<T> listObjects(String bucketName, String prefix, boolean recursive, ResultConverter<T> resultConverter) {
        Assert.notNull(resultConverter, "'resultConverter' cannot be null");
        return listConvertObjects(bucketName, prefix, recursive, resultConverter);
    }

    @Override
    public <T> List<T> listObjects(String prefix, boolean recursive, ResultConverter<T> resultConverter) {
        return listObjects(getBucketName(), prefix, recursive, resultConverter);
    }

    private <T> List<T> listConvertObjects(String bucketName, String prefix, boolean recursive, ResultConverter<T> resultConverter) {
        ListObjectsV2Request req = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(prefix)
                .withMaxKeys(1000);

        ListObjectsV2Result result;
        List<T> objectList = new ArrayList<>();
        do {
            result = client.listObjectsV2(req);

            for (S3ObjectSummary object : result.getObjectSummaries()) {
                FileBasicInfo info = new FileBasicInfo();
                info.setKey(object.getKey());
                info.setSize(object.getSize());
                info.setBucketName(bucketName);
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
    public FileBasicInfo getObjectMetadata(String bucketName, String objectName) {
        FileBasicInfo info = new FileBasicInfo();
        try {
            ObjectMetadata object = client.getObjectMetadata(bucketName, objectName);
            if (object == null) {
                throw new ObjectException("文件不存在!");
            }
            info.setKey(objectName);
            info.setSize(object.getContentLength());
            info.setBucketName(bucketName);
            info.setLastModified(object.getLastModified());
            return info;
        } catch (Exception e) {
            throw new ObjectException(e.getMessage());
        }
    }

    @Override
    public FileBasicInfo getObjectMetadata(String objectName) {
        return getObjectMetadata(getBucketName(), objectName);
    }

    @Override
    public <T> T getObjectMetadata(String bucketName, String objectName, ResultConverter<T> resultConverter) {
        Assert.notNull(resultConverter, "'resultConverter' cannot be null");
        FileBasicInfo info = getObjectMetadata(bucketName, objectName);
        return resultConverter.convert(info);
    }

    @Override
    public <T> T getObjectMetadata(String objectName, ResultConverter<T> resultConverter) {
        return getObjectMetadata(getBucketName(), objectName, resultConverter);
    }

    @Override
    public InputStream getStreamObject(String bucketName, String objectName) {
        try {
            //判断对象是否存在
            if (!objectExist(bucketName, objectName)) {
                throw new DownloadException("文件不存在!");
            }
            S3Object s3Object = client.getObject(bucketName, objectName);
            return s3Object.getObjectContent();
        } catch (Exception e) {
            throw new DownloadException(e.getMessage());
        }
    }

    @Override
    public InputStream getStreamObject(String objectName) {
        return getStreamObject(getBucketName(), objectName);
    }

    @Override
    public boolean objectExist(String bucketName, String objectName) {
        try {
            return client.doesObjectExist(bucketName, objectName);
        } catch (Exception e) {
            throw new DownloadException(e.getMessage());
        }
    }

    @Override
    public boolean objectExist(String objectName) {
        return objectExist(getBucketName(), objectName);
    }

    @Override
    public File getFileObject(String bucketName, String objectName, File file) {
        try {
            //判断对象是否存在
            if (!objectExist(bucketName, objectName)) {
                throw new DownloadException("文件不存在!");
            }
            client.getObject(new GetObjectRequest(bucketName, objectName), file);
            return file;
        } catch (Exception e) {
            throw new DownloadException(e.getMessage());
        }
    }

    @Override
    public File getFileObject(String objectName, File file) {
        return getFileObject(getBucketName(), objectName, file);
    }

    @Override
    public String getFilePathObject(String bucketName, String objectName, String filePath) {
        try {
            //判断对象是否存在
            if (!objectExist(bucketName, objectName)) {
                throw new DownloadException("文件不存在!");
            }
            getFileObject(bucketName, objectName, FileUtil.touch(filePath));
            return filePath;
        } catch (Exception e) {
            throw new DownloadException(e.getMessage());
        }
    }

    @Override
    public String getFilePathObject(String objectName, String filePath) {
        return getFilePathObject(getBucketName(), objectName, filePath);
    }

    @Override
    public byte[] getByteObject(String bucketName, String objectName) {
        try (InputStream inputStream = getStreamObject(bucketName, objectName)) {
            return IoUtils.toByteArray(inputStream);
        } catch (Exception e) {
            throw new DownloadException(e.getMessage());
        }
    }

    @Override
    public byte[] getByteObject(String objectName) {
        return getByteObject(getBucketName(), objectName);
    }

    @Override
    public void deleteObject(String bucketName, String objectName) {
        try {
            client.deleteObject(bucketName, objectName);
            log.debug("delete object: [{}] by bucket name: [{}] success", bucketName, objectName);
        } catch (Exception e) {
            throw new ObjectException(e.getMessage());
        }
    }

    @Override
    public void deleteObject(String objectName) {
        deleteObject(getBucketName(), objectName);
    }

    @Override
    public void deleteObjects(String bucketName, List<String> objectNames) {
        if (objectNames == null || objectNames.isEmpty()) {
            return;
        }
        try {
            List<DeleteObjectsRequest.KeyVersion> objects = objectNames.stream().map(DeleteObjectsRequest.KeyVersion::new).collect(toList());
            DeleteObjectsRequest request = new DeleteObjectsRequest(bucketName).withKeys(objects);
            client.deleteObjects(request);
        } catch (Exception e) {
            throw new ObjectException(e.getMessage());
        }
    }

    @Override
    public void deleteObjects(List<String> objectNames) {
        deleteObjects(getBucketName(), objectNames);
    }

    @Override
    public String getDownloadUrl(String bucketName, String objectName, Date expiration) {
        try {
            URL url = client.generatePresignedUrl(bucketName, objectName, expiration);
            return url.toExternalForm();
        } catch (Exception e) {
            throw new PresignedException(e.getMessage());
        }
    }

    @Override
    public String getDownloadUrl(String objectName, Date expiration) {
        return getDownloadUrl(getBucketName(), objectName, expiration);
    }

    @Override
    public Map<String, String> postUrl(String bucketName, String objectName, Date expiration) {
        throw new PresignedException("method not implement!");
    }

    @Override
    public String getUploadUrl(String bucketName, String objectName, Date expiration, boolean specType) {
        try {
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, objectName)
                            .withMethod(HttpMethod.PUT)
                            .withExpiration(expiration);
            if (specType) {
                //强制前端需要在的上传方法添加对应的 Request Header( key: Content-Type, value: {fileType} )
                //不开启需要前端自行添加没有强制要求
                //用于解决文件上传到文件服务器之后没有对应的文件类型问题
                String fileType = StorageUtil.TIKA.detect(objectName);
                generatePresignedUrlRequest.putCustomRequestHeader(StorageConstant.CONTENT_TYPE, fileType);
            }
            URL url = client.generatePresignedUrl(generatePresignedUrlRequest);
            return url.toExternalForm();
        } catch (Exception e) {
            throw new PresignedException(e.getMessage());
        }
    }


    @Override
    public void shutdown() {
        client.shutdown();
    }
}
