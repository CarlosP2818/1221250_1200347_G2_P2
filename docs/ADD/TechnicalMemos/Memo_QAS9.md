# Technical Memo – QAS 9: Microservices Patterns Adoption

## Issue
The LMS monolithic system limits **availability, scalability, and maintainability**. Distributed transactional operations are hard to coordinate, and synchronous service interactions reduce elasticity and performance.

## Problem
- Single monolith causes tight coupling; changes affect the entire system.
- Lack of **asynchronous messaging** reduces responsiveness and resilience.
- Distributed transactions are complex without a clear pattern.
- Database design is uniform, preventing domain-specific optimizations.

## Summary of Solution
Adopt microservices patterns to enable a **resilient, scalable, and maintainable LMS architecture**:

- **Strangler Fig Pattern:** Gradual migration of monolith to microservices with zero downtime.
- **Database-per-Service / Polyglot Persistence:** Each microservice manages its own DB (MongoDB for Student, SQL for Book).
- **Saga + Outbox Pattern:** Ensure atomic, distributed transactions across services.
- **Event-Driven Messaging:** Asynchronous communication via Kafka/RabbitMQ.
- **CQRS:** Separate read and write models for performance-sensitive services.

## Factors
- Preserve API contracts for existing clients.
- Enable **distributed transactional operations** (Book/Author/Genre, Reader/User).
- Support **scalability and high availability** under peak load.
- Facilitate future integration and evolution of LMS services.

## Solution
- **Event-driven messaging**: Decouples services, allows asynchronous processing.
- **Saga + Outbox**: Manages distributed atomic operations reliably.
- **CQRS**: Improves read/write performance and system responsiveness.
- **Polyglot Persistence**: Each service uses the DB best suited for its domain.
- **Strangler Fig migration**: Ensures smooth transition with zero downtime.

## Motivation
- Achieve high availability and performance for LMS operations.
- Enable maintainable and modular service architecture.
- Reduce operational coupling between services and domains.
- Facilitate scaling and future growth without rearchitecting the system.

## Alternatives

| Alternative                    | Description                             | Advantages                              | Disadvantages                                  | Decision  |
|--------------------------------|-----------------------------------------|-----------------------------------------|------------------------------------------------|-----------|
| Monolithic refactor            | Modularize current system               | Low initial effort                      | Performance & availability limited             | Rejected  |
| Microservices w/o patterns     | Split into services, no messaging       | Some decoupling                         | No transactional guarantees, lower scalability | Rejected  |
| Microservices + Patterns       | Split + events, CQRS, Saga, Outbox      | High availability, scalable, resilient  | Operational complexity, learning curve         | Selected  |

## Pending Issues
- Managing eventual consistency and compensating transactions for complex workflows.
- Monitoring and debugging distributed asynchronous events.
- Training and documentation for teams on adopted patterns.