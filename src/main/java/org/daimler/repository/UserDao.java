package org.daimler.repository;

import org.daimler.entity.user.User;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * DAO to manage the {@link User}.
 *
 * @author Abhilash Ghosh
 */
@Repository
@Transactional
public class UserDAO extends AbstractDAO<User> {

    public UserDAO(){
        setClazz(User.class);
    }

    public Optional<User> findByUsername(String username){
        Query query = getCurrentSession()
                        .createQuery( "from User u where u.username =:username");
        query.setParameter("username", username);
        return Optional.ofNullable((User) query.uniqueResult());
    }

    public void deleteAll(){
        List<User> users = findAll();
        for(User user : users) {
            delete(user);
        }
    }
}
