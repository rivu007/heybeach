package org.daimler.entity.order;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

/**
 * Related to item(s)/photo(s) ordered
 *
 * @author Abhilash Ghosh
 */
@Entity
@Data
@Table(name = "ORDER_ITEM")
public class OrderItem {

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ORDER_ID")
    private Order orderId;

    @Column(name = "PHOTO_ID")
    @NotNull
    private Long photoId;

    @Column(name = "QUANTITY", nullable = false)
    @NotNull
    private Integer quantity;

    @Column(name = "COMMENTS")
    private String comments;

    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "LAST_UPDATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "ORDER_ITEM_SHIPMENT",
            joinColumns = {@JoinColumn(name = "ORDER_ITEM_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "SHIPMENT_ID", referencedColumnName = "ID")})
    private Set<Shipment> shipments;

}
