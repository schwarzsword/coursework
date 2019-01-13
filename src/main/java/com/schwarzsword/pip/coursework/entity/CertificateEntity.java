package com.schwarzsword.pip.coursework.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "certificate", schema = "public", catalog = "s243884")
public class CertificateEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Basic
    @Column(name = "date", nullable = false)
    private Timestamp date;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expert", referencedColumnName = "id")
    private UsersEntity usersByExpert;

    protected CertificateEntity() {}

    public CertificateEntity(UsersEntity user) {
        this.usersByExpert = user;
        this.date = Timestamp.valueOf(LocalDateTime.now());
    }
}
