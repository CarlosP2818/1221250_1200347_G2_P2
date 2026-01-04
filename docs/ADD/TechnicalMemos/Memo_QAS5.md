# Technical Memo – QAS 5: Polyglot Data Persistence

## Issue
Ensure LMS microservices use the most suitable database technology per domain while maintaining availability, performance, and scalability.

## Problem
A single database type across all services causes:

- Bottlenecks and poor performance for high-demand services.
- Limited flexibility for dynamic or semi-structured data (e.g., student comments/grades).
- Tight coupling across services; failures affect unrelated domains.
- Cross-service consistency challenges when databases differ.

## Summary of Solution
Adopt a **polyglot persistence approach** with **database per service** and **messaging for cross-service coordination**:

- **SQL Databases:** BookService, AuthorService, GenreService, UserService (structured relational data).
- **MongoDB:** StudentService (flexible schema for comments, grades, activity logs).
- **Message Broker:** Sagas and Outbox patterns to ensure eventual consistency.
- **Distributed Cache:** Redis for frequently accessed data and performance optimization.
- **Resilience Tactics:** Circuit breakers and retries for database failures.

## Factors
- Each service must remain **independent** and **highly available**.
- Queries must be optimized for the service domain.
- Eventual consistency across heterogeneous databases must be guaranteed.
- System must remain **extensible** for future services adopting different databases.

## Solution
- **Database per service because:** Isolates failures and allows independent scaling.
- **Polyglot persistence because:** Each DB type matches the service's data model (relational vs flexible schema).
- **Sagas / Outbox because:** Coordinates cross-service operations without tight coupling.
- **Cache Layer because:** Reduces DB load and improves response time.
- **Resilience (retry/circuit breaker) because:** Ensures reliability when DB instances fail.

## Motivation
- Achieve high **availability**, **performance**, and **scalability** per service.
- Support future services choosing optimal DB technology.
- Maintain client API consistency despite heterogeneous back-end databases.
- Facilitate maintainability and testing per service.

## Alternatives

| Alternative             | Description                          | Pros                                   | Cons                                   | Decision |
|-------------------------|--------------------------------------|----------------------------------------|----------------------------------------|----------|
| Single SQL Database     | All services share one relational DB | Simple operations                      | Bottleneck, poor flexibility           | Rejected |
| Single MongoDB          | All services share one document DB   | Flexible schema                        | Poor integrity for structured entities | Rejected |
| Polyglot DB per service | Each service selects optimal DB type | Performance, availability, scalability | Operational complexity                 | Selected |

## Pending Issues
- How to handle schema migrations in polyglot DB environments?
- Which additional DB types (NoSQL, graph, time-series) will future services need?
- How to monitor cross-service transactions for eventual consistency?