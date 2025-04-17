package com.megaminds.material.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Data
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name_image")
    private String name;

    @Column(name = "url_image")
    private String url;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private Material material;

}