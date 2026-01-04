# QAS 3 - Student B: Reader and User Creation

*Attribute-Driven Design (ADD) Full Documentation*

---

## 1. Overview

This document presents the *Attribute-Driven Design (ADD)* for *Quality Attribute Scenario (QAS) 6 - "Student B: Reader and User Creation"*.

The scenario addresses the librarian requirement:

> *As a librarian, I want to create a Reader and the respective User in the same request.*

Key objectives:

- **Unified creation:** `Reader` and `User` entities are created atomically.
- **Consistency:** Either both entities are created, or none.
- **Extensibility:** Future integrations with identity providers (SSO, OAuth, LDAP).
- **Resilience:** Failures in either service do not leave inconsistent data.
- **Performance:** Efficient under high concurrency.
- **Asynchronous Messaging:** RabbitMQ for decoupled workflow and notifications.

---

## Step 1. Review Inputs

### 1.1 Objective

- Provide a single endpoint to create a `Reader` and its associated `User`.
- Guarantee atomicity and consistency.
- Use RabbitMQ to decouple the process and handle notifications/events.

### 1.2 Problem Statement

Current system limitations:

- Separate creation of `Reader` and `User` can lead to **inconsistencies**.
- Services are tightly coupled, making scaling difficult.
- No support for asynchronous notifications or downstream processing.

---

### 1.3 Requirements (SMART)

| Type           | Requirement                                                                                   |
|----------------|-----------------------------------------------------------------------------------------------|
| **Specific**   | Create `Reader` and `User` atomically in one request.                                         |
| **Measurable** | Support X concurrent requests with <Y ms latency.                                            |
| **Attainable** | Implementable using microservices, Saga orchestration, and RabbitMQ messaging.               |
| **Relevant**   | Critical for librarian operations and system integrity.                                      |
| **Time-Bound** | Implementation planned for the current sprint/release.                                        |

**Variation and Evolution Points**

- **Variation points:**
    - Future identity providers (OAuth, SSO, LDAP).
    - Additional reader metadata or preferences.

- **Evolution points:**
    - New validation rules or workflow steps can be added without affecting existing services.

---

## Step 2: Establish Goals and Select Inputs

### 2.1 Drivers

| Type             | Driver                                                                                      |
|-----------------|---------------------------------------------------------------------------------------------|
| **Functional**  | Unified creation of Reader + User in a single request.                                       |
| **Quality**     | Availability, Reliability, Consistency, Scalability, Testability.                            |
| **Constraints** | Use RabbitMQ for asynchronous communication; services must remain decoupled.                 |
| **Business**    | Critical for librarian operations and onboarding.                                           |

### 2.2 Design Decisions

- **Microservices:** `ReaderService` and `UserService`.
- **Coordinator Service:** `ReaderUserCreationCoordinatorService` ensures atomic creation.
- **Transaction Pattern:** Saga for distributed transaction management.
- **Resilience:** Retry, circuit breaker, fallback mechanisms.
- **Messaging:** RabbitMQ for notifications and asynchronous workflow.
- **Caching (optional):** Temporary cache for high concurrency.

---

## Step 3 - Choose Elements to Decompose

### 3.1 Selected Elements

- **ReaderService:** CRUD operations for Reader.
- **UserService:** CRUD operations for User.
- **ReaderUserCreationCoordinatorService:** Orchestrates creation of both entities atomically.
- **RabbitMQ Message Broker:** Handles asynchronous notifications/events.
- **CacheLayer (optional):** Redis for temporary state tracking.

### 3.2 Rationale for Selection

- Decouples Reader and User creation responsibilities.
- Coordinator ensures atomicity without tight coupling.
- RabbitMQ supports scalable, asynchronous communication.
- Supports high-concurrency scenarios and testability.

---

## Step 4 - Choose Design Concepts

### 4.1 Tactics

