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

import com.opcooc.storage.config.FileBasicInfo;
import com.opcooc.storage.config.ResultConverter;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author shenqicheng
 * @since 2020-08-22 10:30
 */
public interface FileClient {

    //--------------------------------------shutdown start--------------------------------------

    /**
     * 关闭资源
     */
    void shutdown();

    //--------------------------------------shutdown end--------------------------------------

    //--------------------------------------folder start--------------------------------------

    /**
     * 创建文件夹
     *
     * @param bucketName 存储桶名称
     * @param path       文件夹名称
     */
    void createFolder(String bucketName, String path);

    /**
     * 创建文件夹
     *
     * @param path 文件夹名称
     */
    default void createFolder(String path) {
        createFolder(getBucketName(), path);
    }

    //--------------------------------------folder end--------------------------------------

    //--------------------------------------bucket method start--------------------------------------

    /**
     * 获取获取当前环境的桶名称
     *
     * @return 存储桶名称
     */
    String getBucketName();

    /**
     * 创建存储桶
     *
     * @param bucketName 存储桶名称
     * @return 存储桶名称
     */
    String createBucket(String bucketName);

    /**
     * 删除存储桶
     *
     * @param bucketName 存储桶名称
     */
    void deleteBucket(String bucketName);

    /**
     * 获取所有存储桶名称
     *
     * @return 名称集合
     */
    List<String> listBuckets();

    /**
     * 判断桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return 是否存在bucket
     */
    boolean doesBucketExist(String bucketName);

    //--------------------------------------bucket method end--------------------------------------

    //--------------------------------------upload file start--------------------------------------

    /**
     * 上传文件到服务器
     *
     * @param bucketName 存储桶名称
     * @param objectName 文件完整路径
     * @param stream     文件流
     * @return 文件上传后的信息
     */
    FileBasicInfo uploadObject(String bucketName, String objectName, InputStream stream);

    /**
     * 上传文件到服务器
     *
     * @param objectName 文件完整路径
     * @param stream     文件流
     * @return 文件上传后的信息
     */
    FileBasicInfo uploadObject(String objectName, InputStream stream);

    /**
     * 上传文件到服务器
     *
     * @param bucketName 存储桶名称
     * @param objectName 文件完整路径
     * @param file       文件
     * @return 文件上传后的信息
     */
    FileBasicInfo uploadObject(String bucketName, String objectName, File file);

    /**
     * 上传文件到服务器
     *
     * @param objectName 文件完整路径
     * @param file       文件
     * @return 文件上传后的信息
     */
    FileBasicInfo uploadObject(String objectName, File file);

    /**
     * 上传文件到服务器
     *
     * @param bucketName   存储桶名称
     * @param objectName   文件完整路径
     * @param fullFilePath 文件路径
     * @return 文件上传后的信息
     */
    FileBasicInfo uploadObject(String bucketName, String objectName, String fullFilePath);

    /**
     * 上传文件到服务器
     *
     * @param objectName   文件完整路径
     * @param fullFilePath 文件路径
     * @return 文件上传后的信息
     */
    FileBasicInfo uploadObject(String objectName, String fullFilePath);
    //--------------------------------------upload file end--------------------------------------

    //--------------------------------------copy file start--------------------------------------

    /**
     * 复制文件
     *
     * @param bucketName    源存储桶名称
     * @param objectName    源文件完整路径
     * @param srcBucketName 目标存储桶名称
     * @param srcObjectName 目标文件完整路径
     */
    void copyObject(String bucketName, String objectName, String srcBucketName, String srcObjectName);

    /**
     * 复制文件
     *
     * @param objectName    源文件完整路径
     * @param srcBucketName 目标存储桶名称
     * @param srcObjectName 目标文件完整路径
     */
    default void copyObject(String objectName, String srcBucketName, String srcObjectName) {
        copyObject(getBucketName(), objectName, srcBucketName, srcObjectName);
    }

    /**
     * 复制文件
     *
     * @param objectName    源文件完整路径
     * @param srcObjectName 目标文件完整路径
     */
    void copyObject(String objectName, String srcObjectName);

    //--------------------------------------copy file end--------------------------------------

    //--------------------------------------get file Metadata start--------------------------------------

    /**
     * 获取指定存储桶名称 指定前缀 的下级所有文件
     *
     * @param bucketName 存储桶名称
     * @param prefix     指定前缀
     * @param recursive  是否递归
     * @return 文件信息集合
     */
    List<FileBasicInfo> listObjects(String bucketName, String prefix, boolean recursive);

