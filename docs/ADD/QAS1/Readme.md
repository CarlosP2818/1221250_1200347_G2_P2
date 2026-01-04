# QAS 1 - LMS Reengineering

*Attribute-Driven Design (ADD) Full Documentation*

---

## 1. Overview

This document presents the full application of *Attribute-Driven Design (ADD)* for the *Quality Attribute Scenario (QAS) 2 - "LMS Reengineering"*.

The scenario outlines the requirements and design considerations for reengineering the LMS application into a **distributed/decentralized architecture**:

- **Objective:** Reengineer the LMS to improve performance, availability, and scalability.
- **Functionalities:** Unified creation of books, authors, genres, readers, and users.
- **Extensibility:** Support for future integrations with new services.
- **Performance:** +25% improvement under high demand.
- **Availability:** Reduce impact of centralized failures.
- **Compatibility:** Existing clients should be minimally affected.

---

## Step 1. Review Inputs

### 1.1 Objective

- Transform the monolithic LMS into a distributed architecture.
- Improve **performance, availability, scalability, and elasticity**.
- Ensure existing clients continue to function normally.
- Follow the company’s SOA/API-led connectivity strategy.

### 1.2 Problem Statement

The current LMS architecture, while functional, is monolithic, which limits:

- **Availability:** Failure in one module may affect the entire system.
- **Performance:** High demand peaks (>Y requests/period) degrade response times.
- **Scalability:** Difficult to allocate resources granularly.
- **Flexibility:** Changes impact clients and APIs.

---

### 1.3 Requirements (SMART)

| Type           | Requirement                                                                                   |
|----------------|-----------------------------------------------------------------------------------------------|
| **Specific**   | Reengineer LMS to a distributed architecture with decoupled APIs.                              |
| **Measurable** | Achieve +25% performance improvement during high-demand periods; increase system availability. |
| **Attainable** | Implementable with microservices, containers, load balancers, and distributed caching.        |
| **Relevant**   | Essential to improve user experience and maintain client compatibility.                       |
| **Time-Bound** | Implementation planned for the next sprint/version of the system.                              |

**Variation and Evolution Points**

- **Variation points:**
    - Independent services for books, authors, genres, readers, and users.
    - Dynamic configuration of load balancing and caching.

- **Evolution points:**
    - New services and integrations can be added without affecting existing services.

---

## Step 2: Establish Goals and Select Inputs

### 2.1 Drivers

| Type             | Driver                                                                                      |
|-----------------|---------------------------------------------------------------------------------------------|
| **Functional**  | Unified creation of books, authors, genres, readers, and users.                              |
| **Quality**     | Availability, Performance, Scalability, Elasticity, Maintainability.                        |
| **Constraints** | Distributed architecture; compatibility with existing APIs; efficient use of hardware.      |
| **Business**    | Essential for library operations and integration with external systems.                     |

### 2.2 Design Decisions

- **Architecture:** Independent microservices (Books, Authors, Genres, Readers, Users).
- **Orchestration:** Services communicate via REST APIs or messaging (Kafka/RabbitMQ).
- **Scalability:** Each service can be scaled independently.
- **Distributed Cache:** Reduces latency and load during peak demand.
- **Fallback & Resilience:** Circuit breaker, retry policies, and fault isolation.
- **Monitoring:** Centralized logging and metrics for observability.

---

## Step 3 - Choose Elements to Decompose

### 3.1 Selected Elements

- **Independent Services:** each entity (Book, Author, Genre, Reader, User) as a microservice.
- **API Gateway:** unified entry point, routes requests to the correct services.
- **Message Broker (optional):** asynchronous communication between services.
- **Distributed Cache:** Redis or similar for frequently accessed data.
- **Per-service Database:** decoupled persistence to allow horizontal scalability.

### 3.2 Rationale for Selection

- Reduces coupling and increases availability.
- Enables scaling only the services under high load.
- Facilitates maintenance, testing, and future integrations.

---

## Step 4 - Choose Design Concepts

### 4.1 Tactics

