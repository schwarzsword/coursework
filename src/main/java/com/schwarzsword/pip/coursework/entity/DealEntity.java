package com.schwarzsword.pip.coursework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "deal", schema = "public", catalog = "s243884")
public class DealEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sold_date", referencedColumnName = "id")
    private EndDateEntity endDateBySoldDate;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "customer", referencedColumnName = "id")
    private UsersEntity usersByCustomer;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment", referencedColumnName = "id", nullable = false)
    private PaymentEntity paymentByPayment;

    protected DealEntity() {
    }

    public DealEntity(EndDateEntity endDateEntity, UsersEntity usersByCustomer, PaymentEntity paymentEntity) {
        this.endDateBySoldDate = endDateEntity;
        this.usersByCustomer = usersByCustomer;
        this.paymentByPayment = paymentEntity;
    }
}
