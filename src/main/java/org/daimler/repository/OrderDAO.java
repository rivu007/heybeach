package org.daimler.repository;

import org.daimler.entity.order.Order;
import org.daimler.entity.user.Role;
import org.daimler.entity.user.RoleName;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;


/**
 * DAO to manage the user {@link Order}.
 *
 * @author Abhilash Ghosh
 */
@Repository
public class OrderDAO extends AbstractDAO<Order> {

    public OrderDAO(){
        setClazz(Order.class);
    }
}
