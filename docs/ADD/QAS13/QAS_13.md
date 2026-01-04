# ADD 13 – Infrastructure as Code (IaC) / Reproducibility

*Attribute-Driven Design (ADD) Full Documentation*

---

## 1. Overview

This ADD focuses on **Infrastructure as Code (IaC) and environment reproducibility** for LMS microservices. The goal is to ensure that the **deployment environment is consistent, reproducible, and version-controlled**, reducing errors and facilitating continuous delivery.

- **Objective:** Provide fully reproducible infrastructure through code.
- **Functional Scope:** All LMS microservices (Books, Authors, Genres, Readers, Users).
- **Quality Attributes Addressed:** Deployability, Maintainability, Consistency, Traceability.
- **Compatibility:** Environments match production to prevent client-impacting discrepancies.
- **Extensibility:** New services adopt the same IaC patterns and configurations.

---

## Step 1 – Review Inputs

### 1.1 Objective

- Represent infrastructure as **code artifacts**: Dockerfiles, Docker Compose, Swarm/Kubernetes manifests.
- Ensure **reproducibility** of environments across dev, staging, and production.
- Integrate IaC with **CI/CD pipelines** to automate provisioning and deployments.

### 1.2 Problem Statement

- Manual provisioning leads to **configuration drift** and environment inconsistencies.
- Differences between dev, staging, and production environments cause **bugs and deployment failures**.
- Lack of version-controlled infrastructure reduces **traceability and maintainability**.

### 1.3 Requirements (SMART)

| Type           | Requirement                                                                                   |
|----------------|-----------------------------------------------------------------------------------------------|
| **Specific**   | All microservices must have IaC artifacts that reproduce the environment reliably.            |
| **Measurable** | Environments can be recreated automatically with <5 min of setup using Docker/Compose/IaC.   |
| **Attainable** | Use standard containerization and orchestration tools (Docker, Compose, Swarm/K8s).          |
| **Relevant**   | Critical for consistent deployments, CI/CD automation, and developer productivity.            |
| **Time-Bound** | Implemented during the LMS microservices reengineering sprint.                                 |

**Variation and Evolution Points**

- **Variation points:** Environment variables, service replicas, network configuration.
- **Evolution points:** Infrastructure templates support new services or features seamlessly.

---

## Step 2 – Establish Goals and Select Inputs

### 2.1 Drivers

| Type             | Driver                                                                                      |
|-----------------|---------------------------------------------------------------------------------------------|
| **Functional**  | Recreate environments reproducibly across dev, staging, and production.                     |
| **Quality**     | Consistency, Deployability, Maintainability, Traceability.                                   |
| **Constraints** | Use container-based deployment and standard orchestration tools.                             |
| **Business**    | Minimize deployment failures and configuration errors; improve developer and operational efficiency. |

### 2.2 Design Decisions

- **Dockerfile per microservice**: defines container build process.
- **Docker Compose / Swarm Stack**: orchestrate multi-service environments locally and on remote servers.
- **Version Control**: Infrastructure code committed alongside service code.
- **Environment Variables**: parameterize environment-specific settings.
- **CI/CD Integration**: Pipeline automates build, push, and deploy using IaC artifacts.

---

## Step 3 – Choose Elements to Decompose

### 3.1 Selected Elements

| Element                  | Purpose                                                   |
|--------------------------|-----------------------------------------------------------|
| Dockerfile                | Defines service container image                            |
| Docker Compose / Swarm Stack | Orchestrates multi-service environment                 |
| Environment Configuration | Parameterizes settings for dev, staging, and prod        |
| Version Control Repository | Stores infrastructure code alongside service code       |
| CI/CD Pipeline            | Builds, tests, and deploys containers automatically      |

### 3.2 Rationale for Selection

- **Dockerfiles** provide consistent, reproducible builds per service.
- **Compose / Swarm manifests** ensure multi-service deployment reproducibility.
- **Version control** ensures traceability of infrastructure changes.
- **CI/CD integration** automates provisioning, reducing human error.

