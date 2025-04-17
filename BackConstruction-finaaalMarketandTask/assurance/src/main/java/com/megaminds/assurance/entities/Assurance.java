    package com.megaminds.assurance.entities;

    import com.fasterxml.jackson.annotation.JsonIgnore;
    import jakarta.persistence.*;
    import lombok.*;

    import java.util.ArrayList;
    import java.util.List;

    @Entity
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class Assurance {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String name;
        private String description;
        private String adresse;
        private String email;
        private String telephone;
        private String fax;
        private String logo;
        private String siteWeb;

        @OneToMany(mappedBy = "assurance", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonIgnore
        private List<Contrat> contrats = new ArrayList<>();

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getAdresse() {
            return adresse;
        }

        public void setAdresse(String adresse) {
            this.adresse = adresse;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getFax() {
            return fax;
        }

        public void setFax(String fax) {
            this.fax = fax;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getSiteWeb() {
            return siteWeb;
        }

        public void setSiteWeb(String siteWeb) {
            this.siteWeb = siteWeb;
        }

        public List<Contrat> getContrats() {
            return contrats;
        }

        public void setContrats(List<Contrat> contrats) {
            this.contrats = contrats;
        }
    }
