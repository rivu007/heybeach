package org.daimler.service;

import org.daimler.entity.picture.Photo;
import org.daimler.error.EntityPersistenceException;
import org.daimler.error.MediaUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.Set;

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
    URL upload(MultipartFile multipartFile, Integer pictureId, Integer userId) throws MediaUploadException;

    /**
     * Stores the reference of the photo
     *
     * @param photo to save
     * @return persisted photo object
     * @throws EntityPersistenceException
     */
    Photo save(Photo photo) throws EntityPersistenceException;

    /**
     * Stores the reference of the photo
     *
     * @param photoId to be persisted as the key in the cache
     * @param tags list to save
     * @throws EntityPersistenceException
     */
    void saveHashTags(int photoId, Set<String> tags) throws EntityPersistenceException;
}
