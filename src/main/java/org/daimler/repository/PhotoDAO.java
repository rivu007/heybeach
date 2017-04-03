package org.daimler.repository;

import org.daimler.entity.picture.Photo;
import org.daimler.entity.user.User;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * DAO to manage the {@link org.daimler.entity.picture.Photo}.
 *
 * @author Abhilash Ghosh
 */
@Repository
@Transactional
public class PhotoDAO extends AbstractDAO<Photo> {

    public PhotoDAO(){
        setClazz(Photo.class);
    }

}
