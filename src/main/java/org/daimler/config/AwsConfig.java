package org.daimler.config;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configs to initialize AWS Client
 *
 * @author Abhilash Ghosh
 */
@Configuration
public class AwsConfig {

    @Bean
    public BasicAWSCredentials basicAWSCredentials(String accessKeyId, String secretKeyId) {
        return new BasicAWSCredentials(accessKeyId, secretKeyId);
    }

    @Bean
    public AmazonS3Client amazonS3Client(@Value("${amazon.region}") String amazonAWSRegion,
                                         @Value("${amazon.access_key_id}") String accessKeyId,
                                         @Value("${amazon.secret_key_id}") String secretKeyId) {
        AmazonS3Client amazonS3Client = new AmazonS3Client(basicAWSCredentials(accessKeyId, secretKeyId));
        amazonS3Client.setRegion(Region.getRegion(Regions.fromName(amazonAWSRegion)));
        return amazonS3Client;
    }
}
