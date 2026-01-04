- # System Architecture Documentation
  
- System
  - Level 1
    - [Logical View](Diagrams/Level1/LogicalView/Logical-View-Diagram-Level1.png)
  - Level 2
    - [Logical View](Diagrams/Level2/LogicalView/Logical-View-Diagram-Level2.png)
    - [Physical View](Diagrams/Level2/PhysicalView/Physical-View-Diagram-Level2.png)
    - [Implementation View](Diagrams/Level2/ImplementationView/Implementation-View-Diagram-Level2.png)
  - Level 3
    - [Logical View](Diagrams/Level3/LogicalView/Logical-View-Diagram-Level3.png)
    - [Implementation View](Diagrams/Level3/ImplementationView/Implementation-View-Diagram-Level3.png)
    - [Mapping Between Logical and Implementation Views](Diagram/Level3/MappingView/Mapping-View-Diagram-Level3.svg)

- Quality Attribute Scenarios (QAS)
    - [QAS1](ADD/QAS1/QAS_1.md)
    - [QAS2](ADD/QAS2/QAS_2.md)
    - [QAS3](ADD/QAS3/QAS_3.md)
    - [QAS4](ADD/QAS4/QAS_4.md)
    - [QAS5](ADD/QAS5/QAS_5.md)
    - [QAS6](ADD/QAS6/QAS_6.md)
    - [QAS7](ADD/QAS6/QAS_7.md)
    - [QAS8](ADD/QAS6/QAS_8.md)
    - [QAS9](ADD/QAS6/QAS_9.md)
    - [QAS10](ADD/QAS6/QAS_10.md)
    - [QAS11](ADD/QAS6/QAS_11.md)
    - [QAS12](ADD/QAS6/QAS_12.md)
    - [QAS13](ADD/QAS6/QAS_13.md)
    - [QAS14](ADD/QAS6/QAS_14.md)
    - [QAS15](ADD/QAS6/QAS_15.md)
    - [QAS16](ADD/QAS6/QAS_16.md)
    - [QAS17](ADD/QAS6/QAS_17.md)
- TechnicalMemos
    - [Memo_QAS1](ADD/TechnicalMemos/Memo_QAS1.md)
    - [Memo_QAS2](ADD/TechnicalMemos/Memo_QAS2.md)
    - [Memo_QAS3](ADD/TechnicalMemos/Memo_QAS3.md)
    - [Memo_QAS4](ADD/TechnicalMemos/Memo_QAS4.md)
    - [Memo_QAS5](ADD/TechnicalMemos/Memo_QAS5.md)
    - [Memo_QAS6](ADD/TechnicalMemos/Memo_QAS6.md)
    - [Memo_QAS7](ADD/TechnicalMemos/Memo_QAS7.md)
    - [Memo_QAS8](ADD/TechnicalMemos/Memo_QAS8.md)
    - [Memo_QAS9](ADD/TechnicalMemos/Memo_QAS9.md)
    - [Memo_QAS10](ADD/TechnicalMemos/Memo_QAS10.md)
    - [Memo_QAS11](ADD/TechnicalMemos/Memo_QAS11.md)
    - [Memo_QAS12](ADD/TechnicalMemos/Memo_QAS12.md)
    - [Memo_QAS13](ADD/TechnicalMemos/Memo_QAS13.md)
    - [Memo_QAS14](ADD/TechnicalMemos/Memo_QAS14.md)
    - [Memo_QAS15](ADD/TechnicalMemos/Memo_QAS15.md)
    - [Memo_QAS16](ADD/TechnicalMemos/Memo_QAS16.md)
    - [Memo_QAS17](ADD/TechnicalMemos/Memo_QAS17.md)

- Saga Process Diagrams
  - [Saga Process](saga-process.puml)

### QAS 1 - LMS Reengineering

#### 1.1 Quality Attribute Scenario

| Element           |                                                                                                                                             |
|-------------------|---------------------------------------------------------------------------------------------------------------------------------------------|
| Stimulus          | User requests creation of entities (Book, Author, Genre, Reader, User) or performs operations under high load (>Y requests/period).         |
| Stimulus Source   | Librarian, system components, or external client requests.                                                                                 |
| Environment       | Distributed LMS system deployed via Docker/Kubernetes, with API Gateway, service orchestration, and messaging where applicable.            |
| Artifact          | Microservices: `BookService`, `AuthorService`, `GenreService`, `ReaderService`, `UserService`; API Gateway; Cache Layer; Message Broker.    |
| Response          | Services process requests independently, coordinate via API Gateway or messages, and provide consistent and successful responses.          |
| Response measures | All requested entities are created successfully; system maintains high availability; response times meet performance targets.               |

