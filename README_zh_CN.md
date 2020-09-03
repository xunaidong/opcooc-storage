<p align="center">
  <a href="https://github.com/opcooc/opcooc-storage">
   <img alt="opcooc-storage-logo" src="https://gitee.com/opcooc/opcooc-storage/raw/master/doc/img/opcooc-storage.png">
  </a>
</p>

<p align="center">
  <strong>只是为了简化文件存储</strong>
</p>

<p align="center">
	<a target="_blank" href="https://search.maven.org/search?q=g:com.opcooc%20AND%20a:opcooc-storage">
		<img alt='maven' src="https://img.shields.io/maven-central/v/com.opcooc/opcooc-storage" />
	</a>
	<a target="_blank" href="https://www.apache.org/licenses/LICENSE-2.0.html">
		<img alt='license' src="https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=square" />
	</a>
	<a target="_blank" href="https://www.oracle.com/technetwork/java/javase/downloads/index.html">
		<img alt='JDK' src="https://img.shields.io/badge/JDK-1.8+-green.svg" />
	</a>
	<a target="_blank" href="https://github.com/opcooc/opcooc-storage/wiki" title="参考文档">
		<img alt='Docs' src="https://img.shields.io/badge/Docs-latest-blueviolet.svg" />
	</a>
	<a target="_blank" href='https://gitee.com/opcooc/opcooc-storage/stargazers'>
	  <img alt='gitee star' src='https://gitee.com/opcooc/opcooc-storage/badge/star.svg?theme=white'/>
	</a>
	<a target="_blank" href='https://github.com/opcooc/opcooc-storage'>
		<img alt="github star" src="https://img.shields.io/github/stars/opcooc/opcooc-storage?style=social"/>
	</a>
	<a target="_blank" href='https://gitter.im/opcooc/opcooc-storage?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge'>
		<img alt="gitter" src="https://img.shields.io/gitter/room/opcooc/opcooc-storage"/>
	</a>

</p>

<div style="text-align: center;">
    <table>
        <tr>
            <td align="center" width="200"><img src="https://gitee.com/opcooc/opcooc-storage/raw/master/doc/img/aws-s3.png" width="36" alt="aws-s3"/></td>
            <td align="center" width="200"><img src="https://gitee.com/opcooc/opcooc-storage/raw/master/doc/img/tencent-cos.png" width="70" alt="tencent-cos"/></td>
            <td align="center" width="200"><img src="https://gitee.com/opcooc/opcooc-storage/raw/master/doc/img/minio.png" width="70" alt="minio"/></td>
            <td align="center" width="200"><img src="https://gitee.com/opcooc/opcooc-storage/raw/master/doc/img/aliyun-oss.png" width="70" alt="aliyun-oss"/></td>
            <td align="center" width="200"><img src="https://gitee.com/opcooc/opcooc-storage/raw/master/doc/img/qiliu.png" width="70" alt="qiliu"/></td>
        </tr>
    </table>
</div>

-------------------------------------------------------------------------------

- QQ交流群 `789585778`，可获取各项目详细图文文档、疑问解答
[![](http://pub.idqqimg.com/wpa/images/group.png)](https://jq.qq.com/?_wv=1027&k=iRannIfW)

## 什么是 opcooc-storage?

opcooc-storage 将 AWS S3 协议进行了封装和扩展

[![CN doc](https://img.shields.io/badge/文档-中文版-blue.svg)](README_zh_CN.md)
[![EN doc](https://img.shields.io/badge/document-English-blue.svg)](README.md)

## 快速开始

-   引用依赖
    -   Maven:
        ```xml
            <dependency>
              <groupId>com.opcooc</groupId>
              <artifactId>opcooc-storage-boot-starter</artifactId>
              <version>1.1.0</version>
            </dependency>
        ```
    -   Gradle
        ```groovy
        compile group: 'com.opcooc', name: 'opcooc-storage-boot-starter', version: '1.1.0'
        ```

-   添加配置，在 `application.yml` 中添加配置配置信息

    ```yaml
        opcooc:
          enabled: true
          default-client: MINIO
          client-source:
            MINIO:
              bucket-name: opcooc
              end-point: http://xxx:9000
              access-key: xxx
              secret-key: xxx
            OSS:
              bucket-name: opcooc
              end-point: https://oss-cn-shanghai.aliyuncs.com
              access-key: xxx
              secret-key: xxx
    ```

-   然后就开始玩耍吧~
    ```java
        @RestController
        @RequiredArgsConstructor(onConstructor_ = @Autowired)
        @RequestMapping(value = "/client", produces = {APPLICATION_JSON_VALUE})
        public class ClientController {
        
            private final StorageClient storageClient;
        
            @GetMapping(value = "create-folder")
            public Response createFolder(@RequestParam String source, @RequestParam String folder) {
                storageClient.op(source).createFolder(folder);
                return Response.success();
            }
        
        }
    ```

> 该展示只是 opcooc-storage 功能的一小部分。如果您想了解更多信息，请参阅 [documentation](http://storage.opcooc.com).

## License

opcooc-storage is under the Apache 2.0 license. See the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0) file for details.
