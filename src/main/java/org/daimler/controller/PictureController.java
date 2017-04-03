package org.daimler.controller;

import org.daimler.entity.picture.Photo;
import org.daimler.error.EntityPersistenceException;
import org.daimler.error.MediaUploadException;
import org.daimler.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;
import java.net.URL;

import static org.springframework.web.bind.annotation.RequestMethod.POST;


/**
 * Rest controller for Media operations.
 */
@RestController
@RequestMapping("/pictures")
public class PictureController {

    @Autowired
    private MediaService mediaService;

    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @RequestMapping(method = POST, path = "/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file,
                                         @RequestBody Photo photo)
            throws MediaUploadException, URISyntaxException, EntityPersistenceException {
        URL filePath = mediaService.upload(file, photo.getId(), photo.getUserId());
        mediaService.save(photo);

        return ResponseEntity
                .created(filePath.toURI())
                .body(filePath.toString());
    }

    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @RequestMapping(method = RequestMethod.POST, path = "/hashtag")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void saveHashtag(@RequestBody Photo photo) throws EntityPersistenceException {
        //TODO: implement the logic
    }
}
