package com.schwarzsword.pip.coursework.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

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
    @ManyToOne
    @JoinColumn(name = "expert", referencedColumnName = "id")
    private UsersEntity usersByExpert;
    @OneToMany(mappedBy = "certificateByCertificate")
    private Collection<PaintingEntity> paintings;

    protected CertificateEntity(){}

    public  CertificateEntity(UsersEntity user){
        this.usersByExpert = user;
        this.date = Timestamp.valueOf(LocalDateTime.now());
    }
}
