package org.daimler.entity.picture;

import lombok.Data;

/**
 * Core entity of picture management which stores the picture(s)
 * that the {@link org.daimler.entity.user.RoleName#ROLE_SELLER} uploads.
 *
 * @author Abhilash Ghosh
 */

@Data
public class Photo {

    private int id;
    private String title;
    private String caption;
    private String filePath;
    private int fileSize;
    private int width;
    private int height;
    private boolean approved;
    private String[] tags;
    private int[] likedBy;
}
