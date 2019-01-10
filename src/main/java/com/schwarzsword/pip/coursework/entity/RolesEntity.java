package com.schwarzsword.pip.coursework.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;
@Data
@Entity
@Table(name = "roles", schema = "public", catalog = "s243884")
public class RolesEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Basic
    @Column(name = "role", nullable = false, length = 20)
    private String role;
    protected RolesEntity(){}
    public RolesEntity(String role){
        this.role = role;
    }
}
