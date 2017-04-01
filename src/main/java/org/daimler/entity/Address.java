package org.daimler.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "Address")
public class Address {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_seq")
    @SequenceGenerator(name = "address_seq", sequenceName = "address_seq", allocationSize = 1)
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

    @Column(name = "LANDMARK", length = 20)
    @NotNull
    @Size(max = 100)
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