- **QAS Detailed:** [ADD #1 – LMS Reengineering](QAS1/QAS_1.md)

### QAS 2 - Reader and User Creation

#### 2.1 Quality Attribute Scenario

| Element           |                                                                                                                                                     |
|-------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------|
| Stimulus          | Librarian requests creation of a `Reader` and the associated `User` in a single operation, possibly under high-concurrency conditions.              |
| Stimulus Source   | Librarian or system component initiating the creation.                                                                                               |
| Environment       | Distributed LMS system with ReaderService, UserService, API Gateway, and RabbitMQ for messaging; optional caching for temporary state management.  |
| Artifact          | Microservices: `ReaderService`, `UserService`, `ReaderUserCreationCoordinatorService`; RabbitMQ Message Broker; optional Cache Layer.              |
| Response          | Both entities are created atomically, with Saga orchestration ensuring consistency; asynchronous events are published for notifications/workflow. |
| Response measures | `Reader` and `User` are either both created or none; system maintains consistency under high concurrency and failures are isolated and handled.    |

- **QAS Detailed:** [ADD #2 – Reader and User Creation](QAS2/QAS_2.md)

### QAS 3 - Reader and User Creation (Student B)

#### 3.1 Quality Attribute Scenario

| Element           |                                                                                                                                                     |
|-------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------|
| Stimulus          | Librarian requests creation of a `Reader` and the associated `User` in a single operation, possibly under high-concurrency conditions.              |
| Stimulus Source   | Librarian or system component initiating the creation.                                                                                               |
| Environment       | Distributed LMS system with ReaderService, UserService, API Gateway, and RabbitMQ for messaging; optional caching for temporary state management.  |
| Artifact          | Microservices: `ReaderService`, `UserService`, `ReaderUserCreationCoordinatorService`; RabbitMQ Message Broker; optional Cache Layer.              |
| Response          | Both entities are created atomically, with Saga orchestration ensuring consistency; asynchronous events are published for notifications/workflow. |
| Response measures | `Reader` and `User` are either both created or none; system maintains consistency under high concurrency and failures are isolated and handled.    |

- **QAS Detailed:** [ADD #3 – Reader and User Creation (Student B)](QAS3/QAS_3.md)

### QAS 4 - Availability

#### 4.1 Quality Attribute Scenario

| Element           |                                                                                                                                                     |
|-------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------|
| Stimulus          | A service instance fails due to hardware, network, or software issues.                                                                               |
| Stimulus Source   | Internal system failure or external infrastructure fault.                                                                                             |
| Environment       | Distributed LMS microservices with Kubernetes orchestration, API Gateway, load balancer, circuit breakers, and optional Redis cache.               |
| Artifact          | Independent microservices, API Gateway, Load Balancer, Orchestrator (Kubernetes), Circuit Breakers, Distributed Cache.                             |
| Response          | Traffic is rerouted, failed instances are restarted, circuit breakers prevent cascading failures, and degraded responses are served gracefully.      |
| Response measures | System uptime ≥ 99.9%; failed services recover within <30 seconds; minimal client impact during failures.                                          |

- **QAS Detailed:** [ADD #4 – Availability](QAS4/QAS_4.md)

### QAS 5 - Polyglot Data Persistence

#### 5.1 Quality Attribute Scenario

| Element           |                                                                                                                                                         |
|-------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------|
| Stimulus          | Microservice requires data storage optimized for its domain (structured relational data vs flexible semi-structured data).                              |
| Stimulus Source   | BookService, AuthorService, GenreService, UserService, StudentService.                                                                                  |
| Environment       | Distributed LMS microservices, SQL DBs, MongoDB, Redis cache, message broker for async coordination (Sagas/Outbox).                                     |
| Artifact          | SQL databases for relational services, MongoDB for StudentService, Message Broker, Redis Cache.                                                         |
| Response          | Service interacts with its optimal DB; cross-service consistency achieved via async messaging; failures isolated to service DB.                          |
| Response measures | High availability per service; query performance <100ms; eventual consistency across services validated via integration tests.                          |

- **QAS Detailed:** [ADD #5 – Polyglot Data Persistence](QAS5/QAS_5.md)

### QAS 6 - Fault Tolerance / Health Monitoring

#### 6.1 Quality Attribute Scenario

| Element           |                                                                                                                                              |
|-------------------|----------------------------------------------------------------------------------------------------------------------------------------------|
| Stimulus          | Service failure occurs due to hardware, network, or software error.                                                                         |
| Stimulus Source   | Any LMS microservice (BookService, AuthorService, GenreService, ReaderService, UserService).                                               |
| Environment       | Distributed LMS microservices, container orchestration (Kubernetes/Docker Swarm), monitoring tools (Prometheus/Grafana), circuit breakers. |
| Artifact          | Health Check endpoints, Orchestrator, Circuit Breakers, Monitoring Dashboard, Alerting System.                                             |
| Response          | Failed service is detected automatically, circuit breakers prevent propagation, service is restarted or rerouted, operators notified.       |
| Response measures | Uptime ≥ 99.9%; service restarted automatically within acceptable time; failures contained; proactive monitoring alerts operators.          |

- **QAS Detailed:** [ADD #6 – Fault Tolerance / Health Monitoring](QAS6/QAS_6.md)

### QAS 7 - Independent Deployment / Automatic Deployment

#### 7.1 Quality Attribute Scenario

| Element           |                                                                                                                                      |
|-------------------|--------------------------------------------------------------------------------------------------------------------------------------|
| Stimulus          | New version of a microservice is ready for deployment.                                                                              |
| Stimulus Source   | Development/DevOps team pushing updates via CI/CD pipeline.                                                                          |
| Environment       | Distributed LMS microservices, container orchestration (Kubernetes/Docker Swarm), CI/CD pipelines, monitoring tools.                |
| Artifact          | Microservice containers, CI/CD pipeline, Orchestrator, Deployment Strategy Config, Health Checks.                                   |
| Response          | Service is deployed independently; health is verified; traffic routed to new version; rollback occurs if failure detected.          |
| Response measures | Deployment occurs without downtime; rollback on failure; health checks passed; existing services unaffected; API compatibility maintained. |

- **QAS Detailed:** [ADD #7 – Independent Deployment / Automatic Deployment](QAS7/QAS_7.md)

### QAS 8 - CI/CD Pipeline

#### 8.1 Quality Attribute Scenario

| Element           |                                                                                                               |
|-------------------|---------------------------------------------------------------------------------------------------------------|
| Stimulus          | Code changes are pushed to the repository for LMS microservices.                                             |
| Stimulus Source   | Development/DevOps teams via Git push / pull request.                                                        |
| Environment       | Dev → Staging → Production environments, CI/CD tools (Jenkins/GitHub Actions/GitLab CI), Docker registry.    |
| Artifact          | CI/CD pipeline, Test Suite, Docker images, Deployment scripts, Notifications.                                 |
| Response          | Pipeline executes all stages automatically; tests run; Docker images built and pushed; notifications sent; rollback triggered on failure. |
| Response Measures | Pipeline completes successfully; test coverage maintained; Docker images versioned; deployment succeeds without errors; failures notified and rolled back. |

- **QAS Detailed:** [ADD #8 – CI/CD Pipeline](QAS8/QAS_8.md)

### QAS 9 - Microservices Patterns Adoption

#### 9.1 Quality Attribute Scenario

| Element           |                                                                                                               |
|-------------------|---------------------------------------------------------------------------------------------------------------|
| Stimulus          | Transactional operation (e.g., create Book and update Author) across services is requested.                  |
| Stimulus Source   | LMS API / Librarian / System Integration                                                                     |
| Environment       | Distributed microservices with message broker and polyglot databases.                                        |
| Artifact          | BookService, AuthorService, ReaderService, UserService, MessageBroker, Saga Orchestrator, CQRS models.       |
| Response          | Services execute independently; events published; distributed transaction coordinated via Saga; read/write optimized via CQRS. |
| Response Measures | Operations complete atomically across services; events delivered; no downtime; consistent read/write views; system maintains availability and performance. |

- **QAS Detailed:** [ADD #9 – Microservices Patterns Adoption](QAS9/QAS_9.md)

### Elasticity / Auto-Scaling

#### 10.1 Quality Attribute Scenario

| Element           |                                                                                     |
|-------------------|-------------------------------------------------------------------------------------|
| Stimulus          | Increased or decreased traffic for LMS services                                     |
| Stimulus Source   | Users / API clients / System load                                                 |
| Environment       | Distributed LMS microservices under orchestrator (Kubernetes/Docker)               |
| Artifact          | Microservice instances, Metrics Collector, Scaling Scripts, Load Balancer           |
| Response          | Service replicas adjust automatically; traffic is routed to active instances       |
| Response Measures | Optimal resource utilization; requests handled without delay; minimum service availability maintained |

- **QAS Detailed:** [ADD #10 – Elasticity / Auto-Scaling](QAS10/QAS_10.md)

### Performance & Load Testing

#### 11.1 Quality Attribute Scenario

| Element           | Description                                                                                     |
|-------------------|-------------------------------------------------------------------------------------------------|
| Stimulus          | High or varying user/API request load on LMS services                                           |
| Stimulus Source   | Users / API clients / System load                                                              |
| Environment       | Distributed LMS microservices under orchestrator (Kubernetes/Docker)                           |
| Artifact          | Service endpoints, Load Testing Suite, Metrics Collector, Monitoring Tools, Reporting Engine    |
| Response          | System maintains performance; metrics collected; bottlenecks identified; auto-scaling triggered |
| Response Measures | Response times within SLA; throughput optimized; resources monitored; scaling behavior validated |

- **QAS Detailed:** [ADD #11 – Performance & Load Testing](QAS11/QAS_11.md)

### Release Strategy / Exposure

#### 12.1 Quality Attribute Scenario

| Element           | Description                                                                                     |
|-------------------|-------------------------------------------------------------------------------------------------|
| Stimulus          | New features or service updates ready for release                                              |
| Stimulus Source   | Development team / CI/CD pipeline / product management                                         |
| Environment       | Distributed LMS microservices under orchestrator (Kubernetes/Docker)                          |
| Artifact          | Feature Flag Service, Traffic Router / Gateway, CI/CD pipeline, Monitoring & Metrics, Rollback Mechanism |
| Response          | Features are progressively exposed; internal validation occurs; rapid rollback possible        |
| Response Measures | Controlled adoption percentage; minimal user impact; errors detected and reverted; metrics collected |

- **QAS Detailed:** [ADD #12 – Release Strategy / Exposure](QAS12/QAS_12.md)

### Infrastructure as Code / Reproducibility

#### 13.1 Quality Attribute Scenario

| Element           | Description                                                                                     |
|-------------------|-------------------------------------------------------------------------------------------------|
| Stimulus          | Need to provision or replicate LMS environments across dev, staging, or production             |
| Stimulus Source   | Development team, CI/CD pipeline, or operations                                               |
| Environment       | Distributed LMS microservices deployed using Docker/Compose/Swarm                               |
| Artifact          | Dockerfile, Compose/Swarm manifests, environment configuration, CI/CD scripts                  |
| Response          | Environments are provisioned consistently and reproducibly; deployments automated               |
| Response Measures | Deployment time <5 min; consistent environment state; traceable version-controlled infrastructure |

- **QAS Detailed:** [ADD #13 – Infrastructure as Code / Reproducibility](QAS13/QAS_13.md)

### Deployment & Rollout Strategy

#### 14.1 Quality Attribute Scenario

| Element           | Description                                                                                     |
|-------------------|-------------------------------------------------------------------------------------------------|
| Stimulus          | Deployment of new LMS service versions                                                          |
| Stimulus Source   | CI/CD pipeline, development team, or operations                                                 |
| Environment       | Distributed LMS microservices under orchestrated deployment                                     |
| Artifact          | Service containers, load balancer, feature flags, monitoring dashboards, rollback scripts      |
| Response          | Services updated progressively; traffic routed carefully; automatic rollback if failure occurs |
| Response Measures | Zero downtime; controlled exposure to users; rollback executed automatically on errors; metrics monitored |

- **QAS Detailed:** [ADD #14 – Deployment & Rollout Strategy](QAS14/QAS_14.md)

### Interoperability

#### 15.1 Quality Attribute Scenario

| Element           | Description                                                                                     |
|-------------------|-------------------------------------------------------------------------------------------------|
| Stimulus          | Integration request from external or internal system                                             |
| Stimulus Source   | Other LMS services, third-party systems, or event consumers                                     |
| Environment       | Distributed LMS microservices with synchronous REST and asynchronous message communication     |
| Artifact          | API Gateway, REST endpoints, message broker, CDC tests                                          |
| Response          | Requests and events are handled according to standardized contracts; validated via CDC tests   |
| Response Measures | Correct response data; events consumed/published as per contracts; integration is reliable     |

- **QAS Detailed:** [ADD #15 – Interoperability](QAS15/QAS_15.md)

### Operability

#### 16.1 Quality Attribute Scenario

| Element           | Description                                                                                     |
|-------------------|-------------------------------------------------------------------------------------------------|
| Stimulus          | Service failure, performance degradation, high traffic                                          |
| Stimulus Source   | Users, system load, internal/external events                                                   |
| Environment       | Distributed LMS microservices under container orchestration                                     |
| Artifact          | Logs, metrics, dashboards, health endpoints, alerting system                                   |
| Response          | Alerts triggered automatically; dashboards updated; health endpoints reflect service status   |
| Response Measures | Failures detected within 5 min; real-time visibility of metrics; proactive operational actions |

- **QAS Detailed:** [ADD #16 – Operability](QAS16/QAS_16.md)

### Testability

#### 17.1 Quality Attribute Scenario

| Element           | Description                                                                                     |
|-------------------|-------------------------------------------------------------------------------------------------|
| Stimulus          | Code changes, new feature deployments, increased traffic                                        |
| Stimulus Source   | Developers, CI/CD pipeline, user interactions                                                  |
| Environment       | LMS microservices under test environments with CI/CD orchestration                               |
| Artifact          | Service classes, mocks/stubs, CDC tests, integration tests, load tests                          |
| Response          | Automated tests run; integration/contract issues detected; performance validated               |
| Response Measures | Test success/failure reports; contracts verified; performance metrics recorded; regressions prevented |

- **QAS Detailed:** [ADD #17 – Testability](QAS17/QAS_17.md)

# Adoption of Microservices Patterns

This section documents the **explicit adoption and justification** of microservices patterns in the LMS reengineering project. Each pattern is linked to **observed requirements, quality attributes, and design decisions**.

---

## 1. Strangler Fig

**Usage:** Gradual replacement of the monolithic LMS with microservices.  
**Justification:**
- Allows **incremental migration** without system downtime.
- Critical to **minimize risk** while modernizing legacy services.
- Supports **service-by-service extraction** for Books, Authors, Genres, Readers, and Users.

---

## 2. Events & Messaging

**Usage:** Event-driven communication using **RabbitMQ** for asynchronous integration.  
**Justification:**
- Decouples services for **independent evolution**.
- Enables **reactive workflows** and reliable inter-service notifications (e.g., when a book is added).
- Improves **scalability and resilience**, avoiding synchronous bottlenecks.

---

## 3. CQRS (Command Query Responsibility Segregation)

**Usage:** Separate models for **commands (writes)** and **queries (reads)**.  
**Justification:**
- Optimizes **read-heavy LMS operations** like listing books or genres.
- Allows **different scaling strategies** for write vs read workloads.
- Supports **eventual consistency** in distributed systems.

---

## 4. Database-per-Service

**Usage:** Each microservice owns its own database according docker to its domain.

**Justification:**
- Ensures **service autonomy** and **data encapsulation**.
- Prevents **cross-service locking** and tight coupling.
- Enables **independent scaling and backup strategies**.

---

## 5. Polyglot Persistence

**Usage:** Choice of database technology based on service needs.  
**Justification:**
- Books, Authors, and Genres → **Relational (PostgreSQL)** for structured data.
- Events, Logs → **NoSQL (MongoDB)** for high write throughput.
- Improves **performance and efficiency** by using the best storage type per service.

---

## 6. Outbox Pattern

**Usage:** Persist events within the **transactional boundary** of service updates.  
**Justification:**
- Ensures **reliable event publication** even if messaging broker fails.
- Prevents **lost or duplicate messages**, guaranteeing consistency between services.

---

## 7. Saga Pattern

**Usage:** Implement **long-running, distributed transactions** via choreography.  
**Justification:**
- Manages **multi-service workflows** (e.g., user registration triggers profile creation and subscription setup).
- Maintains **eventual consistency** without global transactions.
- Supports **compensation logic** to rollback actions in case of failure.

---

### Summary

The LMS microservices architecture explicitly adopts **modern microservices patterns** to address:

- **Scalability & Performance:** Event-driven messaging, CQRS, database-per-service.
- **Resilience & Fault Tolerance:** Saga, Outbox, message broker.
- **Maintainability & Extensibility:** Strangler Fig, polyglot persistence, service autonomy.

These patterns are **not applied arbitrarily**; each is chosen to meet **specific system requirements and quality attributes**, ensuring a robust, scalable, and maintainable LMS microservices ecosystem.