| Quality Attribute      | Tactic                              | Description                                                      | Implementation                                                    |
|------------------------|------------------------------------|------------------------------------------------------------------|------------------------------------------------------------------|
| **Availability**       | Component Isolation                 | Failure in one service does not halt workflow.                  | Retry, circuit breaker, independent microservices.              |
| **Consistency**        | Distributed Transaction (Saga)      | Either both Reader and User are created or neither.             | Saga orchestrated by ReaderUserCreationCoordinatorService.       |
| **Performance**        | Caching                             | Reduce repeated validations and temporary state checks.         | Redis cache (optional).                                          |
| **Resilience**         | Retry / Circuit Breaker              | Handle transient failures in UserService.                        | Retry policy + fallback response.                                |
| **Scalability**        | Microservice Replication             | Each service scales independently under load.                    | Kubernetes/Docker auto-scaling.                                  |
| **Testability**        | Isolation / Mocking                 | Unit and integration tests without dependencies.                 | Mock ReaderService, UserService, RabbitMQ.                        |

### 4.2 Reference Architecture / Patterns

- **Saga Pattern:** Distributed transaction orchestration for atomic creation.
- **Microservices:** ReaderService and UserService.
- **API Gateway:** Single entry point for librarian requests.
- **Event-driven Architecture:** RabbitMQ for notifications and asynchronous communication.

### 4.3 Patterns

- **Coordinator Pattern (Saga):** Ensures atomic creation workflow.
- **Circuit Breaker / Retry:** Resilient service communication.
- **RabbitMQ:** Decouples services for async events and notifications.

### 4.4 Architectural Design Alternatives and Rationale

| Alternative                         | Description                              | Advantages                            | Disadvantages                      | Decision    |
|-------------------------------------|------------------------------------------|---------------------------------------|-----------------------------------|-------------|
| Monolithic creation endpoint        | Single service handles both entities     | Simple to implement                    | Hard to scale, difficult to test   | ❌ Rejected |
| Microservices + Saga + RabbitMQ     | Distributed, decoupled services          | Scalable, resilient, atomic creation  | Higher operational complexity      | ✅ Selected |

---

## Step 5 - Instantiate Elements, Allocate Responsibilities, Define Interfaces

### 5.1 Main Components

| Component                          | Responsibility                                      | Interface/Methods                             |
|-----------------------------------|----------------------------------------------------|-----------------------------------------------|
| API Gateway                        | Receives creation requests                          | POST /readers                                  |
| ReaderService                       | CRUD for Reader entity                              | createReader(), getReader(), updateReader()   |
| UserService                         | CRUD for User entity                                | createUser(), getUser(), updateUser()         |
| ReaderUserCreationCoordinatorService| Orchestrates atomic creation of Reader + User      | createReaderWithUser(), compensate()          |
| RabbitMQ Message Broker             | Publishes async events for notifications/workflow  | publishEvent(), subscribeEvent()              |
| CacheLayer (optional)               | Temporary enrollment state storage                  | get(), put(), invalidate()                     |

---

## Step 6 - Sketch Views and Record Decisions

### 6.1 Trade-Off Analysis

| Decision                                | Benefit                                     | Cost                                     |
|-----------------------------------------|--------------------------------------------|-----------------------------------------|
| Microservices + Saga + RabbitMQ          | Atomic creation, scalable, resilient       | Operational complexity                    |
| Event-driven messaging via RabbitMQ      | Decouples services, reliable notifications | Added latency and complexity             |
| Temporary caching                         | Reduces repeated validations               | Eventual consistency possible           |

### 6.2 Outcome

- Atomic creation of Reader + User.
- Independent, scalable services with failure isolation.
- RabbitMQ enables decoupled asynchronous communication.
- Extensible for future identity provider integrations.

---

## Step 7 - Perform Analysis of Current Design and Review Iteration Goals

- Transform separate creation processes into a unified, distributed workflow.
- Ensure atomicity and consistency for high-concurrency requests.
- Services communicate asynchronously via RabbitMQ.
- Observability, caching, and fallback mechanisms implemented.

**Result:** Reliable, distributed, and scalable workflow for creating a Reader and its respective User atomically, using RabbitMQ for messaging, ensuring consistency and system integrity.
