# ADD 9 – Microservices Patterns Adoption

*Attribute-Driven Design (ADD) Full Documentation*

---

## 1. Overview

This ADD focuses on the **explicit adoption of microservices patterns** in the LMS reengineering project. The goal is to justify and implement patterns that improve **availability, scalability, performance, and maintainability**.

- **Objective:** Adopt and justify patterns like **Strangler Fig, Events, Messaging, CQRS, Database-per-Service, Polyglot Persistence, Outbox, and Saga**.
- **Functional Scope:** Support transactional operations (Book/Author/Genre, Reader/User) and distributed workflows.
- **Quality Attributes Addressed:** Availability, Performance, Scalability, Maintainability.
- **Compatibility:** Patterns should maintain API contracts for existing clients.
- **Extensibility:** Patterns facilitate future integrations and evolution.

---

## Step 1. Review Inputs

### 1.1 Objective

- Decouple monolithic LMS into **independent microservices**.
- Ensure **resilient event-driven communication**.
- Support **distributed transactions** across services.
- Use **polyglot persistence** for domain-specific databases (e.g., Mongo for Students, SQL for Books).

### 1.2 Problem Statement

- Current monolithic LMS limits **scalability, availability, and maintainability**.
- Changes to one module affect the entire system.
- Transactional operations across domains are **hard to coordinate** without a pattern.
- Lack of asynchronous messaging reduces elasticity and performance.

### 1.3 Requirements (SMART)

| Type           | Requirement                                                                                   |
|----------------|-----------------------------------------------------------------------------------------------|
| **Specific**   | Adopt microservices patterns to improve distributed operation and maintainability.           |
| **Measurable** | Validate patterns through automated tests, event contracts, and load testing.                |
| **Attainable** | Implementable using modern frameworks, message brokers, orchestration libraries, and DBs.    |
| **Relevant**   | Essential for reengineering LMS into a distributed architecture.                             |
| **Time-Bound** | Implemented in the current sprint/project.                                                   |

**Variation and Evolution Points**

- **Variation points:** Choice of messaging broker, database technology, orchestration framework.
- **Evolution points:** New services and domains can adopt patterns without changing existing services.

---

## Step 2: Establish Goals and Select Inputs

### 2.1 Drivers

| Type             | Driver                                                                                      |
|-----------------|---------------------------------------------------------------------------------------------|
| **Functional**  | Distributed transactional operations (Book/Author/Genre, Reader/User).                     |
| **Quality**     | Availability, Performance, Scalability, Maintainability.                                     |
| **Constraints** | Existing clients must continue functioning; peak loads may occur sporadically.              |
| **Business**    | Ensure LMS operations continue reliably under heavy load and future growth.                 |

### 2.2 Design Decisions

- **Architecture:** Independent microservices per domain entity.
- **Orchestration:** Services communicate asynchronously via **message broker** (Kafka/RabbitMQ).
- **Transactions:** Use **Saga pattern** for distributed atomic operations.
- **CQRS:** Separate read and write models for performance-sensitive services.
- **Database-per-Service:** Each service manages its own database (Mongo for Student, SQL for Book).
- **Strangler Fig:** Gradually migrate monolith to microservices without downtime.
- **Outbox Pattern:** Ensure event consistency between DB transactions and messaging.

---

## Step 3 – Choose Elements to Decompose

### 3.1 Selected Elements

| Element                | Purpose                                                      |
|------------------------|--------------------------------------------------------------|
| BookService            | Handles CRUD and transactions for Books                     |
| AuthorService          | CRUD and transactional operations for Authors               |
| GenreService           | CRUD and transactional operations for Genres                |
| ReaderService          | CRUD for Readers                                            |
| UserService            | CRUD for Users                                              |
| MessageBroker          | Asynchronous event-driven communication                     |
| Saga Orchestrator      | Coordinates multi-service transactions                      |
| CQRS Read/Write Models | Separate command and query paths for high-performance needs |

### 3.2 Rationale for Selection

- Reduces coupling and increases **availability**.
- Supports **scalable transaction workflows**.
- Facilitates **future evolution** and integration.

---

## Step 4 – Choose Design Concepts

### 4.1 Tactics