---

## Step 4 – Choose Design Concepts

### 4.1 Tactics

| Quality Attribute      | Tactic                       | Description                                           | Implementation                                      |
|------------------------|------------------------------|-----------------------------------------------------|----------------------------------------------------|
| Consistency            | IaC Artifacts                | Environment described in code                        | Dockerfile, Compose, Swarm/K8s manifests          |
| Reproducibility        | Parameterized Configurations | Environment variables control deployment specifics  | `.env` files, YAML parameters                      |
| Traceability           | Version Control              | All infrastructure changes are tracked              | Git repository, commit history                    |
| Deployability          | CI/CD Integration            | Infrastructure code used in automated pipeline     | Build & deploy stages use Docker/Compose commands |
| Maintainability        | Modular Templates            | Reusable templates for new services                 | Base Docker images, service templates             |

### 4.2 Reference Architecture / Patterns

- **Dockerfile**: Defines container image build per service.
- **Docker Compose / Swarm Stack**: Defines multi-service environment orchestration.
- **Environment Variables / Configs**: Parameterize deployment without code changes.
- **CI/CD Pipeline**: Automates build, test, and deployment using IaC artifacts.

### 4.3 Architectural Design Alternatives and Rationale

| Alternative                    | Description                                  | Advantages                        | Disadvantages                  | Decision    |
|--------------------------------|----------------------------------------------|-----------------------------------|--------------------------------|-------------|
| Manual provisioning             | Setup environment manually                  | Simple for one-time deployment    | Error-prone, inconsistent       | ❌ Rejected |
| Containerized, non-versioned    | Docker containers but not tracked in VCS     | Some consistency                  | Difficult to reproduce           | ❌ Rejected |
| Full IaC with Docker + Compose/Swarm | Versioned, reproducible environments        | Fully reproducible, automated     | Initial setup effort required    | ✅ Selected |

---

## Step 5 – Instantiate Elements, Allocate Responsibilities, Define Interfaces

| Component                  | Responsibility                                        | Interface/Methods                               |
|----------------------------|----------------------------------------------------|------------------------------------------------|
| Dockerfile                  | Define service container image                       | FROM, RUN, COPY, CMD                            |
| Docker Compose / Swarm Stack | Orchestrate multi-service deployment                 | `docker-compose up`, `docker stack deploy`     |
| Environment Configuration    | Parameterize deployments                             | `.env` files, YAML parameters                  |
| Version Control Repository   | Track all infrastructure code changes                | git commit, git push, git tags                 |
| CI/CD Pipeline               | Build, push, and deploy containers automatically    | pipeline scripts, docker build, docker push    |

---

## Step 6 – Sketch Views and Record Decisions

### 6.1 Trade-Off Analysis

| Decision                               | Benefit                                     | Cost                                     |
|----------------------------------------|--------------------------------------------|-----------------------------------------|
| Dockerfile per service                  | Consistent, reproducible container builds  | Requires learning Docker best practices  |
| Compose / Swarm orchestration           | Multi-service reproducibility               | Additional configuration complexity      |
| Version control IaC                     | Traceability and collaboration              | Discipline to commit all changes         |
| CI/CD integration                       | Automated reproducible deployments          | Initial setup effort                     |

### 6.2 Outcome

- Environments are **fully reproducible and version-controlled**.
- Deployment errors due to inconsistent setups are **eliminated**.
- CI/CD pipelines **automate build and deployment**, ensuring reliability.
- New services adopt IaC **without extra manual effort**.

---

## Step 7 – Perform Analysis of Current Design and Review Iteration Goals

- Monolithic LMS required **manual environment setup**, prone to errors.
- IaC ensures **every developer and pipeline sees the same environment**.
- Docker + Compose/Swarm provides **reproducibility and scalability**.
- Supports **continuous delivery, safe releases, and maintainability**.

**Result:** LMS microservices infrastructure is **version-controlled, reproducible, and automated**, enabling **reliable, consistent deployments across all environments**.
