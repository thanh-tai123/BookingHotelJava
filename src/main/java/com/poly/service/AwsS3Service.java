package com.poly.service;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.poly.exception.OurException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class AwsS3Service {

  
    private final String bucketName = "hotel-img-asm";

    @Value("${aws.s3.access.key}")
    private String awsS3AccessKey;

    @Value("${aws.s3.secret.key}")
    private String awsS3SecretKey;

    public String saveImageToS3(MultipartFile img) {
        String s3LocationImage = null;

        try {

            String s3Filename = img.getOriginalFilename();

            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsS3AccessKey, awsS3SecretKey);
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion("ap-southeast-2")
                    .build();

            InputStream inputStream = img.getInputStream();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/jpeg");

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3Filename, inputStream, metadata);
            s3Client.putObject(putObjectRequest);
            return "https://" + bucketName + ".s3.amazonaws.com/" + s3Filename;

        } catch (Exception e) {
            e.printStackTrace();
            throw new OurException("Unable to upload image to s3 bucket" + e.getMessage());
        }
    }

    // Phương thức xóa hình ảnh từ S3
    public void deleteImageFromS3(String imageUrl) {
        try {
            String s3Filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsS3AccessKey, awsS3SecretKey);
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion("ap-southeast-2")
                    .build();

            s3Client.deleteObject(new DeleteObjectRequest(bucketName, s3Filename));
        } catch (Exception e) {
            e.printStackTrace();
            throw new OurException("Unable to delete image from s3 bucket: " + e.getMessage());
        }
    }

}

















