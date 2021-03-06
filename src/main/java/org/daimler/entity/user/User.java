package org.daimler.entity.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Defines 3 types of actors/users : Admin, Seller and Buyer
 *
 * @author Abhilash Ghosh
 */
@Entity
@Data
@Table(name = "USER")
@EqualsAndHashCode(exclude = {"createdAt","lastUpdated"})
public class User implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "USERNAME", length = 20, unique = true)
    @NotNull
    @Size(min = 4, max = 20)
    private String username;

    @Column(name = "PASSWORD", length = 100)
    @NotNull
    @Size(min = 4, max = 100)
    private String password;

    @Column(name = "FIRST_NAME", length = 30)
    @NotNull
    @Size(min = 4, max = 30)
    private String firstname;

    @Column(name = "LAST_NAME", length = 30)
    @NotNull
    @Size(min = 4, max = 30)
    private String lastname;

    @Column(name = "EMAIL_ADDRESS", length = 30)
    @NotNull
    private String emailAddress;

    @Column(name = "ENABLED")
    @NotNull
    private Boolean enabled;

    @Column(name = "LASTPASSWORDRESETDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPasswordResetDate;

    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "LAST_UPDATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "USER_ROLE",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")})
    private Set<Role> roles;

    @ManyToMany()
    @JoinTable(
            name = "USER_ADDRESS",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "ADDRESS_ID", referencedColumnName = "ID")})
    private Set<Address> addresses;
}