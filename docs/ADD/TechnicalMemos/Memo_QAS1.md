# Technical Memo – QAS 1: LMS Reengineering

## Issue
Reengineer the LMS application to a distributed/decentralized architecture to improve performance, availability, scalability, and elasticity.

## Problem
The current LMS system is monolithic (or modular monolith), which limits:

- **Availability:** Failure in one module may impact the entire system.
- **Performance:** High demand peaks (>Y requests/period) degrade response times.
- **Scalability:** Hard to allocate resources granularly.
- **Flexibility:** Changes affect multiple components and clients.

## Summary of Solution
Adopt a microservices-based architecture with **independent services**, **API Gateway**, **event-driven communication**, **distributed caching**, and **resiliency patterns**:

- **Independent Services:** `BookService`, `AuthorService`, `GenreService`, `ReaderService`, `UserService`.
- **API Gateway:** Single entry point routing requests to appropriate services.
- **Message Broker:** Optional asynchronous communication between services.
- **Distributed Cache:** Redis or similar to reduce database load and latency.
- **Resilience Mechanisms:** Circuit breakers, retries, and component isolation.
- **Monitoring:** Centralized logging and metrics for observability.
- **Database per Service / Polyglot Persistence:** Each service manages its own data store for horizontal scalability (e.g., `Student` uses MongoDB, `Book` uses SQL).

## Factors
- Services must be **independent** and **scalable**.
- High **availability** is required even if individual services fail.
- **Performance** must improve by at least 25% under high-demand conditions.
- Future services must be easily **integrable** without affecting existing ones.
- System must remain **compatible** with existing clients and APIs.

## Solution
- **Independent services because:** Failure in one does not affect others; enables targeted scaling.
- **API Gateway because:** Provides unified access, routing, and fallback.
- **Event-driven communication because:** Enables eventual consistency and decouples services.
- **Distributed cache because:** Reduces latency and DB load during peak demand.
- **Polyglot persistence because:** Each service uses the optimal data store (MongoDB or SQL).
- **Resilience patterns because:** Circuit breakers and retries isolate failures and improve availability.

## Motivation
- Reliable operation during high-load and partial failures.
- Supports future integrations and additional services.
- Scalable deployment for varying traffic patterns.
- Decouples domain logic from infrastructure concerns.

## Alternatives

| Alternative                           | Description                             | Pros                                    | Cons                                           | Decision   |
|---------------------------------------|-----------------------------------------|-----------------------------------------|------------------------------------------------|------------|
| **Monolithic refactoring**            | Modularize current LMS                  | Less initial effort                     | Limited availability, scalability, performance | Rejected   |
| **Microservices + API Gateway**       | Independent services with orchestration | High availability, scalable, extensible | Higher operational complexity                  | Selected   |

## Pending Issues
- Which services should be prioritized for independent scaling?
- How to define consistent caching and fallback strategies across services?
- Integration plan for external APIs or third-party systems in future releases?