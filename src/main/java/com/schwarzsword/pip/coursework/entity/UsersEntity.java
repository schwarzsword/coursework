package com.schwarzsword.pip.coursework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Data
@Entity
@Table(name = "users", schema = "public", catalog = "s243884")
public class UsersEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Basic
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @Basic
    @Column(name = "surname", nullable = false, length = 50)
    private String surname;
    @Basic
    @Column(name = "username", nullable = false, length = 30)
    private String username;
    @JsonIgnore
    @Basic
    @Column(name = "password", nullable = false, length = 100)
    private String password;
    @Basic
    @JsonIgnore
    @Column(name = "mail", nullable = false)
    private String mail;

    @JsonIgnore
    @OneToMany(mappedBy = "usersByExpert")
    private Collection<CertificateEntity> certificatesById;
    @JsonIgnore
    @OneToMany(mappedBy = "usersByCustomer")
    private Collection<DealEntity> dealsById;
    @JsonIgnore
    @OneToMany(mappedBy = "usersBySeller")
    private Collection<LotEntity> lotsById;
    @JsonIgnore
    @OneToMany(mappedBy = "usersByLastBet")
    private Collection<LotEntity> lotsByLastBet;
    @JsonIgnore
    @OneToOne(mappedBy = "usersByOwner")
    private WalletEntity walletById;
    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "username", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role", referencedColumnName = "id"))
    private Collection<RolesEntity> roles;

    protected UsersEntity() {
    }

    public UsersEntity(String name, String surname, String password, String mail, RolesEntity role) {
        this.name = name;
        this.surname = surname;
        this.mail = mail;
        this.username = mail;
        this.password = password;
        ArrayList<RolesEntity> list = new ArrayList<>();
        list.add(role);
        this.roles = list;
    }

    public void addRole(RolesEntity role) {
        this.roles.add(role);
    }

    public void removeRole(RolesEntity role) {
        this.roles.remove(role);
    }

}
