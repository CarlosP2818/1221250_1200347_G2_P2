# ADD 5 - Polyglot Data Persistence

*Attribute-Driven Design (ADD) Full Documentation*

---

## 1. Overview

This document presents the ADD for **Polyglot Data Persistence** in the LMS distributed architecture.

The scenario outlines the requirements and design considerations for **using multiple database types** according to service domain:

- **Objective:** Ensure each microservice uses the most suitable database technology, supporting availability, performance, and scalability.
- **Functionalities:** CRUD operations for books, authors, genres, students/readers, users, comments, and grades.
- **Extensibility:** Future services can select their optimal DB technology.
- **Performance:** Queries optimized per database type.
- **Availability:** Database failures are isolated to the affected service.
- **Compatibility:** APIs remain unchanged for clients.

---

## Step 1. Review Inputs

### 1.1 Objective

- Assign databases based on service domain:
    - **MongoDB:** StudentService (flexible schema for comments, grades, activity logs).
    - **SQL Database (PostgreSQL/MySQL):** BookService, AuthorService, GenreService, UserService (structured relational data).
- Support **scalable, independent, and resilient microservices**.
- Maintain **eventual consistency** for cross-service operations.

### 1.2 Problem Statement

- Single database limits scalability and flexibility.
- Different services have different data patterns.
- Cross-service consistency must be managed carefully when databases are heterogeneous.

### 1.3 Requirements (SMART)

| Type           | Requirement                                                                                       |
|----------------|---------------------------------------------------------------------------------------------------|
| **Specific**   | Use MongoDB for StudentService; SQL for Book/Author/Genre/UserService.                             |
| **Measurable** | Query performance <100ms for high-demand operations; integration tests validate consistency.       |
| **Attainable** | Implementable using per-service DB, messaging, and Sagas/Outbox for cross-service consistency.    |
| **Relevant**   | Ensures performance, availability, maintainability, and scalability.                               |
| **Time-Bound** | Implementation within the current sprint/version.                                                 |

**Variation and Evolution Points**

- **Variation points:** Database type per service, data model evolution.
- **Evolution points:** New services can select SQL, MongoDB, or other databases independently.

---

## Step 2: Establish Goals and Select Inputs

### 2.1 Drivers

| Type             | Driver                                                                                       |
|-----------------|----------------------------------------------------------------------------------------------|
| **Functional**  | CRUD operations for all services using optimal DB type.                                       |
| **Quality**     | Availability, Performance, Scalability, Maintainability.                                      |
| **Constraints** | Each service must own its DB; cross-database operations use messaging/Sagas.                  |
| **Business**    | Reduce downtime and improve user experience; future services may adopt different DBs.        |

### 2.2 Design Decisions

- **Database per Service:** Each service manages its own DB.
- **MongoDB:** Used for StudentService (flexible schema for comments, grading, and activity logs).
- **SQL:** Used for Book/Author/Genre/UserService (structured relational data).
- **Cross-Service Consistency:** Sagas + Outbox pattern via message broker.
- **Caching:** Redis for frequently accessed data.
- **Resilience:** Circuit breakers and retries on DB failures.

---

## Step 3 - Choose Elements to Decompose

### 3.1 Selected Elements

- **BookService / AuthorService / GenreService / UserService:** SQL database.
- **StudentService:** MongoDB.
- **Message Broker:** Asynchronous communication for multi-service operations.
- **Orchestrator / Load Balancer:** Manages service instances independently.
- **Cache Layer:** Redis to reduce load during peak demand.

### 3.2 Rationale for Selection

- SQL ensures **data integrity** for relational entities.
- MongoDB provides **flexibility** for dynamic and semi-structured data (student comments, grades).
- Database per service isolates failures, improving **availability**.

---

## Step 4 - Choose Design Concepts

### 4.1 Tactics

