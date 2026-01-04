# QAS 8 - CI/CD Pipeline

*Attribute-Driven Design (ADD) Full Documentation*

---

## 1. Overview

This ADD focuses on **CI/CD automation** for the LMS microservices.

- **Objective:** Automate build, test, verification, containerization, and deployment.
- **Functionalities:** Build, static analysis, unit/mutation tests, contract tests, Docker image creation.
- **Availability:** Ensure pipeline reliability; reduce manual errors.
- **Compatibility:** Integrate with existing deployment and rollback mechanisms.

---

## Step 1. Review Inputs

### 1.1 Objective
- Automate the full CI/CD process
- Ensure high-quality software and reproducibility
- Support multiple environments (Dev → Staging → Prod)

### 1.2 Problem Statement
- Manual processes are error-prone
- Tests not consistent
- Containerization and deployment require coordination

### 1.3 Requirements (SMART)

| Type           | Requirement                                                           |
|----------------|-----------------------------------------------------------------------|
| **Specific**   | Full CI/CD automation for build, test, containerization, deploy       |
| **Measurable** | Pipeline logs show successful execution of all stages                 |
| **Attainable** | Using Jenkins/GitHub Actions/GitLab CI with Docker                    |
| **Relevant**   | Improves reliability, maintainability, and traceability               |
| **Time-Bound** | Implemented next sprint/version                                        |

**Variation / Evolution Points**
- Variation: Choice of CI/CD tool
- Evolution: Extend pipeline for new microservices

---

## Step 2 - Goals and Inputs

| Type             | Driver                                                          |
|-----------------|-----------------------------------------------------------------|
| **Quality**     | Testability, Reliability, Maintainability                       |
| **Constraints** | Must integrate with existing pipeline, Docker, and IaC           |
| **Business**    | Reduce errors, faster delivery, reproducible builds             |

### 2.2 Design Decisions
- Use pipeline stages: Build → Test → Static Analysis → Container → Deploy
- Unit / Mutation / Contract tests executed automatically
- Docker images built, pushed to registry
- Notifications for failures

---

## Step 3 - Elements to Decompose

| Element          | Rationale                                                 |
|-----------------|------------------------------------------------------------|
| Pipeline         | Orchestrates stages                                       |
| Test Suite       | Verify correctness, coverage                               |
| Docker           | Containerize services                                      |
| Registry         | Store images                                              |
| Notifications    | Alert failures                                           |

---

## Step 4 - Design Concepts

| Quality Attribute      | Tactic                           | Implementation                         |
|------------------------|----------------------------------|---------------------------------------|
| **Testability**        | Automated Tests                  | Unit, mutation, contract tests         |
| **Reliability**        | Pipeline Automation              | CI/CD triggers, logs                    |
| **Deployability**      | Containerization                 | Docker build & push                     |
| **Maintainability**    | Versioned Artifacts              | Tagged images, reproducible pipeline   |

**Patterns:**
- Build → Test → Deploy pipeline
- Automated rollback on failure
- Versioned Docker images

---

## Step 5 - Instantiate Elements

| Component          | Responsibility                              | Interface/Methods                  |
|-------------------|---------------------------------------------|----------------------------------|
| CI/CD Pipeline     | Orchestrate all stages                        | pipeline.yml / Jenkinsfile       |
| Test Suite         | Verify code quality and correctness           | runTests(), mutationTest()       |
| Docker/Registry    | Build, tag, push images                        | docker build, docker push        |
| Notifications      | Inform failures or success                     | email/Slack API                 |

---

## Step 6 - Trade-Offs

| Decision                  | Benefit                                   | Cost                             |
|----------------------------|-------------------------------------------|---------------------------------|
| Full automation            | Reliable, repeatable, faster feedback     | Pipeline complexity              |
| Docker images              | Portability, reproducibility              | Needs registry management       |
| Automated rollback         | Safety in deployments                      | Extra pipeline scripting        |

### Outcome
- Fully automated CI/CD pipeline
- Tests integrated and reproducible
- Dockerized services with rollback support

---

## Step 7 - Review Iteration Goals
- Ensure pipeline includes all build/test/deploy stages
- Integrate notifications and rollback mechanisms
- Maintain reproducible deployments

**Result:** CI/CD pipeline automates LMS delivery, ensures quality, and reduces human error.
