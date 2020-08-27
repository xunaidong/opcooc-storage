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
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.opcooc.storage.config.StorageProperty;
import com.opcooc.storage.exception.*;
import com.opcooc.storage.config.ClientSource;
import com.opcooc.storage.config.FileBasicInfo;
import com.opcooc.storage.config.FileInfoProcess;
import com.opcooc.storage.utils.IoUtils;
import com.opcooc.storage.utils.StorageUtil;
import com.opcooc.storage.utils.StorageChecker;
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
 *
 *
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

    /**
     * 文件信息处理器
     */
    protected FileInfoProcess process;

    public AbstractS3Client(StorageProperty config, ClientSource source) {
        this.config = config;
        this.source = source;
        // 校验配置合法性
        StorageChecker.checkConfig(config, source);
        // 初始化client
        this.client = init(config);
    }

    @Override
    public void initPostProcess(FileInfoProcess process) {
        this.process = process;
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
        return config.getBucketName();
    }

    @Override
    public void createBucket(String bucketName) {
        if (doesBucketExist(bucketName)) {
            log.debug("exists [{}] bucket name ", bucketName);
            return;
        }

        try {
            client.createBucket(bucketName);
            log.debug("create [{}] bucket name success", bucketName);
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

            // If the bucket contains many objects, the listObjects() call
            // might not return all of the objects in the first listing. Check to
            // see whether the listing was truncated. If so, retrieve the next page of objects
            // and delete them.
            if (objectListing.isTruncated()) {
                objectListing = client.listNextBatchOfObjects(objectListing);
            } else {
                break;
            }
        }
        // After all objects and object versions are deleted, delete the bucket.
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
    public FileBasicInfo putObject(String bucketName, String objectName, InputStream stream) {
        try {
            client.putObject(bucketName, objectName, stream, null);
            return getObjectMetadata(bucketName, objectName);
        } catch (Exception e) {
            throw new UploadException(e.getMessage());
        }
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
    public FileBasicInfo uploadObject(String bucketName, String objectName, String fullFilePath) {
        try {
            return uploadObject(bucketName, objectName, FileUtil.touch(fullFilePath));
        } catch (Exception e) {
            throw new UploadException(e.getMessage());
        }
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
    public List<FileBasicInfo> listObjects(String bucketName, String prefix, boolean recursive) {
        ListObjectsV2Request req = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(prefix)
                .withMaxKeys(1000);
        ListObjectsV2Result result;
        List<FileBasicInfo> objectList = new ArrayList<>();
        do {
            result = client.listObjectsV2(req);

            for (S3ObjectSummary object : result.getObjectSummaries()) {
                String a = null;
//                FileBasicInfo info = new FileBasicInfo();
//                if (object.) {
//                    info.setParentPath(object.objectName());
//                } else {
//                    String filename = StrUtil.subAfter(object.objectName(), "/");
//                    info.setName(filename);
//                    info.setSize(object.size());
//                }
//                info.setBucketName(bucketName);
//                info.setLatestUpdateTime(object.lastModified());
////                info.setMetadata(Map.copyOf(item.userMetadata()));
//                if (process != null) {
//                    info = process.process(info);
//                }
//                objectList.add(info);
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
//            //判断对象是否存在
//            if (object == null) {
//                throw new ObjectException("文件不存在!");
//            }
//            String filename = StrUtil.subAfter(object.name(), "/");
//            if(StringUtils.isBlank(filename)){
//                info.setName(object.name());
//                info.setParentPath(null);
//            }else{
//                info.setName(filename);
//                info.setParentPath(object.name().replaceFirst(filename, ""));
//            }
//            info.setBucketName(object.bucketName());
//            info.setLatestUpdateTime(object.createdTime());
//            info.setSize(object.length());
//            info.setMetadata(Map.copyOf(object.httpHeaders()));
//            if (process != null) {
//                return process.process(info);
//            }
            return info;
        } catch (Exception e) {
            throw new ObjectException(e.getMessage());
        }
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

    private boolean objectExist(String bucketName, String objectName) {
        try {
            return client.doesObjectExist(bucketName, objectName);
        } catch (Exception e) {
            throw new DownloadException(e.getMessage());
        }
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
    public byte[] getByteObject(String bucketName, String objectName) {
        try (InputStream inputStream = getStreamObject(bucketName, objectName)) {
            return IoUtils.toByteArray(inputStream);
        } catch (Exception e) {
            throw new DownloadException(e.getMessage());
        }
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
    public String getUrl(String bucketName, String objectName, Date expiration) {
        try {
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, objectName)
                            .withMethod(HttpMethod.GET)
                            .withExpiration(expiration);

            URL url = client.generatePresignedUrl(generatePresignedUrlRequest);
            return url.toExternalForm();
        } catch (Exception e) {
            throw new PresignedException(e.getMessage());
        }
    }

    @Override
    public Map<String, String> postUrl(String bucketName, String objectName, Date expiration) {
        return null;
    }

    @Override
    public String putUrl(String bucketName, String objectName, Date expiration) {
        try {
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, objectName)
                            .withMethod(HttpMethod.PUT)
                            .withExpiration(expiration);

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