    /**
     * 获取指定存储桶名称 指定前缀 的下级所有文件
     *
     * @param prefix    指定前缀
     * @param recursive 是否递归
     * @return 文件信息集合
     */
    List<FileBasicInfo> listObjects(String prefix, boolean recursive);

    /**
     * 获取指定存储桶名称 指定前缀 的下级所有文件
     *
     * @param bucketName 存储桶名称
     * @param prefix     指定前缀
     * @param recursive  是否递归
     * @param resultConverter  文件信息转换器
     * @param <T> 泛型
     * @return 文件信息集合
     */
    <T> List<T> listObjects(String bucketName, String prefix, boolean recursive, ResultConverter<T> resultConverter);

    /**
     * 获取指定存储桶名称 指定前缀 的下级所有文件
     *
     * @param prefix    指定前缀
     * @param recursive 是否递归
     * @param resultConverter  文件信息转换器
     * @param <T> 泛型
     * @return 文件信息集合
     */
    <T> List<T> listObjects(String prefix, boolean recursive, ResultConverter<T> resultConverter);

    /**
     * 获取对象元数据
     *
     * @param bucketName 储桶名称
     * @param objectName 文件完整路径
     * @return 文件信息
     */
    FileBasicInfo getObjectMetadata(String bucketName, String objectName);

    /**
     * 获取对象元数据
     *
     * @param objectName 文件完整路径
     * @return 文件信息
     */
    FileBasicInfo getObjectMetadata(String objectName);

    /**
     * 获取对象元数据
     *
     * @param bucketName 储桶名称
     * @param objectName 文件完整路径
     * @param resultConverter  文件信息转换器
     * @param <T> 泛型
     * @return 文件信息
     */
    <T> T getObjectMetadata(String bucketName, String objectName, ResultConverter<T> resultConverter);

    /**
     * 获取对象元数据
     *
     * @param objectName 文件完整路径
     * @param resultConverter  文件信息转换器
     * @param <T> 泛型
     * @return 文件信息
     */
    <T> T getObjectMetadata(String objectName, ResultConverter<T> resultConverter);

    /**
     * 判断对象是否存在
     *
     * @param bucketName 储桶名称
     * @param objectName 文件完整路径
     * @return 结果
     */
    boolean objectExist(String bucketName, String objectName);

    /**
     * 判断对象是否存在
     *
     * @param objectName 文件完整路径
     * @return 结果
     */
    boolean objectExist(String objectName);
    //--------------------------------------get file Metadata end--------------------------------------

    //--------------------------------------get file object start--------------------------------------

    /**
     * 获得文件 InputStream
     *
     * @param bucketName 储桶名称
     * @param objectName 文件完整路径
     * @return InputStream
     */
    InputStream getStreamObject(String bucketName, String objectName);

    /**
     * 获得文件 InputStream
     *
     * @param objectName 文件完整路径
     * @return InputStream
     */
    InputStream getStreamObject(String objectName);

    /**
     * 获得文件
     *
     * @param bucketName 储桶名称
     * @param objectName 文件完整路径
     * @param file 文件
     * @return 文件
     */
    File getFileObject(String bucketName, String objectName, File file);

    /**
     * 获得文件
     *
     * @param objectName 文件完整路径
     * @param file 文件
     * @return 文件
     */
    File getFileObject(String objectName, File file);

    /**
     * 获得文件
     *
     * @param bucketName 储桶名称
     * @param objectName 文件完整路径
     * @param filePath   文件path
     * @return 文件path
     */
    String getFilePathObject(String bucketName, String objectName, String filePath);

    /**
     * 获得文件
     *
     * @param objectName 文件完整路径
     * @param filePath   文件path
     * @return 文件path
     */
    String getFilePathObject(String objectName, String filePath);

    /**
     * 获得文件 byte[]
     *
     * @param bucketName 储桶名称
     * @param objectName 文件完整路径
     * @return byte
     */
    byte[] getByteObject(String bucketName, String objectName);

    /**
     * 获得文件 byte[]
     *
     * @param objectName 文件完整路径
     * @return byte
     */
    byte[] getByteObject(String objectName);
    //--------------------------------------get file object end--------------------------------------

    //--------------------------------------delete file start--------------------------------------

    /**
     * 删除单个文件
     *
     * @param bucketName 储桶名称
     * @param objectName 文件完整路径
     */
    void deleteObject(String bucketName, String objectName);

