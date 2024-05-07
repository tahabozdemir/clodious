package com.bozdemir.clodious.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;


import java.io.Serializable;

@Entity
@Table(name = "meta_file")
public class FileMeta implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private String path;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public FileMeta(String name, String type, String path, User user) {
        this.name = name;
        this.type = type;
        this.path = path;
        this.user = user;
    }

    public FileMeta() {
        
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
