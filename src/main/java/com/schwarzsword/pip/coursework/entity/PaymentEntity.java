package com.schwarzsword.pip.coursework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "payment", schema = "public", catalog = "s243884")
public class PaymentEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Basic
    @Column(name = "sum", nullable = false)
    private Long sum;
    @JsonIgnore
    @OneToOne(mappedBy = "paymentByPayment")
    private DealEntity dealById;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source", referencedColumnName = "id", nullable = false)
    private WalletEntity walletBySource;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination", referencedColumnName = "id", nullable = false)
    private WalletEntity walletByDestination;

    protected PaymentEntity(){}

    public PaymentEntity(Long sum, WalletEntity walletBySource, WalletEntity walletByDestination){
        this.sum=sum;
        this.walletBySource=walletBySource;
        this.walletByDestination=walletByDestination;
    }
}
