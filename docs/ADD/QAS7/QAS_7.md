# ADD 7 – Independent Deployment / Automatic Deployment

*Attribute-Driven Design (ADD) Full Documentation*

---

## 1. Overview

This ADD focuses on **Independent Deployment of Microservices** in the LMS reengineering project. The goal is to ensure each microservice can be **deployed independently** without causing downtime or affecting other services.

- **Objective:** Allow **automatic deployment** of services with minimal impact on existing clients.
- **Functional Scope:** All LMS services (Books, Authors, Genres, Readers, Users).
- **Quality Attributes Addressed:** Deployability, Availability, Maintainability, Fault Tolerance.
- **Compatibility:** Existing API contracts must remain intact during deployments.
- **Extensibility:** New services can adopt the same deployment patterns without affecting others.

---

## Step 1. Review Inputs

### 1.1 Objective

- Deploy services **independently** and **automatically** via the CI/CD pipeline.
- Avoid **downtime** for other services during deployment.
- Enable **rollback** in case of failures.
- Maintain **consistency and availability** across the system.

### 1.2 Problem Statement

- Monolithic LMS deployments affect **all functionalities**, causing downtime.
- Any change in one module requires **full system redeployment**, increasing risk.
- Lack of independent deployment slows **release cycles** and reduces agility.

### 1.3 Requirements (SMART)

| Type           | Requirement                                                                                   |
|----------------|-----------------------------------------------------------------------------------------------|
| **Specific**   | Each microservice can be deployed independently using automated pipelines.                    |
| **Measurable** | Deployment logs demonstrate successful rollout without downtime or impact on other services.   |
| **Attainable** | Implementable via **CI/CD pipelines, container orchestration, blue-green/canary deployment**.|
| **Relevant**   | Critical for faster delivery, reduced risk, and minimal disruption to clients.                |
| **Time-Bound** | Implemented in the current sprint/project.                                                    |

**Variation and Evolution Points**

- **Variation points:** Deployment strategies (canary, blue-green, rolling update).
- **Evolution points:** Future services adopt same deployment pipelines with minimal configuration changes.

---

## Step 2 – Establish Goals and Select Inputs

### 2.1 Drivers

| Type             | Driver                                                                                      |
|-----------------|---------------------------------------------------------------------------------------------|
| **Functional**  | Deploy LMS microservices independently.                                                     |
| **Quality**     | Deployability, Availability, Fault Tolerance.                                               |
| **Constraints** | No downtime for existing services; maintain API compatibility.                               |
| **Business**    | Support rapid release cycles, minimize client disruption, and reduce operational risk.      |

### 2.2 Design Decisions

- **Pipeline Integration:** Fully automate deployment using **CI/CD pipelines**.
- **Deployment Strategies:** Adopt **blue-green, canary, or rolling updates**.
- **Service Isolation:** Deploy one service at a time without affecting others.
- **Monitoring & Rollback:** Detect failures and **rollback automatically** if necessary.
- **Infrastructure:** Use Docker/Kubernetes for container orchestration.

---

## Step 3 – Choose Elements to Decompose

### 3.1 Selected Elements

| Element              | Purpose                                                  |
|---------------------|----------------------------------------------------------|
| CI/CD Pipeline       | Automate build, test, and deployment stages             |
| Microservice Containers | Independent units for deployment                        |
| Orchestrator         | Manage deployment, scaling, and rollback                |
| Deployment Strategy Config | Define canary/blue-green/rolling update behavior      |
| Monitoring & Health Checks | Detect failures during deployment                    |

### 3.2 Rationale for Selection

- Allows **frequent and safe deployments**.
- **Rollback capabilities** ensure minimal service disruption.
- **Service isolation** reduces risk and improves maintainability.

---

## Step 4 – Choose Design Concepts

### 4.1 Tactics

| Quality Attribute      | Tactic                           | Description                                        | Implementation                                           |
|------------------------|----------------------------------|--------------------------------------------------|---------------------------------------------------------|
| Deployability          | Independent Deployment           | Each service deploys without affecting others    | CI/CD pipeline triggers per service                     |
| Availability           | Blue-Green / Canary Deployment   | Avoid downtime during rollout                     | Route traffic gradually to new instances               |
| Fault Tolerance        | Rollback on Failure              | Automatic revert if deployment fails             | Pipeline checks health, triggers rollback              |
| Maintainability        | Modular Deployment               | Deploy one service independently                 | Service containers managed separately                  |
| Testability            | Staging Validation               | Test deployments in staging before production   | Automated smoke/load tests integrated in pipeline      |

### 4.2 Reference Architecture / Patterns

- **Blue-Green Deployment:** Deploy new version alongside old, switch traffic after validation.
- **Canary Deployment:** Gradually route small percentage of traffic to new version.
- **Rolling Update:** Incrementally update service replicas to new version.
- **Health Checks & Monitoring:** Detect and revert failed deployments.

### 4.3 Architectural Design Alternatives and Rationale

| Alternative                    | Description                                | Advantages                        | Disadvantages                  | Decision    |
|--------------------------------|--------------------------------------------|-----------------------------------|--------------------------------|-------------|
| Manual deployment              | Developer deploys service manually         | Simple                            | Risk of human error, downtime  | ❌ Rejected |
| Full-system redeployment       | Deploy entire LMS for each update          | Predictable                        | High downtime, slow release    | ❌ Rejected |
| Independent automated deployment| Deploy services individually with rollback | Safe, fast, minimal downtime      | Requires pipeline setup        | ✅ Selected |

---

## Step 5 – Instantiate Elements, Allocate Responsibilities, Define Interfaces

| Component                  | Responsibility                                    | Interface/Methods                               |
|----------------------------|--------------------------------------------------|------------------------------------------------|
| CI/CD Pipeline             | Automate build, test, deployment                | build(), test(), deploy(service)              |
| Microservice Containers     | Encapsulate service logic and dependencies      | start(), stop(), healthCheck()                 |
| Orchestrator               | Manage container deployment, scaling, rollback | deploy(), scale(), rollback()                  |
| Deployment Strategy Config  | Define traffic routing rules                     | configureBlueGreen(), configureCanary()        |
| Monitoring & Health Checks  | Detect failures and trigger rollback            | checkHealth(), notifyFailure()                 |

---

## Step 6 – Sketch Views and Record Decisions

### 6.1 Trade-Off Analysis

| Decision                               | Benefit                                     | Cost                                     |
|----------------------------------------|--------------------------------------------|-----------------------------------------|
| Independent deployment per service     | Faster releases, minimal downtime          | Requires pipeline and orchestration setup |
| Blue-Green / Canary deployment         | Zero-downtime deployment                    | Slightly more infrastructure needed    |
| Automated rollback                      | Reduces risk of failed deployments          | Complexity in pipeline configuration   |

### 6.2 Outcome

- LMS microservices can be deployed **independently and safely**.
- Supports **zero-downtime deployment** for all services.
- Rollback mechanism ensures **fault tolerance during deployment**.
- Enables **frequent releases** and **continuous delivery**.

---

## Step 7 – Perform Analysis of Current Design and Review Iteration Goals

- Monolithic LMS requires **full redeployment**, causing downtime.
- Independent deployment with automated pipelines ensures **rapid and safe releases**.
- Deployment strategies (blue-green/canary) reduce risk and maintain **high availability**.
- CI/CD integration allows **consistent, repeatable deployments**.

**Result:** LMS microservices are **independently deployable**, with automated pipelines supporting **zero-downtime releases**, rollback on failures, and safe, frequent delivery.
