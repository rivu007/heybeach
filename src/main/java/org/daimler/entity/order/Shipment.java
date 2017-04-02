package org.daimler.entity.order;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

/**
 * Relates to order shipment
 *
 * @author Abhilash Ghosh
 *
 */
@Entity
@Data
@Table(name = "SHIPMENT")
public class Shipment {

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "TRACKING_NUMBER", nullable = false, unique = true)
    @NotNull
    private Long trackingNumber;

    @Column(name = "SHIPMENT_DATE")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date shipmentDate;

    @Column(name = "COMMENTS")
    private String comments;

    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "LAST_UPDATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @ManyToMany(mappedBy = "shipments", fetch = FetchType.LAZY)
    private Set<OrderItem> orderItems;
}
