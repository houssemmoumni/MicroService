# CONSPRIT

## Description du projet

**CONSPRIT** est une plateforme de gestion de construction, développé par l’équipe **MEGAMINDS** dans le cadre de notre formation en spécialité **SAE** à **ESPRIT**. Ce projet s’inscrit dans un travail collaboratif réalisé par 7 étudiants, chacun responsable de **deux microservices**, en appliquant les principes de l’architecture **microservices** avec **Spring Boot**.

Le projet vise à fournir une solution complète, modulaire et évolutive pour répondre aux besoins réels de gestion dans le secteur de la construction.

## Fonctionnalités principales

Le système couvre plusieurs modules métiers essentiels :

- 🔐 **Gestion des utilisateurs** : Authentification, autorisation, rôles.
- 🏗️ **Gestion des projets** : Suivi de l’état d’avancement des chantiers.
- 📋 **Gestion des tâches** : Affectation et suivi des missions.
- 🛠️ **Gestion des ressources** : Gestion des matériaux, équipements et personnel.
- 💰 **Gestion financière** : Suivi des dépenses et du budget.
- 💬 **Communication interne** : Échanges entre les intervenants du chantier.
- 📆 **Gestion des pointages** : Gestion des présences et absences.
- 📑 **Gestion du marché** : Appels d’offres, fournisseurs, contrats.
- 📚 **Formations et blog** : Publication de contenus internes.
- 🧑‍💼 **Recrutement** : Offres d’emploi, candidatures, entretiens.
- 🛡️ **Assurances** : Liaison entre les projets et les contrats d’assurance.
- ⚠️ **Incidents** : Déclaration, affectation et résolution des incidents.

## Architecture technique

- ⚙️ **Spring Boot** : Développement des microservices
- ☁️ **Spring Cloud Config Server** : Gestion centralisée des configurations
- 🔍 **Eureka Discovery Server** : Enregistrement et découverte des services
- 🛢️ **Bases de données utilisées** :
  - **MySQL/MariaDB via XAMPP** : Pour la gestion des données relationnelles en local
  - **MongoDB** pour la persistance des données non relationnelles
  - **H2** pour les tests ou microservices légers en mémoire
- 🔐 **JWT** : Sécurité et gestion des accès
- 🔀 Architecture **full microservices** : chaque service dispose de son propre `pom.xml`, port dédié et configuration indépendante

## Instructions de déploiement

1. Lancer le **Config Server** (`port 8888`)
2. Lancer le **Eureka Server** (`port 8761`)
3. Lancer chaque microservice individuellement (ex : Recrutement `port 8060`)
4. Les services chargeront automatiquement leur configuration et s’enregistreront dans Eureka

## Objectifs pédagogiques

- Implémenter une architecture **microservices distribuée**
- Appliquer les bonnes pratiques de **modularité et scalabilité**
- Développer en équipe avec une répartition claire des responsabilités
- Utiliser des outils modernes pour la **gestion de configuration**, **sécurité** et **découverte de services**

---

> 👨‍💻 Réalisé par : **Équipe MEGAMINDS** – Spécialité SAE – ESPRIT  
> 📅 Année universitaire : 2024 / 2025  
> 🧑‍🏫 Encadré par : **Équipe PIDEV**  
> 🔗 GitHub : [https://github.com/houssemmoumni/BackConstruction](https://github.com/houssemmoumni/BackConstruction)
