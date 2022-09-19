package com.joanna.staybooking.service;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.joanna.staybooking.exception.GCSUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@Service
public class ImageStorageService {
    @Value("${gcs.bucket}")   //特殊的dependency injection
    private String bucketName; //gcs bucket name is globally unique

    private Storage storage;

    @Autowired
    public ImageStorageService(Storage storage) {
        this.storage = storage;
    }

    //https://cloud.google.com/storage/docs/reference/libraries#client-libraries-usage-java     Using the client library
    //上传文件的method. 返回String，是返回的存储的文件的URL，这个URL会存到database里
    public String save(MultipartFile file) throws GCSUploadException {  //file: request body里传进来的文件
        String filename = UUID.randomUUID().toString();  //UUID library: 自动生成globally unique identifier
        BlobInfo blobInfo = null;
        try {
            blobInfo = storage.createFrom(
                    BlobInfo
                            .newBuilder(bucketName, filename)
                            .setContentType("image/jpeg")
                            //Acl:access control list  把所有的user都set了READER的权限。因为如果不设置，能访问的只能是自己的java文件和自己，前端程序访问不了
                            .setAcl(new ArrayList<>(Arrays.asList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))))
                            .build(),
                    file.getInputStream());
        } catch (IOException exception) {
            throw new GCSUploadException("Failed to upload file to GCS");
        }

        return blobInfo.getMediaLink();

    }
}
