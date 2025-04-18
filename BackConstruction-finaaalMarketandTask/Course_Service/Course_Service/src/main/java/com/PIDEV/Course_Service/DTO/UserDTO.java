package com.PIDEV.Course_Service.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDTO {
    private Long id_User;
    private String login;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private int num_tel;
    private String keycloakId;
    private byte[] image;
    // Ajoutez d'autres champs si nécessaire


    public Long getId_User() {
        return id_User;
    }

    public void setId_User(Long id_User) {
        this.id_User = id_User;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getNum_tel() {
        return num_tel;
    }

    public void setNum_tel(int num_tel) {
        this.num_tel = num_tel;
    }

    public String getKeycloakId() {
        return keycloakId;
    }

    public void setKeycloakId(String keycloakId) {
        this.keycloakId = keycloakId;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}