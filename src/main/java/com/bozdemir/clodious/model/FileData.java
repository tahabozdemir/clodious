package com.bozdemir.clodious.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "files")
public class FileData implements Serializable {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    private UUID id;
    @NotBlank
    private String name;
    @NotBlank
    private String type;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @CreatedDate
    private Date creationDate;
    @Lob
    private byte[] data;

    public FileData() {}
    public FileData(String name, String type, User user, byte[] data) {
        this.name = name;
        this.type = type;
        this.user = user;
        this.creationDate = new Date();
        this.data = data;
    }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Date getCreationDate() { return creationDate; }
    public byte[] getData() { return data; }
    public void setData(byte[] data) { this.data = data; }
}
