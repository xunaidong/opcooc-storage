package com.opcooc.storage.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 动态 切换 client 和 bucketName(或者文件第一个path)
 * @author shenqicheng
 * @since 2020-08-30 10:30
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Storage {

    /**
     * client name
     * @return The name of the client to be switch
     */
    String client();

    /**
     * bucket name
     * @return The name of the bucket to be switch
     */
    String bucket();
}
