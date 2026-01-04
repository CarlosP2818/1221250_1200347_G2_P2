# ADD 4 - Availability

*Attribute-Driven Design (ADD) Full Documentation*

---

## 1. Overview

This document presents the full application of *Attribute-Driven Design (ADD)* for the *Quality Attribute Scenario (QAS 4 - "Availability")*.

The scenario outlines the requirements and design considerations for ensuring **high availability** in the LMS application:
- **Objective:** Ensure the system remains available despite service failures or infrastructure faults.
- **Functionalities:** All LMS operations (create book, author, reader, user, etc.) must remain accessible during failures.
- **Extensibility:** Support scaling and failover for future services.
- **Performance:** Minimal degradation during failures.
- **Availability:** Automatic recovery and fault isolation.
- **Compatibility:** Existing clients continue to function with no downtime.

---

## Step 1. Review Inputs

### 1.1 Objective

- Maintain **high availability** across all LMS services.
- Minimize service disruption during **hardware, network, or container failures**.
- Support **auto-recovery, failover, and load balancing**.
- Align with **SOA/API-led connectivity strategy**.

### 1.2 Problem Statement

The LMS monolithic system limits availability:

- **Single point of failure:** Failure in one module affects the whole system.
- **Limited resilience:** No automatic recovery mechanisms.
- **Client impact:** Users experience downtime during failures.

---

### 1.3 Requirements (SMART)

| Type           | Requirement                                                                                   |
|----------------|-----------------------------------------------------------------------------------------------|
| **Specific**   | Implement automated failover and recovery mechanisms for all microservices.                   |
| **Measurable** | System uptime ≥ 99.9%; failed services recover within <30 seconds.                             |
| **Attainable** | Achievable with independent microservices, load balancers, circuit breakers, and monitoring.  |
| **Relevant**   | Critical for user experience and system reliability.                                          |
| **Time-Bound** | Implementation planned for the current sprint/version.                                         |

**Variation and Evolution Points**

- **Variation points:**
    - Service instance recovery strategies (restart, replace, reroute traffic).
    - Health check thresholds and retry policies.
- **Evolution points:**
    - Additional services can join the availability mechanisms seamlessly.
    - New orchestration patterns can be adopted (e.g., canary deployment).

---

## Step 2: Establish Goals and Select Inputs

### 2.1 Drivers

| Type             | Driver                                                                                      |
|-----------------|---------------------------------------------------------------------------------------------|
| **Functional**  | Ensure uninterrupted access to all LMS functionalities.                                      |
| **Quality**     | Availability, Fault Tolerance, Resilience, Scalability.                                      |
| **Constraints** | Distributed microservice architecture; minimal client impact; efficient use of hardware.    |
| **Business**    | Continuous service is essential for library operations and external integrations.           |

### 2.2 Design Decisions

- **Architecture:** Independent microservices with per-service DB and API endpoints.
- **Orchestration:** Kubernetes (or similar) for automatic instance management.
- **Resilience:** Circuit breakers, retries, and failover for each service.
- **Scalability:** Horizontal scaling per service.
- **Monitoring:** Health checks, logging, alerting.
- **Fallback:** API Gateway handles degraded responses gracefully.

---

## Step 3 - Choose Elements to Decompose

### 3.1 Selected Elements

- **Independent Microservices:** BookService, AuthorService, GenreService, ReaderService, UserService.
- **API Gateway:** Routes requests and handles fallback responses.
- **Load Balancer:** Distributes traffic and detects unhealthy instances.
- **Orchestrator:** Kubernetes for scaling, health checks, and failover.
- **Monitoring & Alerts:** Centralized logging and health metrics.
- **Cache Layer:** Redis or similar to reduce load during recovery.

### 3.2 Rationale for Selection

- Isolates failures to individual services.
- Enables independent scaling and recovery.
- Reduces client impact and downtime.
- Provides observability for proactive maintenance.

---

## Step 4 - Choose Design Concepts

### 4.1 Tactics

