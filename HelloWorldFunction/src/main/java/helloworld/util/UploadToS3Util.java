package helloworld.util;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class UploadToS3Util {

    private static final String BUCKET_NAME = "bucket-example";
    private static final String BUCKET_FOLDER_NAME = "test";
    private static final String S3_ENDPOINT_URL = "http://s3.localhost.localstack.cloud:4566";
    private static final int URL_EXPIRATION_TIME = 15 * 60; // download URL expires in 15 minutes
    private static final String REGION = "us-east-1";
    private static final String ACCESS_KEY = "test";
    private static final String SECRET_KEY = "test";

    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(getCredentialsProvider())
                .withEndpointConfiguration(getEndpointConfiguration(S3_ENDPOINT_URL))
                .build();
    }

    private AwsClientBuilder.EndpointConfiguration getEndpointConfiguration(String url) {
        return new AwsClientBuilder.EndpointConfiguration(url, REGION);
    }

    private AWSStaticCredentialsProvider getCredentialsProvider() {
        return new AWSStaticCredentialsProvider(getBasicAWSCredentials());
    }

    private BasicAWSCredentials getBasicAWSCredentials() {
        return new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
    }

    public URL uploadToS3(ByteArrayOutputStream arrayOutputStream, String filename){

        AmazonS3 s3Client = amazonS3();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(arrayOutputStream.size());

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = dateFormat.format(new Date());
        String S3_OBJECT_KEY = String.format("%s/%s_%s.txt", BUCKET_FOLDER_NAME, timestamp, filename);

        try (InputStream inputStream = new ByteArrayInputStream(arrayOutputStream.toByteArray())) {
            PutObjectRequest putRequest = new PutObjectRequest(BUCKET_NAME, S3_OBJECT_KEY, inputStream, metadata);
            s3Client.putObject(putRequest);
        } catch (IOException ex) {
            // Handle the exception
        }

        // Generate a pre-signed URL for the S3 file
        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(BUCKET_NAME, S3_OBJECT_KEY);
        urlRequest.setMethod(HttpMethod.GET);
        urlRequest.setExpiration(Date.from(Instant.now().plusSeconds(URL_EXPIRATION_TIME)));
        URL preSignedUrl = s3Client.generatePresignedUrl(urlRequest);

        return preSignedUrl;
    }
}