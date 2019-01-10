package com.schwarzsword.pip.coursework.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
@Table(name = "painting", schema = "public", catalog = "s243884")
public class PaintingEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Basic
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @Basic
    @Column(name = "author", nullable = false, length = 50)
    private String author;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "img", nullable = false)
    private String img;
    @Basic
    @Column(name = "genre", nullable = false, length = 30)
    private String genre;
    @Basic
    @Column(name = "technique", nullable = false, length = 30)
    private String technique;
    @OneToMany(mappedBy = "paintingByPainting")
    private Collection<LotEntity> lotsById;
    @ManyToOne
    @JoinColumn(name = "certificate", referencedColumnName = "id")
    private CertificateEntity certificateByCertificate;

    protected PaintingEntity() {
    }

    public PaintingEntity(String name, String author, String description, String img, String genre, String technique) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.img = img;
        this.genre = genre;
        this.technique = technique;
    }
}
