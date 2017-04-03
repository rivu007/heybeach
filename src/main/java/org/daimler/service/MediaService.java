package org.daimler.service;

import org.daimler.error.MediaUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

/**
 * @author abhilash.ghosh
 */
public interface MediaService {

    /**
     * Stores the Picture within Amazon S3 bucket
     *
     * @param multipartFile file to store
     * @param pictureId     id of the content related to the media
     * @param userId      the name of the file to be uploaded
     * @return a path to the stored resource
     * @throws MediaUploadException when error occurred during the file upload
     */
    URL upload(MultipartFile multipartFile, String pictureId, String userId) throws MediaUploadException;
}
