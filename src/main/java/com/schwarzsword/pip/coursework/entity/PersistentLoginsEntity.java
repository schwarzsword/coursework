package com.schwarzsword.pip.coursework.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
@Data
@Entity
@Table(name = "persistent_logins", schema = "public", catalog = "s243884")
public class PersistentLoginsEntity {
    @Basic
    @Column(name = "username", nullable = false, length = 100)
    private String username;
    @Id
    @Column(name = "series", nullable = false, length = 64)
    private String series;
    @Basic
    @Column(name = "token", nullable = false, length = 64)
    private String token;
    @Basic
    @Column(name = "last_used", nullable = false)
    private Timestamp lastUsed;
}
