package com.joanna.staybooking.config;

import com.google.auth.Credentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GoogleCloudStorageConfig {   //establish connection with GCS

    @Bean
    public Storage storage() throws IOException { //通过credentials 做为验证gce access 身份认证的文件,如果没有credential gcs不允许上传文件
        Credentials credentials = ServiceAccountCredentials.fromStream(getClass().getClassLoader().getResourceAsStream("credentials.json"));
        // 只要文件放在resources folder下面，都可以用下面的code 去load文件 （相对路径）
        //getClass().getClassLoader().getResourceAsStream();
        return StorageOptions.newBuilder().setCredentials(credentials).build().getService();

    }
}