| Quality Attribute      | Tactic                             | Description                                                      | Implementation                                                    |
|------------------------|------------------------------------|------------------------------------------------------------------|------------------------------------------------------------------|
| **Availability**       | Component Isolation                | DB failure in one service does not affect others.                 | Each service owns its DB; circuit breaker; auto-recovery.        |
| **Performance**        | Polyglot Persistence               | DB type optimized per service domain.                             | MongoDB for StudentService, SQL for structured entities.         |
| **Scalability**        | Horizontal Scaling                 | Each DB scaled independently.                                     | Replica sets (MongoDB), read replicas (SQL).                     |
| **Maintainability**    | Modularization                     | Services encapsulate their DBs.                                    | Independent service + DB per domain.                              |
| **Consistency**        | Sagas / Outbox                     | Eventual consistency across heterogeneous DBs.                    | Async messaging via broker; transactional outbox.                |

### 4.2 Reference Architecture / Patterns

- **Database per Service**
- **Polyglot Persistence** (MongoDB + SQL)
- **Event-Driven / Messaging** (Sagas, Outbox)
- **Cache Layer** for performance optimization

### 4.3 Patterns

- Circuit Breaker / Retry
- Database per Service
- Sagas / Outbox for consistency

### 4.4 Architectural Design Alternatives and Rationale

| Alternative                        | Description                                 | Advantages                       | Disadvantages                 | Decision    |
|-----------------------------------|--------------------------------------------|---------------------------------|-------------------------------|-------------|
| Single SQL Database                | All services share relational DB            | Simple operations               | Bottleneck, poor flexibility  | ❌ Rejected |
| Single MongoDB                     | All services share document DB              | Flexible schema                  | Poor integrity for structured entities | ❌ Rejected |
| Polyglot DB per service            | Services choose optimal DB per domain       | Performance, scalability, availability | Operational complexity         | ✅ Selected |

---

## Step 5 - Instantiate Elements, Allocate Responsibilities, Define Interfaces

| Component        | Responsibility                                | Interface/Methods                        | DB Type |
|-----------------|-----------------------------------------------|-----------------------------------------|---------|
| BookService      | CRUD for books                                | createBook(), getBook(), updateBook()   | SQL     |
| AuthorService    | CRUD for authors                              | createAuthor(), getAuthor(), updateAuthor() | SQL     |
| GenreService     | CRUD for genres                               | createGenre(), getGenre(), updateGenre() | SQL     |
| UserService      | CRUD for users                                | createUser(), getUser(), updateUser()   | SQL     |
| StudentService   | CRUD for students, comments, grades          | createStudent(), addComment(), addGrade() | MongoDB |
| Message Broker   | Async coordination for multi-service actions | publishEvent(), subscribeEvent()       | N/A     |
| Cache Layer      | Reduce DB load                                | get(), put(), invalidate()             | Redis   |

---

## Step 6 - Sketch Views and Record Decisions

| Decision                    | Benefit                                    | Cost                         |
|-----------------------------|--------------------------------------------|------------------------------|
| Polyglot Persistence         | Optimal DB for each service domain        | Operational complexity       |
| Database per service         | Isolate failures, enable independent scaling | More complex cross-service transactions |
| Sagas / Outbox               | Eventual consistency across heterogeneous DBs | Added latency and messaging complexity |

### 6.2 Outcome

- Each service uses the DB best suited to its domain.
- StudentService: MongoDB for flexible schema (comments, grading).
- Book/Author/Genre/UserService: SQL for relational consistency.
- High availability, scalability, and maintainability achieved.

---

## Step 7 - Perform Analysis of Current Design and Review Iteration Goals

- Polyglot DB ensures **availability and performance** per service.
- Cross-service consistency managed via **Sagas/Outbox** and async messaging.
- Caching reduces load and improves response times.
- New services can adopt SQL, MongoDB, or other DBs independently.

**Result:** LMS achieves **Polyglot Data Persistence**, balancing **availability, performance, scalability, and maintainability**.
