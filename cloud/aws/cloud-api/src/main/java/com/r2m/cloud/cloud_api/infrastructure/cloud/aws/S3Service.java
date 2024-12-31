package com.r2m.cloud.cloud_api.infrastructure.cloud.aws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Uri;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.CompletedFileUpload;
import software.amazon.awssdk.transfer.s3.model.FileUpload;
import software.amazon.awssdk.transfer.s3.model.UploadFileRequest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class S3Service {

    private final S3Client client;

    private final S3AsyncClient asyncClient;

    private final S3TransferManager transferManager;

    public S3Service() {
        this.client = S3Client.builder()
                .region(Region.US_EAST_1)
                .forcePathStyle(true)
                .build();
        this.asyncClient = S3AsyncClient.crtBuilder()
                .region(Region.US_EAST_1)
                .forcePathStyle(true)
                .minimumPartSizeInBytes((long) 5 * 1024 * 1024)
                .build();
        this.transferManager = S3TransferManager.builder()
                .s3Client(asyncClient)
                .build();
    }

    public List<Bucket> getBuckets() {
        return client.listBuckets().buckets();
    }

    /**
     * Upload file with putObject
     * The file upload should be used for less than 100 MB files
     *
     * @param bucket
     * @param fileKey
     * @param file
     */
    public void uploadFile(String bucket, String fileKey, File file) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileKey)
                .build();

        RequestBody body = RequestBody.fromFile(file);
        client.putObject(objectRequest, body);
    }

    /**
     * Upload file with multi part strategy
     * Transfer method for large files up to 5 TB
     *
     * @param bucket
     * @param fileKey
     * @param file
     * @throws IOException
     */
    public void uploadMultipartFile(String bucket, String fileKey, MultipartFile file) {
        // initiate upload
        CreateMultipartUploadResponse createResponse = startMultipartUpload(bucket, fileKey);

        try {
            // upload part
            List<CompletedPart> completedParts = uploadFileParts(bucket, fileKey, file, createResponse);

            // complete upload
            completeMultipartUpload(bucket, fileKey, completedParts, createResponse);
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    /**
     * Upload file with Transfer manager API
     *
     * @param bucket
     * @param fileKey
     * @param file
     * @return
     */
    public String transferFile(String bucket, String fileKey, File file) {
        UploadFileRequest uploadFileRequest = UploadFileRequest.builder()
                .putObjectRequest(b -> b.bucket(bucket).key(fileKey))
                .source(file)
                .build();

        FileUpload fileUpload = transferManager.uploadFile(uploadFileRequest);

        CompletedFileUpload uploadResult = fileUpload.completionFuture().join();
        return uploadResult.response().eTag();
    }

    public String getPresignedObjectUrl(String bucket, String fileKey) {
        try (S3Presigner presigner = S3Presigner.builder().s3Client(client).build()) {
            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileKey)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .getObjectRequest(objectRequest)
                    .signatureDuration(Duration.ofMinutes(5))
                    .build();
            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
            log.info("Presigned URL: [{}]", presignedRequest.url().toString());
            log.info("HTTP method: [{}]", presignedRequest.httpRequest().method());

            return presignedRequest.url().toExternalForm();
        }
    }

    public S3Uri getS3Uri(String uri) {
        return client.utilities().parseUri(URI.create(uri));
    }

    /**
     * Upload file to S3 with GLACIER_IR
     */
    public void archiveFile(){

        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Restore file from Glacier to S3 for given duration
     */
    public void restoreArchivedFile(Duration timeOut){
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Pool to get restored object or timeout and throw error
     * @param poolTimeOut
     * @param timeOut
     */
    public void getArchiveFile(Duration poolTimeOut, Duration timeOut){
        throw new UnsupportedOperationException("Not implemented");
    }

    private void completeMultipartUpload(String bucket, String fileKey, List<CompletedPart> completedParts, CreateMultipartUploadResponse createResponse) {
        CompletedMultipartUpload completeUpload = CompletedMultipartUpload.builder()
                .parts(completedParts)
                .build();
        CompleteMultipartUploadRequest completeRequest = CompleteMultipartUploadRequest.builder()
                .bucket(bucket)
                .key(fileKey)
                .uploadId(createResponse.uploadId())
                .multipartUpload(completeUpload)
                .build();
        client.completeMultipartUpload(completeRequest);
    }

    private List<CompletedPart> uploadFileParts(String bucket, String fileKey, MultipartFile file, CreateMultipartUploadResponse createResponse) throws IOException {
        InputStream fileInputStream = file.getInputStream();
        List<CompletedPart> completedParts = new ArrayList<>();

        for (int partNumber = 1, position = 0; position < file.getSize(); partNumber++) {
            byte[] buffer = new byte[5 * 1024 * 1024];
            int bytesRead = fileInputStream.read(buffer);

            UploadPartRequest uploadPartRequest = UploadPartRequest.builder()
                    .bucket(bucket)
                    .key(fileKey)
                    .uploadId(createResponse.uploadId())
                    .partNumber(partNumber)
                    .contentLength((long) bytesRead)
                    .build();
            UploadPartResponse response = client.uploadPart(uploadPartRequest, RequestBody.fromBytes(buffer));
            completedParts.add(CompletedPart.builder()
                    .partNumber(partNumber)
                    .eTag(response.eTag())
                    .build());


            position += bytesRead;
        }
        return completedParts;
    }

    private CreateMultipartUploadResponse startMultipartUpload(String bucket, String fileKey) {
        CreateMultipartUploadRequest createMultipartUploadRequest = CreateMultipartUploadRequest.builder()
                .bucket(bucket)
                .key(fileKey)
                .build();
        return client.createMultipartUpload(createMultipartUploadRequest);
    }
}