| Quality Attribute      | Tactic                             | Description                                                      | Implementation                                                    |
|------------------------|------------------------------------|------------------------------------------------------------------|------------------------------------------------------------------|
| **Availability**       | Component Isolation                | Failures in one service do not affect others.                     | Independent services, circuit breaker, retries.                 |
| **Availability**       | Failover / Recovery                | Automatically replace failed instances.                           | Kubernetes pod restart, auto-scaling groups.                     |
| **Performance**        | Caching                            | Reduce load on services during failure recovery.                  | Redis / in-memory cache.                                         |
| **Scalability**        | Horizontal Scaling                 | Services replicated to handle load.                               | Kubernetes deployments, auto-scaling policies.                   |
| **Maintainability**    | Modularization                     | Independent services allow updates without downtime.              | Each microservice encapsulates its domain and DB.                |
| **Testability**        | Health Checks & Monitoring         | Detect failures quickly.                                          | Probes, metrics, alerts.                                         |

### 4.2 Reference Architecture / Patterns

- **Circuit Breaker & Retry:** Protects services from cascading failures.
- **Load Balancer + Orchestrator:** Redirects traffic to healthy instances.
- **Distributed Cache:** Reduces load on services recovering from failure.
- **API Gateway:** Handles degraded or fallback responses.

### 4.3 Patterns

- **Circuit Breaker / Retry Pattern**
- **Database per Service**
- **Health Check / Self-healing**

### 4.4 Architectural Design Alternatives and Rationale

| Alternative                           | Description                                | Advantages                        | Disadvantages                   | Decision    |
|---------------------------------------|--------------------------------------------|-----------------------------------|--------------------------------|-------------|
| Monolithic refactoring                 | Keep system monolithic with added redundancy | Easier initial deployment         | High risk of downtime; less granular control | ❌ Rejected |
| Microservices + Resilience Patterns    | Independent services with failover        | High availability, fault isolation | Operational complexity          | ✅ Selected |

---

## Step 5 - Instantiate Elements, Allocate Responsibilities, Define Interfaces

### 5.1 Main Components

| Component          | Responsibility                                           | Interface/Methods                      |
|-------------------|----------------------------------------------------------|---------------------------------------|
| API Gateway        | Route requests, fallback responses                      | REST endpoints                          |
| BookService        | Handle book CRUD, ensure recovery on failure           | createBook(), getBook(), updateBook() |
| AuthorService      | Handle author CRUD, fault tolerant                      | createAuthor(), getAuthor(), updateAuthor() |
| GenreService       | Handle genre CRUD, fault tolerant                       | createGenre(), getGenre(), updateGenre() |
| ReaderService      | Handle reader CRUD, fault tolerant                      | createReader(), getReader(), updateReader() |
| UserService        | Handle user CRUD, fault tolerant                        | createUser(), getUser(), updateUser() |
| Load Balancer      | Route traffic to healthy instances                      | distribute(), healthCheck()           |
| Orchestrator       | Auto-restart failed instances                            | scaleUp(), scaleDown(), restartPod() |
| CacheLayer         | Provide fast data access during recovery                | get(), put(), invalidate()             |

---

## Step 6 - Sketch Views and Record Decisions

### 6.1 Trade-Off Analysis

| Decision                        | Benefit                                     | Cost                                     |
|---------------------------------|--------------------------------------------|-----------------------------------------|
| Microservices + Circuit Breaker | High availability, fault isolation         | More operational complexity             |
| Load Balancer + Auto Recovery   | Minimize downtime, automatic failover      | Infrastructure and configuration cost   |
| Distributed Cache               | Reduces load during failures               | Eventual consistency issues             |

### 6.2 Outcome

- LMS can tolerate service failures with minimal user impact.
- Independent, scalable, and fault-tolerant services.
- Observability ensures proactive detection and recovery.

---

## Step 7 - Perform Analysis of Current Design and Review Iteration Goals

- Transform monolithic LMS into a resilient, distributed architecture.
- Ensure **uptime ≥ 99.9%** under normal and peak load.
- Implement **failover, auto-recovery, and circuit breakers**.
- Provide fallback responses via API Gateway.
- Observability, caching, and automated recovery mechanisms implemented.

**Result:** Highly available LMS architecture, minimizing downtime and ensuring service continuity for clients.
