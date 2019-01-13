package com.schwarzsword.pip.coursework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "end_date", schema = "public", catalog = "s243884")
public class EndDateEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Basic
    @Column(name = "expecting_date", nullable = false)
    private Timestamp expectingDate;
    @Basic
    @Column(name = "state", nullable = false)
    private Boolean state;
    @JsonIgnore
    @OneToOne(mappedBy = "endDateBySoldDate")
    private DealEntity dealById;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot", referencedColumnName = "id", nullable = false)
    private LotEntity lotByLot;

    protected EndDateEntity() {
    }

    public EndDateEntity(LotEntity lot) {
        this.lotByLot = lot;
        this.state = true;
        this.expectingDate = Timestamp.valueOf(lot.getStartDate().toLocalDateTime().plusDays(7));
    }
}