| Quality Attribute      | Tactic                      | Description                                          | Implementation                                              |
|------------------------|-----------------------------|------------------------------------------------------|------------------------------------------------------------|
| Availability           | Component Isolation         | Failure in one service does not affect others       | Independent services, circuit breakers, retries           |
| Scalability            | Service Replication         | Services can be scaled independently                | Docker/Kubernetes horizontal scaling                       |
| Performance            | CQRS / Event-driven         | Reduce bottlenecks, asynchronous processing        | Separate read/write models, Kafka/RabbitMQ events         |
| Maintainability        | Modularization              | Each service encapsulates domain and logic          | Microservice + Database per Service                        |
| Consistency            | Saga / Outbox               | Distributed transactions across services            | Saga orchestration + outbox event pattern                  |
| Testability            | Consumer Contract / Mocking | Validate event-driven interactions                  | Pact / CDC tests for services                               |

### 4.2 Reference Architecture / Patterns

- **Strangler Fig Pattern:** Gradual migration of monolith.
- **Database-per-Service:** Domain-specific DBs (Mongo vs SQL).
- **Saga Pattern:** Distributed transaction management.
- **CQRS:** Optimized read/write paths.
- **Outbox Pattern:** Reliable event publishing.
- **Event-Driven Messaging:** Decoupled service communication.

### 4.3 Architectural Design Alternatives and Rationale

| Alternative                    | Description                             | Advantages                        | Disadvantages                  | Decision    |
|--------------------------------|-----------------------------------------|-----------------------------------|--------------------------------|-------------|
| Monolithic refactor            | Modularize current system               | Low initial effort                | Performance & availability limited | ❌ Rejected |
| Microservices w/o patterns     | Split into services, no messaging       | Some decoupling                   | No transactional guarantees, lower scalability | ❌ Rejected |
| Microservices + Patterns       | Split + events, CQRS, Saga, Outbox      | High availability, scalable, resilient | Operational complexity, higher learning curve | ✅ Selected |

---

## Step 5 – Instantiate Elements, Allocate Responsibilities, Define Interfaces

| Component          | Responsibility                                             | Interface/Methods                          |
|-------------------|------------------------------------------------------------|-------------------------------------------|
| BookService        | Manage books, publish events on creation/update           | createBook(), updateBook(), deleteBook()  |
| AuthorService      | Manage authors, publish events                             | createAuthor(), updateAuthor()            |
| GenreService       | Manage genres, publish events                              | createGenre(), updateGenre()              |
| ReaderService      | Manage readers, events                                     | createReader(), updateReader()            |
| UserService        | Manage users, events                                       | createUser(), updateUser()                |
| MessageBroker      | Event transport & delivery                                  | publishEvent(), subscribeEvent()          |
| Saga Orchestrator  | Coordinate multi-service transactions                     | startSaga(), compensateSaga()             |
| CQRS Read/Write    | Serve optimized queries and commands                        | queryBooks(), queryUsers(), writeCommands() |

---

## Step 6 – Sketch Views and Record Decisions

### 6.1 Trade-Off Analysis

| Decision                            | Benefit                                     | Cost                                     |
|-------------------------------------|--------------------------------------------|-----------------------------------------|
| Event-driven messaging               | Decoupling, async processing               | Added latency, complexity                |
| Saga + Outbox                        | Distributed atomicity                        | Implementation overhead                  |
| Polyglot Persistence                 | Domain-optimized DB per service             | More DB techs to maintain                |
| CQRS                                 | Improved read/write performance             | Complexity, potential data sync delays  |
| Strangler Fig migration              | Zero downtime during transition             | Longer migration timeline                |

### 6.2 Outcome

- Independent, resilient microservices with **event-driven communication**.
- Distributed transactions coordinated via **Saga + Outbox**.
- Domain-specific databases for **polyglot persistence** (Mongo for Student, SQL for Book).
- Scalable, maintainable, and testable architecture.

---

## Step 7 – Perform Analysis of Current Design and Review Iteration Goals

- Monolithic LMS limited scalability and resilience.
- Patterns adoption solves **availability, performance, and maintainability issues**.
- Event-driven design and Saga orchestration **ensure atomic multi-service operations**.
- Architecture ready for **future integration, polyglot DBs, and peak load handling**.

**Result:** LMS is reengineered into a **distributed, pattern-driven microservices architecture**, with high availability, performance, and maintainability.