| Quality Attribute      | Tactic                             | Description                                                      | Implementation                                                    |
|------------------------|------------------------------------|------------------------------------------------------------------|------------------------------------------------------------------|
| **Availability**       | Component Isolation                | Failures in one service do not affect others.                     | Independent services, circuit breaker, retries.                 |
| **Performance**        | Caching                            | Reduces calls to services and databases.                          | Redis or similar.                                                |
| **Scalability**        | Service Replication                | Services replicated horizontally.                                  | Kubernetes, Docker, auto-scaling.                                |
| **Elasticity**         | Dynamic Resource Allocation         | Resources allocated according to demand.                          | Autoscaling, cloud orchestration.                                |
| **Maintainability**    | Modularization                     | Independent services, easy evolution.                              | Each microservice encapsulates its own domain.                   |
| **Testability**        | Isolation / Mocking                | Facilitates unit and integration testing.                          | Mock APIs and databases per service.                              |

### 4.2 Reference Architecture / Patterns

- **API Gateway:** Single entry point, routing, authentication, and fallback.
- **Microservices:** Each domain is independent with its own DB and business logic.
- **Event-driven / Messaging:** Asynchronous communication for eventual consistency.
- **Caching Layer:** Redis for frequently accessed shared data.

### 4.3 Patterns

- **Circuit Breaker / Retry Pattern:** For service resilience.
- **Database per Service:** Avoid bottlenecks and enable horizontal scaling.
- **API Composition / Aggregator:** Combine data from multiple services in one endpoint.

### 4.4 Architectural Design Alternatives and Rationale

| Alternative                           | Description                                | Advantages                        | Disadvantages                   | Decision    |
|---------------------------------------|--------------------------------------------|-----------------------------------|--------------------------------|-------------|
| Monolithic refactoring                 | Only modularizing the current system       | Less initial effort                | Does not improve performance/availability | ❌ Rejected |
| Microservices + API Gateway            | Independent, decoupled services           | Scalable, resilient, extensible    | Higher operational complexity   | ✅ Selected |

---

## Step 5 - Instantiate Elements, Allocate Responsibilities, Define Interfaces

### 5.1 Main Components

| Component          | Responsibility                                           | Interface/Methods                      |
|-------------------|----------------------------------------------------------|---------------------------------------|
| API Gateway        | Receives requests and routes to services                | REST endpoints                          |
| BookService        | CRUD operations for books                                | createBook(), getBook(), updateBook() |
| AuthorService      | CRUD operations for authors                              | createAuthor(), ...                     |
| GenreService       | CRUD operations for genres                               | createGenre(), ...                      |
| ReaderService      | CRUD operations for readers                               | createReader(), ...                     |
| UserService        | CRUD operations for users                                 | createUser(), ...                       |
| MessageBroker      | Asynchronous communication between services             | publishEvent(), subscribeEvent()       |
| CacheLayer         | Fast storage for frequently accessed data               | get(), put(), invalidate()             |

---

## Step 6 - Sketch Views and Record Decisions

### 6.1 Trade-Off Analysis

| Decision                        | Benefit                                     | Cost                                     |
|---------------------------------|--------------------------------------------|-----------------------------------------|
| Microservices + API Gateway     | High availability, scalability             | Operational complexity, more services   |
| Distributed Cache                | Reduces latency and DB load                 | Eventual consistency possible           |
| Event-driven messaging           | Decoupling, eventual consistency            | Added latency, higher complexity        |

### 6.2 Outcome

- LMS is distributed and decoupled.
- Independent, scalable services.
- Minimal impact on existing clients.
- Ready for future integrations and peak demand scenarios.

---

## Step 7 - Perform Analysis of Current Design and Review Iteration Goals

- Transform monolithic LMS into a distributed architecture.
- Ensure availability and performance under high demand (>Y requests).
- Each microservice encapsulates its own domain and database.
- Communication via REST APIs or messaging.
- Observability, caching, and fallback mechanisms implemented.

**Result:** Scalable, distributed, and resilient LMS architecture, maintaining client compatibility and ready for future evolution.
