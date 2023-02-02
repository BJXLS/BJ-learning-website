package com.xuecheng.media;

import io.minio.*;
import io.minio.errors.MinioException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Hao Ge
 * @version 1.0
 * @description MinIO 测试类
 * @date 2023/2/2 12:05
 */
public class MinIOTest {

    static MinioClient minioClient = MinioClient.builder()
                                    .endpoint("http://121.36.65.129:9000")
                                    .credentials("minio", "minioadmin")
                                    .build();

    @Test
    public void testUpload() {
        try {
            // 先查看我们要的桶有没有，没有就创建
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket("mediafiles").build());
            if (!found) {
                // Make a new bucket called 'mediafiles'.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket("mediafiles").build());
            } else {
                System.out.println("Bucket 'mediafiles' already exists.");
            }

            // 上传
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket("mediafiles")
                            .object("r2e391.png")
                            .filename("C:\\Users\\Hao Ge\\OneDrive\\图片\\r2e391.png")
                            .build());
            System.out.println(
                    "'C:\\Users\\Hao Ge\\OneDrive\\图片\\r2e391.png' is successfully uploaded as "
                            + "object 'r2e391.png' to bucket 'mediafiles'.");
        } catch (Exception e) {
            System.out.println("e = " + e);
        }
    }

    @Test
    public void testDelete() {
        String bucket = "mediafiles";
        String filepath = "r2e391.png";
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(filepath).build());
            System.out.println("删除成功");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
