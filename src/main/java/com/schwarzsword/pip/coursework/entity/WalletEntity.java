package com.schwarzsword.pip.coursework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
@Table(name = "wallet", schema = "public", catalog = "s243884")
public class WalletEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Basic
    @Column(name = "balance", nullable = false)
    private Long balance;
    @JsonIgnore
    @OneToMany(mappedBy = "walletBySource")
    private Collection<PaymentEntity> paymentsByCustomer;
    @JsonIgnore
    @OneToMany(mappedBy = "walletByDestination")
    private Collection<PaymentEntity> paymentsBySeller;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner", referencedColumnName = "id", nullable = false)
    private UsersEntity usersByOwner;

    protected WalletEntity() {
    }

    public WalletEntity(UsersEntity usersEntity) {
        this.usersByOwner = usersEntity;
        this.balance = 0L;
    }

    public Long plus(Long val){
        return this.balance+=val;
    }

    public Long minus(Long val){
        return this.balance-=val;
    }

}
