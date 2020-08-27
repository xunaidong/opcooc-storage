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
package com.opcooc.storage.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 上传文件类型获取 <br>
 * 参考 https://www.cnblogs.com/yuchuan/p/File_ContentType_Util.html
 * @author shenqicheng
 * @since 2020-08-22 10:30
 */
public class FileContentType {

    // 流上传
    public static final String STREAM_CONTENT_TYPE = "application/octet-stream";

    public final static Map<String, String> FILE_TYPE = new HashMap<>();

    static {
        //------------------------ 图片 ------------------------
        FILE_TYPE.put("jpe", "image/jpeg");
        FILE_TYPE.put("jpeg", "image/jpeg");
        FILE_TYPE.put("jpg", "image/jpeg");
        FILE_TYPE.put("bmp", "image/bmp");
        FILE_TYPE.put("png", "image/png");
        FILE_TYPE.put("gif", "image/gif");

        //------------------------ 视频 ------------------------
        FILE_TYPE.put("flv", "video/x-flv");
        FILE_TYPE.put("3gp", "video/3gpp");
        FILE_TYPE.put("avi", "video/x-msvideo");
        FILE_TYPE.put("mp4", "video/mp4");
        FILE_TYPE.put("wmv", "video/x-ms-wmv");

        //------------------------ 音频 ------------------------
        FILE_TYPE.put("mp3", "audio/mpeg");
        FILE_TYPE.put("wma", "audio/x-ms-wma");

        //------------------------ 压缩文档 ------------------------
        FILE_TYPE.put("7z", "application/x-7z-compressed");
        FILE_TYPE.put("z", "application/x-compress");
        FILE_TYPE.put("zip", "application/x-zip-compressed");
        FILE_TYPE.put("tgz", "application/x-compressed");
        FILE_TYPE.put("jar", "application/java-archive");

        //------------------------ 文档 ------------------------
        // txt
        FILE_TYPE.put("txt", "text/plain");

        // css
        FILE_TYPE.put("css", "text/css");

        // html
        FILE_TYPE.put("htm", "text/html");
        FILE_TYPE.put("html", "text/html");
        FILE_TYPE.put("shtml", "text/html");

        // xml
        FILE_TYPE.put("wsdl", "text/xml");
        FILE_TYPE.put("xml", "text/xml");

        // pdf
        FILE_TYPE.put("pdf", "application/pdf");

        // ppt
        FILE_TYPE.put("ppt", "application/vnd.ms-powerpoint");
        FILE_TYPE.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");

        // doc
        FILE_TYPE.put("doc", "application/msword");
        FILE_TYPE.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

        // excel
        FILE_TYPE.put("xlm", "application/vnd.ms-excel");
        FILE_TYPE.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        // note
        FILE_TYPE.put("one", "application/onenote");

        // access
        FILE_TYPE.put("accdb", "application/msaccess");

        // visio
        FILE_TYPE.put("vsd", "application/vnd.visio");


        FILE_TYPE.put("hxt", "text/html");

        FILE_TYPE.put("vssettings", "text/xml");
        FILE_TYPE.put("vstemplate", "text/xml");
        FILE_TYPE.put("vml", "text/xml");
        FILE_TYPE.put("vsct", "text/xml");
        FILE_TYPE.put("vsixlangpack", "text/xml");
        FILE_TYPE.put("vsixmanifest", "text/xml");
        FILE_TYPE.put("exe.config", "text/xml");
        FILE_TYPE.put("disco", "text/xml");
        FILE_TYPE.put("dll.config", "text/xml");
        FILE_TYPE.put("AddIn", "text/xml");
        FILE_TYPE.put("dtd", "text/xml");
        FILE_TYPE.put("dtsConfig", "text/xml");
        FILE_TYPE.put("mno", "text/xml");
        FILE_TYPE.put("xrm-ms", "text/xml");
        FILE_TYPE.put("xsd", "text/xml");
        FILE_TYPE.put("xsf", "text/xml");
        FILE_TYPE.put("xsl", "text/xml");
        FILE_TYPE.put("xslt", "text/xml");
        FILE_TYPE.put("SSISDeploymentManifest", "text/xml");

        // ppt
        FILE_TYPE.put("pot", "application/vnd.ms-powerpoint");
        FILE_TYPE.put("ppa", "application/vnd.ms-powerpoint");
        FILE_TYPE.put("pwz", "application/vnd.ms-powerpoint");
        FILE_TYPE.put("pps", "application/vnd.ms-powerpoint");
        FILE_TYPE.put("sldm", "application/vnd.ms-powerpoint.slide.macroEnabled.12");
        FILE_TYPE.put("ppam", "application/vnd.ms-powerpoint.addin.macroEnabled.12");
        FILE_TYPE.put("potm", "application/vnd.ms-powerpoint.template.macroEnabled.12");
        FILE_TYPE.put("ppsm", "application/vnd.ms-powerpoint.slideshow.macroEnabled.12");
        FILE_TYPE.put("pptm", "application/vnd.ms-powerpoint.presentation.macroEnabled.12");
        FILE_TYPE.put("potx", "application/vnd.openxmlformats-officedocument.presentationml.template");
        FILE_TYPE.put("ppsx", "application/vnd.openxmlformats-officedocument.presentationml.slideshow");

        // doc
        FILE_TYPE.put("wbk", "application/msword");
        FILE_TYPE.put("wiz", "application/msword");
        FILE_TYPE.put("dot", "application/msword");
        FILE_TYPE.put("docm", "application/vnd.ms-word.document.macroEnabled.12");
        FILE_TYPE.put("dotm", "application/vnd.ms-word.template.macroEnabled.12");
        FILE_TYPE.put("dotx", "application/vnd.openxmlformats-officedocument.wordprocessingml.template");

        // excel
        FILE_TYPE.put("xla", "application/vnd.ms-excel");
        FILE_TYPE.put("xlc", "application/vnd.ms-excel");
        FILE_TYPE.put("xld", "application/vnd.ms-excel");
        FILE_TYPE.put("xlk", "application/vnd.ms-excel");
        FILE_TYPE.put("xll", "application/vnd.ms-excel");
        FILE_TYPE.put("xls", "application/vnd.ms-excel");
        FILE_TYPE.put("xlt", "application/vnd.ms-excel");
        FILE_TYPE.put("xlw", "application/vnd.ms-excel");
        FILE_TYPE.put("slk", "application/vnd.ms-excel");
        FILE_TYPE.put("xlam", "application/vnd.ms-excel.addin.macroEnabled.12");
        FILE_TYPE.put("xlsm", "application/vnd.ms-excel.sheet.macroEnabled.12");
        FILE_TYPE.put("xltm", "application/vnd.ms-excel.template.macroEnabled.12");
        FILE_TYPE.put("xlsb", "application/vnd.ms-excel.sheet.binary.macroEnabled.12");
        FILE_TYPE.put("xltx", "application/vnd.openxmlformats-officedocument.spreadsheetml.template");

    }

    /**
     * 得到文件 contentType
     *
     * @param name 文件名称
     * @return contentType
     */
    public static String getContentType(String name) {
        String type = StrUtil.subAfter(name, StorageConstant.POINT);
        return FILE_TYPE.getOrDefault(type, STREAM_CONTENT_TYPE);
    }

}