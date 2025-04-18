# BackConstruction
PIDEV
# MegaMinds Project Management System

![Project Architecture](docs/architecture.png) <!-- Optionnel: Ajouter un diagramme -->

Un système intégré de gestion de projets avec :
- **Backend Spring Boot** (Microservices)
- **API .NET** (Legacy System)
- **Frontend Angular** (à venir)

## 📋 Table des matières
- [Technologies](#-technologies)
- [Installation](#-installation)
- [API Endpoints](#-api-endpoints)
- [Gestion de Projet](#-gestion-de-projet)
- [Workflow d'Intégration](#-workflow-dintégration)
- [Déploiement](#-déploiement)

## 🛠 Technologies

### Backend Spring Boot
- Java 17
- Spring Boot 3.x
- Spring Cloud Gateway
- WebFlux (WebClient)
- MongoDB

### API .NET
- .NET 6
- MongoDB Driver
- Swagger

### Infrastructure
- Docker
- Kubernetes (optionnel)
- NGINX (pour le frontend)

## 🚀 Installation

### Prérequis
- JDK 17
- .NET 6 SDK
- Docker Desktop
- MongoDB 6+

### Lancer les services
```bash
# 1. Démarrer MongoDB
docker run --name megaminds-mongo -p 27017:27017 -d mongo:latest

# 2. Lancer l'API .NET
cd dotnet-api
dotnet run

# 3. Lancer le Spring Boot Gateway
cd spring-gateway
./mvnw spring-boot:run
