package com.schwarzsword.pip.coursework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "lot", schema = "public", catalog = "s243884")
public class LotEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Basic
    @Column(name = "start_date")
    private Timestamp startDate;
    @Basic
    @Column(name = "state", nullable = false, length = 50)
    private String state;
    @Basic
    @Column(name = "start_price", nullable = false)
    private Long startPrice;
    @Basic
    @Column(name = "policy")
    private Integer policy;
    @JsonIgnore
    @OneToOne(mappedBy = "lotByLot")
    private EndDateEntity endDateById;
    @ManyToOne
    @JoinColumn(name = "painting", referencedColumnName = "id", nullable = false)
    private PaintingEntity paintingByPainting;
    @ManyToOne
    @JoinColumn(name = "seller", referencedColumnName = "id")
    private UsersEntity usersBySeller;
    @ManyToOne
    @JoinColumn(name = "last_bet", referencedColumnName = "id")
    private UsersEntity usersByLastBet;


    protected LotEntity() {
    }

    public LotEntity(PaintingEntity paintingEntity, Long startPrice, UsersEntity seller, Integer policy) {
        long l = System.currentTimeMillis()/1000/60/60+1;
        this.startDate=new Timestamp(l*1000*60*60);
        this.startPrice=startPrice;
        this.usersBySeller = seller;
        this.paintingByPainting = paintingEntity;
        this.state="on market";
        this.policy = policy;
    }
}
