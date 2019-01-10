package com.schwarzsword.pip.coursework.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

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
    @Basic
    @Column(name = "password", nullable = false, length = 100)
    private String password;
    @Basic
    @Column(name = "mail", nullable = false)
    private String mail;
    @Basic
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;
    @OneToMany(mappedBy = "usersByExpert")
    private Collection<CertificateEntity> certificatesById;
    @OneToMany(mappedBy = "usersByCustomer")
    private Collection<DealEntity> dealsById;
    @OneToMany(mappedBy = "usersBySeller")
    private Collection<LotEntity> lotsById;
    @OneToMany(mappedBy = "usersByLastBet")
    private Collection<LotEntity> lotsByLastBet;
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

    public UsersEntity(String name, String surname, String username, String password, String mail, String phone) {
        this.name = name;
        this.surname = surname;
        this.mail = mail;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.roles.add(new RolesEntity("USER"));
    }

//    @ElementCollection(targetClass = Role.class, fetch = FetchType.LAZY)
//    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = ))
//    private List<Role> roles;
}
