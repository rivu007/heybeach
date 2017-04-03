package org.daimler.repository;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.net.URL;

import static com.amazonaws.services.s3.model.GroupGrantee.AllUsers;
import static com.amazonaws.services.s3.model.GroupGrantee.AuthenticatedUsers;
import static com.amazonaws.services.s3.model.Permission.FullControl;
import static com.amazonaws.services.s3.model.Permission.Read;

@Repository
public class S3MediaRepository {

    private static final AccessControlList ACCESS_CONTROL_LIST;

    static {
        ACCESS_CONTROL_LIST = new AccessControlList();
        ACCESS_CONTROL_LIST.grantPermission(AllUsers, Read);
        ACCESS_CONTROL_LIST.grantPermission(AuthenticatedUsers, FullControl);
    }

    private final AmazonS3Client amazonS3Client;
    private final String bucket;

    @Autowired
    public S3MediaRepository(AmazonS3Client amazonS3Client, @Value("${amazon.s3.bucket}") String bucket) {
        this.amazonS3Client = amazonS3Client;
        this.bucket = bucket;
    }

    public URL uploadPhoto(InputStream inputStream, String fileKey) throws InterruptedException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        return upload(inputStream, fileKey, objectMetadata);
    }

    private URL upload(InputStream inputStream, String fileKey, ObjectMetadata objectMetadata) throws InterruptedException {
        PutObjectRequest putObject = new PutObjectRequest(bucket, fileKey, inputStream, objectMetadata)
                .withAccessControlList(ACCESS_CONTROL_LIST);

        amazonS3Client.putObject(putObject);
        return amazonS3Client.getUrl(bucket, fileKey);
    }
}
