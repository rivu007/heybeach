package org.daimler.controller;

import org.daimler.entity.order.Order;
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
 * Rest controller for managing Picture Photo.
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
                                         @RequestParam("pictureId") String pictureId,
                                         @RequestParam("userId") String userId)
            throws MediaUploadException, URISyntaxException {
        URL location = mediaService.upload(file, pictureId, userId);

        return ResponseEntity
                .created(location.toURI())
                .body(location.toString());
    }

    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @RequestMapping(method = RequestMethod.POST, path = "/hashtag")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void saveHashtag(@RequestBody Order order) throws EntityPersistenceException {
        //TODO: implement the logic
    }
}
