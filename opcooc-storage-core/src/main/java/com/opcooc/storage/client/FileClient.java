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

import com.opcooc.storage.arguments.*;
import com.opcooc.storage.config.FileBasicInfo;
import com.opcooc.storage.config.ResultConverter;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
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
     * @param args 参数
     */
    void createFolder(SetFolderArgs args);

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
     * @param args 参数
     * @return 存储桶名称
     */
    String createBucket(CreateBucketArgs args);

    /**
     * 删除存储桶
     *
     * @param args 参数
     */
    void deleteBucket(DeleteBucketArgs args);

    /**
     * 获取所有存储桶名称
     *
     * @return 名称集合
     */
    List<String> listBuckets();

    /**
     * 判断桶是否存在
     *
     * @param args 参数
     * @return 是否存在bucket
     */
    boolean doesBucketExist(DoesBucketExistArgs args);

    //--------------------------------------bucket method end--------------------------------------

    //--------------------------------------upload file start--------------------------------------

    /**
     * 上传文件到服务器
     *
     * @param args 参数
     * @return 文件上传后的信息
     */
    FileBasicInfo uploadObject(UploadObjectArgs args);

    /**
     * 上传文件到服务器
     *
     * @param args 参数
     * @return 文件上传后的信息
     */
    FileBasicInfo uploadFile(UploadFileArgs args);


    /**
     * 上传文件到服务器
     *
     * @param args 参数
     * @return 文件上传后的信息
     */
    FileBasicInfo uploadPath(UploadPathArgs args);

    //--------------------------------------upload file end--------------------------------------

    //--------------------------------------copy file start--------------------------------------

    /**
     * 复制文件
     *
     * @param args 参数
     */
    void copyObject(CopyObjectArgs args);

    //--------------------------------------copy file end--------------------------------------

    //--------------------------------------get file Metadata start--------------------------------------

    /**
     * 获取指定存储桶名称 指定前缀 的下级所有文件
     *
     * @param args 参数
     * @return 文件信息集合
     */
    List<FileBasicInfo> listObjects(ListObjectsArgs args);


    /**
     * 获取指定存储桶名称 指定前缀 的下级所有文件
     *
     * @param args            参数
     * @param resultConverter 文件信息转换器
     * @param <T>             泛型
     * @return 文件信息集合
     */
    <T> List<T> listObjects(ListObjectsArgs args, ResultConverter<T> resultConverter);

    /**
     * 获取对象元数据
     *
     * @param args 参数
     * @return 文件信息
     */
    FileBasicInfo getObjectMetadata(ObjectMetadataArgs args);

    /**
     * 获取对象元数据
     *
     * @param args            参数
     * @param resultConverter 文件信息转换器
     * @param <T>             泛型
     * @return 文件信息
     */
    <T> T getObjectMetadata(ObjectMetadataArgs args, ResultConverter<T> resultConverter);

    /**
     * 判断对象是否存在
     *
     * @param args 参数
     * @return 结果
     */
    boolean objectExist(DoesObjectExistArgs args);

    //--------------------------------------get file Metadata end--------------------------------------

    //--------------------------------------get file object start--------------------------------------

    /**
     * 获得文件 InputStream
     *
     * @param args 参数
     * @return InputStream
     */
    InputStream getObjectToStream(GetObjectToStreamArgs args);

    /**
     * 获得文件
     *
     * @param args 参数
     * @return 文件
     */
    File geObjectToFile(GetObjectToFileArgs args);

    /**
     * 获得文件
     *
     * @param args 参数
     * @return 文件path
     */
    String getObjectToPath(GetObjectToPathArgs args);

    /**
     * 获得文件 byte[]
     *
     * @param args 参数
     * @return byte
     */
    byte[] getObjectToBytes(GetObjectToBytesArgs args);

    //--------------------------------------get file object end--------------------------------------

    //--------------------------------------delete file start--------------------------------------

    /**
     * 删除单个文件
     *
     * @param args 参数
     */
    void deleteObject(DeleteObjectArgs args);


    /**
     * 删除文件集合
     *
     * @param args 参数
     */
    void deleteObjects(DeleteObjectsArgs args);

    //--------------------------------------delete file end--------------------------------------

    //--------------------------------------get generate presigned url start--------------------------------------

    /**
     * 生成签名的URL，以使用get的HTTP方法访问文件
     *
     * @param args 参数
     * @return 签名url
     */
    String generatePresignedUrl(GetPresignedObjectUrlArgs args);

    //--------------------------------------get generate presigned url start--------------------------------------

}
