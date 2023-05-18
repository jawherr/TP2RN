package com.neo.exp.service;

import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import javax.inject.Singleton;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.*;
import java.util.logging.Logger;

@Singleton
public class StorageService {
    @ConfigProperty(name="s3client.accesskey",defaultValue = "")
    String accesskey;
    @ConfigProperty(name="s3client.secretkey",defaultValue = "")
    String secretkey;
    String s3url="http://192.168.1.15:9000";

    S3Client s3Client = S3Client.builder()
            .endpointOverride(URI.create(s3url))
            .region(Region.US_WEST_2)
            .credentialsProvider(() -> AwsBasicCredentials.create(accesskey, secretkey ))
            .build();
    // Generate a pre-signed URL with a validity of 1 hour
    S3Presigner presigner = S3Presigner.builder()
            .endpointOverride(URI.create(s3url))
            .region(Region.US_WEST_2)
            .credentialsProvider(() -> AwsBasicCredentials.create(accesskey, secretkey))
            .build();
    //uploadfile fucntion
    public Response uploadFile(String bucketName, InputStream inputStream) throws IOException {
        byte[] bytes = IOUtils.toByteArray(inputStream);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        String uuid = UUID.randomUUID().toString();
        String fileExtension = getFileExtension(byteArrayInputStream);
        String key = uuid + "." + fileExtension;

        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", getContentType(fileExtension));

        byteArrayInputStream.reset();
        byte[] byteArray = IOUtils.toByteArray(byteArrayInputStream);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .metadata(metadata)
                .contentLength((long) byteArray.length)
                .build();

        RequestBody requestBody = RequestBody.fromBytes(byteArray);
        s3Client.putObject(putObjectRequest, requestBody);

        return Response.ok("{\"url\": \"https://" + bucketName + ".s3-us-west-2.amazonaws.com/" + key + "\"}").build();
    }

    private String getFileExtension(InputStream file) throws IOException {
        Tika tika = new Tika();
        String mimeType = tika.detect(file);
        if (mimeType == null) {
            throw new IOException("Unsupported file format");
        }
        return mimeType.split("/")[1];
    }

    private String getContentType(String fileExtension) {
        return switch (fileExtension) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "pdf" -> "application/pdf";
            default -> "application/octet-stream";
        };
    }
    //Method tested and works
    public String CreateBucket(String bucketName) {
        if (s3Client.listBuckets().buckets().stream().anyMatch(b -> b.name().equals(bucketName))) {
            return bucketName+" Exist";
        } else {
            // Create a new S3 bucket
            s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
            return bucketName+" Created";
        }
    }
    //Method tested and works

    public String DeleteBucket(String bucketName) {
        if (s3Client.listBuckets().buckets().stream().noneMatch(b -> b.name().equals(bucketName))) {
            return bucketName+" does not exists";
        } else {
            // Create a new S3 bucket
            s3Client.deleteBucket(DeleteBucketRequest.builder().bucket(bucketName).build());
            return bucketName+" Deleted";
        }
    }
    //Method tested and Works
    public List<String> Buckets() {
        List<String> Buckets= new ArrayList<>();
        // List all the buckets in the account
        ListBucketsResponse listBucketsResponse = s3Client.listBuckets();
        for (Bucket bucket : listBucketsResponse.buckets()) {
            Logger logger = Logger.getLogger("Bucket info");
            logger.info(bucket.name());
            Buckets.add(bucket.name());
        }
        return Buckets;
    }
    public Response deleteFile(String bucket,String filename) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(filename)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
            return Response.ok().build();
        } catch (S3Exception e) {
            return Response.status(e.statusCode()).entity(e.getMessage()).build();
        }
    }

    public Response Viewfile(
            String bucket,
            String filename) {

        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(filename)
                    .build();
            ResponseBytes<GetObjectResponse> responseBytes = s3Client.getObjectAsBytes(getObjectRequest);
            return Response.ok(responseBytes.asInputStream())
                    .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                    .build();
        } catch (S3Exception e) {
            return Response.status(e.statusCode()).entity(e.getMessage()).build();
        }
    }

    public Response getFileLink(
            String bucket,
            String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofHours(1))
                .getObjectRequest(getObjectRequest)
                .build();
        PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(getObjectPresignRequest);

        // Return the pre-signed URL in a JSON response
        JsonObject responseJson = Json.createObjectBuilder()
                .add("url", presignedGetObjectRequest.url().toString())
                .build();
        return Response.ok(responseJson).build();
    }

}