    /**
     * 删除单个文件
     *
     * @param objectName 文件完整路径
     */
    void deleteObject(String objectName);

    /**
     * 删除文件集合
     *
     * @param bucketName  储桶名称
     * @param objectNames 文件完整路径集合
     */
    void deleteObjects(String bucketName, List<String> objectNames);

    /**
     * 删除文件集合
     *
     * @param objectNames 文件完整路径集合
     */
    void deleteObjects(List<String> objectNames);
    //--------------------------------------delete file end--------------------------------------

    //--------------------------------------get download url start--------------------------------------

    /**
     * 生成签名的URL，以使用get的HTTP方法访问文件
     *
     * @param bucketName 储桶名称
     * @param objectName 文件完整路径
     * @param expiration 过期时间
     * @return 签名url
     */
    String getDownloadUrl(String bucketName, String objectName, Date expiration);

    /**
     * 生成签名的URL，以使用get的HTTP方法访问文件
     *
     * @param objectName 文件完整路径
     * @param expiration 过期时间
     * @return 签名url
     */
    String getDownloadUrl(String objectName, Date expiration);

    /**
     * 生成签名的URL，以使用get的HTTP方法访问文件(默认为5分钟)
     *
     * @param objectName 文件完整路径
     * @return 签名url
     */
    default String getDownloadUrl(String objectName) {
        Date expiration = new Date(System.currentTimeMillis() + 5 * 60 * 1000);
        return getDownloadUrl(objectName, expiration);
    }

    //--------------------------------------get download url start--------------------------------------

    //--------------------------------------get upload signature url start--------------------------------------


    /**
     * 生成签名的URL，以使用post的HTTP方法访问
     *
     * @param bucketName 储桶名称
     * @param objectName 文件完整路径
     * @param expiration 过期时间
     * @return post body 信息
     */
    Map<String, String> postUrl(String bucketName, String objectName, Date expiration);

    /**
     * 生成签名的URL，以使用post的HTTP方法访问
     *
     * @param objectName 文件完整路径
     * @param expiration 过期时间
     * @return post body 信息
     */
    default Map<String, String> postUrl(String objectName, Date expiration) {
        return postUrl(getBucketName(), objectName, expiration);
    }

    /**
     * 生成签名的URL，以使用post的HTTP方法访问(默认为5分钟)
     *
     * @param objectName 文件完整路径
     * @return post body 信息
     */
    default Map<String, String> postUrl(String objectName) {
        Date expiration = new Date(System.currentTimeMillis() + 5 * 60 * 1000);
        return postUrl(objectName, expiration);
    }

    /**
     * 生成签名的URL，以使用特put的HTTP方法访问
     *
     * @param bucketName 储桶名称
     * @param objectName 文件完整路径
     * @param expiration 过期时间
     * @param specType 是否添加文件类型验证
     * @return 生成签名的URL
     */
    String getUploadUrl(String bucketName, String objectName, Date expiration, boolean specType);

    /**
     * 生成签名的URL，以使用特put的HTTP方法访问
     *
     * @param objectName 文件完整路径
     * @param expiration 过期时间
     * @return 生成签名的URL
     */
    default String getUploadUrl(String objectName, Date expiration) {
        return getUploadUrl(getBucketName(), objectName, expiration, false);
    }

    /**
     * 生成签名的URL，以使用特put的HTTP方法访问(默认为5分钟)
     *
     * @param objectName 文件完整路径
     * @return 生成签名的URL
     */
    default String getUploadUrl(String objectName) {
        Date expiration = new Date(System.currentTimeMillis() + 5 * 60 * 1000);
        return getUploadUrl(objectName, expiration);
    }

    /**
     * 生成签名的URL，以使用特put的HTTP方法访问
     *
     * @param objectName 文件完整路径
     * @param expiration 过期时间
     * @param specType 是否添加文件类型验证
     * @return 生成签名的URL
     */
    default String getUploadUrl(String objectName, Date expiration, boolean specType) {
        return getUploadUrl(getBucketName(), objectName, expiration, specType);
    }

    /**
     * 生成签名的URL，以使用特put的HTTP方法访问(默认为5分钟)
     *
     * @param objectName 文件完整路径
     * @param specType 是否添加文件类型验证
     * @return 生成签名的URL
     */
    default String getUploadUrl(String objectName, boolean specType) {
        Date expiration = new Date(System.currentTimeMillis() + 5 * 60 * 1000);
        return getUploadUrl(objectName, expiration, specType);
    }
    //--------------------------------------get upload signature url end--------------------------------------

}
