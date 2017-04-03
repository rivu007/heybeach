package org.daimler.service.impl;

import org.daimler.entity.picture.Photo;
import org.daimler.error.EntityPersistenceException;
import org.daimler.error.MediaUploadException;
import org.daimler.repository.PhotoDAO;
import org.daimler.repository.S3MediaRepository;
import org.daimler.service.MediaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author abhilash.ghosh
 */
@Service
public class MediaServiceImpl implements MediaService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private PhotoDAO photoDAO;

    private S3MediaRepository mediaRepository;

    @Autowired
    public MediaServiceImpl(S3MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    /**
     * Stores the Picture within Amazon S3 bucket
     *
     * @param multipartFile file to store
     * @param pictureId     id of the content related to the media
     * @param userId      the name of the file to be uploaded
     * @return a path to the stored resource
     * @throws MediaUploadException when error occurred during the file upload
     */
    @Override
    public URL upload(MultipartFile multipartFile, Integer pictureId, Integer userId) throws MediaUploadException {
        if (multipartFile.isEmpty()) {
            throw new MediaUploadException("File is empty");
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            return mediaRepository.uploadPhoto(inputStream, pictureId.toString());

        } catch (IOException | InterruptedException e) {
            log.error("Picture upload failed.", e);
                throw new MediaUploadException(String.format("Photo for and UserId='%s' is not uploaded", userId), e);
        }
    }

    /**
     * Stores the reference of the photo
     *
     * @param photo to save
     * @return persisted photo object
     * @throws EntityPersistenceException
     */
    @Override
    public Photo save(Photo photo) throws EntityPersistenceException {
        return photoDAO.save(photo);
    }
}
