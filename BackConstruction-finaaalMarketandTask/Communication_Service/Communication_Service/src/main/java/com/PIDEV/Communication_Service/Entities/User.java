package com.PIDEV.Communication_Service.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "User")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Iduser;

    private String username;

    private String email;

    private String password;

    private String avatar = "assets/images/admin.png"; // Chemin par défaut
    private String status = "online"; // online/offline


    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "Iduser"),
            inverseJoinColumns = @JoinColumn(name = "Idrole")
    )
    private List<Role> roles = new ArrayList<>();



    @OneToMany(mappedBy = "user" ,cascade =   CascadeType.ALL)
    @JsonIgnore
    private List<Reclamation> Reclamation;


    @ManyToMany(mappedBy = "participants")
    @JsonIgnore
    private List<ChatRoom> chatRooms = new ArrayList<>();

    public Long getIduser() {
        return Iduser;
    }

    public void setIduser(Long iduser) {
        Iduser = iduser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<com.PIDEV.Communication_Service.Entities.Reclamation> getReclamation() {
        return Reclamation;
    }

    public void setReclamation(List<com.PIDEV.Communication_Service.Entities.Reclamation> reclamation) {
        Reclamation = reclamation;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ChatRoom> getChatRooms() {
        return chatRooms;
    }

    public void setChatRooms(List<ChatRoom> chatRooms) {
        this.chatRooms = chatRooms;
    }
}
