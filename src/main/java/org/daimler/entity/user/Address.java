package org.daimler.entity.user;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * Relates to user's address. Typically a user can have more than one addresses
 *
 * @author Abhilash Ghosh
 */
@Entity
@Data
@Table(name = "Address")
public class Address {

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "STREET_NAME", length = 20)
    @NotNull
    @Size(min = 4, max = 20)
    private String streetName;

    @Column(name = "STREET_NUMBER", length = 20)
    @NotNull
    @Size(max = 20)
    private Integer streetNumber;

    @Column(name = "CITY", length = 20)
    @NotNull
    @Size(max = 20)
    private String city;

    @Column(name = "COUNTRY", length = 20)
    @NotNull
    @Size(max = 20)
    private String country;

    @Column(name = "LANDMARK")
    private String landmark;

    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "LAST_UPDATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @ManyToMany(mappedBy = "addresses", fetch = FetchType.LAZY)
    private List<User> users;
}