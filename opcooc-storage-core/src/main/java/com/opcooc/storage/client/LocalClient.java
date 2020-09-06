package com.opcooc.storage.client;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.opcooc.storage.config.ClientSource;
import com.opcooc.storage.config.FileBasicInfo;
import com.opcooc.storage.config.ResultConverter;
import com.opcooc.storage.config.StorageProperty;
import com.opcooc.storage.utils.StorageUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class LocalClient extends AbstractClient{

    private String locationPath;

    public LocalClient(StorageProperty config) {
        super(config, ClientSource.LOCAL);
    }

    public String getStoragePath(String path){
        return StorageUtil.getFullPath(this.locationPath, path);
    }

    @Override
    protected void init(StorageProperty config) {
        locationPath = StrUtil.isBlank(config.getLocation()) ?
                System.getProperty("user.dir") + "public" :config.getLocation();
        File file = new File(locationPath);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void createFolder(String bucketName, String path) {

    }

    @Override
    public String getBucketName() {
        return null;
    }

    @Override
    public String createBucket(String bucketName) {
        return null;
    }

    @Override
    public void deleteBucket(String bucketName) {

    }

    @Override
    public List<String> listBuckets() {
        return null;
    }

    @Override
    public boolean doesBucketExist(String bucketName) {
        return false;
    }

    @Override
    public FileBasicInfo uploadObject(String bucketName, String objectName, InputStream stream) {

        return null;
    }

    @Override
    public FileBasicInfo uploadObject(String objectName, InputStream stream) {
        FileBasicInfo info = new FileBasicInfo();
        File file = FileUtil.writeFromStream(stream, getStoragePath(objectName));
        info.setSize(file.length());
        info.setLastModified(new Date(file.lastModified()));
        return info;
    }

    @Override
    public FileBasicInfo uploadObject(String bucketName, String objectName, File file) {
        return null;
    }

    @Override
    public FileBasicInfo uploadObject(String objectName, File file) {
        FileBasicInfo info = new FileBasicInfo();
        File targetFile = FileUtil.touch(getStoragePath(objectName));
        FileUtil.copy(file, targetFile, true);
        info.setKey(targetFile.getAbsolutePath());
        info.setSize(targetFile.length());
        info.setLastModified(new Date(targetFile.lastModified()));
        return info;
    }

    @Override
    public FileBasicInfo uploadObject(String bucketName, String objectName, String fullFilePath) {
        return null;
    }

    @Override
    public FileBasicInfo uploadObject(String objectName, String fullFilePath) {
        FileBasicInfo info = new FileBasicInfo();
        File file = FileUtil.copy(fullFilePath, getStoragePath(objectName), true);
        info.setKey(file.getAbsolutePath());
        info.setSize(file.length());
        info.setLastModified(new Date(file.lastModified()));
        return info;
    }

    @Override
    public void copyObject(String bucketName, String objectName, String srcBucketName, String srcObjectName) {

    }

    @Override
    public void copyObject(String objectName, String srcObjectName) {
        FileUtil.copy(srcObjectName, getStoragePath(objectName), true);
    }

    @Override
    public List<FileBasicInfo> listObjects(String bucketName, String prefix, boolean recursive) {
        return null;
    }

    @Override
    public List<FileBasicInfo> listObjects(String prefix, boolean recursive) {
        List<File> files = FileUtil.loopFiles(prefix);
        List<FileBasicInfo> result = new ArrayList<>();
        for(File file : files){
            FileBasicInfo info = new FileBasicInfo();
            info.setKey(file.getAbsolutePath());
            info.setSize(file.length());
            info.setLastModified(new Date(file.lastModified()));
            result.add(info);
        }
        return result;
    }

    @Override
    public <T> List<T> listObjects(String bucketName, String prefix, boolean recursive, ResultConverter<T> resultConverter) {
        return null;
    }

    @Override
    public <T> List<T> listObjects(String prefix, boolean recursive, ResultConverter<T> resultConverter) {
        Assert.notNull(resultConverter, "'resultConverter' cannot be null");
        List<File> files = FileUtil.loopFiles(prefix);
        List<T> result = new ArrayList<>();
        for(File file : files){
            FileBasicInfo info = new FileBasicInfo();
            info.setKey(file.getAbsolutePath());
            info.setSize(file.length());
            info.setLastModified(new Date(file.lastModified()));
            result.add(resultConverter.convert(info));
        }
        return result;
    }

    @Override
    public FileBasicInfo getObjectMetadata(String bucketName, String objectName) {
        return null;
    }

    @Override
    public FileBasicInfo getObjectMetadata(String objectName) {

        return null;
    }

    @Override
    public <T> T getObjectMetadata(String bucketName, String objectName, ResultConverter<T> resultConverter) {
        return null;
    }

    @Override
    public <T> T getObjectMetadata(String objectName, ResultConverter<T> resultConverter) {
        return null;
    }

    @Override
    public boolean objectExist(String bucketName, String objectName) {
        return false;
    }

    @Override
    public boolean objectExist(String objectName) {
        return false;
    }

    @Override
    public InputStream getStreamObject(String bucketName, String objectName) {
        return null;
    }

    @Override
    public InputStream getStreamObject(String objectName) {
        return null;
    }

    @Override
    public File getFileObject(String bucketName, String objectName, File file) {
        return null;
    }

    @Override
    public File getFileObject(String objectName, File file) {
        return null;
    }

    @Override
    public String getFilePathObject(String bucketName, String objectName, String filePath) {
        return null;
    }

    @Override
    public String getFilePathObject(String objectName, String filePath) {
        return null;
    }

    @Override
    public byte[] getByteObject(String bucketName, String objectName) {
        return new byte[0];
    }

    @Override
    public byte[] getByteObject(String objectName) {
        return new byte[0];
    }

    @Override
    public void deleteObject(String bucketName, String objectName) {

    }

    @Override
    public void deleteObject(String objectName) {

    }

    @Override
    public void deleteObjects(String bucketName, List<String> objectNames) {

    }

    @Override
    public void deleteObjects(List<String> objectNames) {

    }

    @Override
    public String getDownloadUrl(String bucketName, String objectName, Date expiration) {
        return null;
    }

    @Override
    public String getDownloadUrl(String objectName, Date expiration) {
        return null;
    }

    @Override
    public Map<String, String> postUrl(String bucketName, String objectName, Date expiration) {
        return null;
    }

    @Override
    public String getUploadUrl(String bucketName, String objectName, Date expiration, boolean specType) {
        return null;
    }
}
