# QAS 2 - Student A: Reader and User Creation

*Attribute-Driven Design (ADD) Full Documentation*

---

## 1. Overview

This document presents the *Attribute-Driven Design (ADD)* for the *Quality Attribute Scenario (QAS) 5 - "Reader and User Creation"*.

The scenario addresses the librarian requirement:

> *As a librarian, I want to create a Reader and the respective User in the same request.*

Key objectives:

- **Unified creation:** Both `Reader` and its associated `User` are created atomically.
- **Consistency:** Either both entities are created, or none.
- **Extensibility:** Support future identity integrations (SSO, OAuth, LDAP).
- **Resilience:** Handle failures in either service without partial creation.
- **Performance:** Efficient under high-concurrency requests.
- **Asynchronous Communication:** Use RabbitMQ for decoupled workflow and notifications.

---

## Step 1. Review Inputs

### 1.1 Objective

- Implement a single endpoint for creating `Reader` and the associated `User`.
- Ensure atomicity and data consistency.
- Leverage RabbitMQ for asynchronous messaging and decoupled notifications.

### 1.2 Problem Statement

Currently, creating a `Reader` and a `User` separately can result in:

- **Inconsistencies:** `Reader` may exist without a `User`, or vice versa.
- **Tight coupling:** Services depend directly on each other.
- **Limited scalability:** Cannot efficiently handle high-volume creation requests.

---

### 1.3 Requirements (SMART)

| Type           | Requirement                                                                                   |
|----------------|-----------------------------------------------------------------------------------------------|
| **Specific**   | Create `Reader` and `User` atomically in a single request.                                     |
| **Measurable** | Must succeed for X concurrent requests with <Y ms latency.                                     |
| **Attainable** | Implementable using microservices, orchestration patterns, and RabbitMQ messaging.             |
| **Relevant**   | Critical for librarian operations and data integrity.                                         |
| **Time-Bound** | Implementation planned for the upcoming sprint.                                                |

**Variation and Evolution Points**

- **Variation points:**
    - Future identity providers (OAuth, SSO, LDAP).
    - Additional reader-related entities (membership, subscriptions).

- **Evolution points:**
    - New validation rules or workflow steps can be added without impacting current services.

---

## Step 2: Establish Goals and Select Inputs

### 2.1 Drivers

| Type             | Driver                                                                                      |
|-----------------|---------------------------------------------------------------------------------------------|
| **Functional**  | Unified creation of Reader and User in one request.                                          |
| **Quality**     | Availability, Reliability, Consistency, Scalability, Testability.                            |
| **Constraints** | Decoupled services; backward-compatible API; use RabbitMQ for async communication.           |
| **Business**    | Essential for efficient onboarding of library users.                                        |

### 2.2 Design Decisions

- **Microservices:** `ReaderService` and `UserService`.
- **Orchestration:** `ReaderUserCreationCoordinatorService` ensures atomic creation.
- **Transaction Pattern:** Saga pattern for distributed transaction consistency.
- **Resilience:** Retry, circuit breaker, fallback mechanisms.
- **Messaging:** RabbitMQ for asynchronous events (e.g., enrollment completion notifications).
- **Caching (optional):** Temporary cache for high-concurrency requests.

---

## Step 3 - Choose Elements to Decompose

### 3.1 Selected Elements

- **ReaderService:** Handles CRUD for `Reader`.
- **UserService:** Handles CRUD for `User`.
- **ReaderUserCreationCoordinatorService:** Orchestrates atomic creation of both entities.
- **RabbitMQ Message Broker:** Handles asynchronous events for notifications and decoupled workflows.
- **CacheLayer (optional):** Redis for temporary status tracking.

### 3.2 Rationale for Selection

- Separation of concerns: Reader domain vs authentication domain.
- Coordinator ensures atomicity without tight coupling.
- RabbitMQ enables decoupled, reliable asynchronous communication.
- Supports scalability, testability, and resilience under high load.

---

## Step 4 - Choose Design Concepts

### 4.1 Tactics

| Quality Attribute      | Tactic                              | Description                                                      | Implementation                                                    |
|------------------------|------------------------------------|------------------------------------------------------------------|------------------------------------------------------------------|
| **Availability**       | Component Isolation                 | Failure in one service does not halt the workflow.               | Retry, circuit breaker, independent microservices.              |
| **Consistency**        | Distributed Transaction (Saga)      | Ensure both Reader and User are created or neither.              | Saga orchestrated by ReaderUserCreationCoordinatorService.       |
| **Performance**        | Caching                             | Reduce repeated validation or temporary state checks.            | Redis cache (optional).                                          |
| **Resilience**         | Retry / Circuit Breaker              | Handle transient failures in UserService.                        | Retry policy + fallback response.                                |
| **Scalability**        | Microservice Replication             | Each service scales independently under load.                    | Kubernetes/Docker auto-scaling.                                  |
| **Testability**        | Isolation / Mocking                 | Unit and integration tests without external dependencies.        | Mock ReaderService, UserService, and RabbitMQ messages.           |

### 4.2 Reference Architecture / Patterns

- **Saga Pattern:** Orchestrates distributed transaction for Reader + User creation.
- **Microservices:** ReaderService and UserService.
- **API Gateway:** Unified entry point for librarian requests.
- **Event-driven Architecture:** RabbitMQ for asynchronous communication and notifications.

### 4.3 Patterns

- **Coordinator Pattern (Saga):** Ensures atomic workflow.
- **Circuit Breaker / Retry:** Resilient service communication.
- **RabbitMQ:** Decouples services and enables asynchronous events.

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
| Microservices + Saga + RabbitMQ          | Ensures atomic creation, scalable, resilient | More operational complexity             |
| Event-driven messaging via RabbitMQ      | Decouples services, reliable notifications | Added latency and setup complexity      |
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
- Services communicate asynchronously via RabbitMQ to reduce coupling.
- Observability, caching, and fallback mechanisms implemented.

**Result:** Reliable, distributed, and scalable workflow for creating a Reader and its respective User atomically, using RabbitMQ for messaging, ensuring consistency and system integrity.
