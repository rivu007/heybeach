package org.daimler.entity.order;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

/**
 * Core entity of order management which stores the potential picture(s) that the user is about to purchase.
 *
 * @author Abhilash Ghosh
 */
@Entity
@Data
@Table(name = "ORDER")
public class Order {

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "TOTAL_ORDER_PRICE", precision=10, scale=2)
    @NotNull
    private Double totalOrderPrice;

    @Column(name = "PAID_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paidDate;

    @Column(name = "ORDER_STATUS")
    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "TRANSACTION_ID", unique = true, nullable = false)
    @NotNull
    private String transactionId;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "LAST_UPDATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @OneToMany(mappedBy="orderId")
    private Set<OrderItem> orderItems;
}
