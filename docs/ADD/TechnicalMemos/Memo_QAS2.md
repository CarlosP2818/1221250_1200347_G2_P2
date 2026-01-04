# Technical Memo – QAS 2: Reader and User Creation

## Issue
Provide a reliable, atomic mechanism to create a `Reader` and the associated `User` in the same request, ensuring data consistency and supporting future identity integrations.

## Problem
Currently, creating a `Reader` and `User` separately can lead to:

- **Inconsistencies:** `Reader` may exist without a `User` or vice versa.
- **Tight coupling:** Services depend directly on each other.
- **Limited scalability:** Cannot efficiently handle high-concurrency requests.
- **Reduced testability:** Hard to mock and test atomic workflows.

## Summary of Solution
Adopt a microservices-based architecture with **Saga orchestration**, **RabbitMQ messaging**, and optional caching:

- **ReaderService & UserService:** Independent services managing their respective entities.
- **ReaderUserCreationCoordinatorService:** Orchestrates atomic creation using Saga pattern.
- **RabbitMQ Message Broker:** Handles asynchronous events and decouples workflows.
- **Cache Layer (optional):** Temporary storage to manage high-concurrency requests.
- **Resilience Mechanisms:** Retry, circuit breaker, and fallback to ensure consistency.

## Factors
- Atomicity: Both `Reader` and `User` must be created successfully or neither.
- Services must remain **decoupled** for scalability and maintainability.
- System must handle **high-concurrency requests** reliably.
- Extensible for future identity providers (OAuth, SSO, LDAP).

## Solution
- **Saga orchestration because:** Ensures distributed transaction consistency across services.
- **RabbitMQ messaging because:** Decouples services and provides reliable asynchronous communication.
- **Independent services because:** Isolates failures and allows horizontal scaling.
- **Retry & circuit breaker because:** Improves resilience and system reliability.
- **Optional caching because:** Reduces repeated validations under load.

## Motivation
- Ensure atomic creation of `Reader` and `User`.
- Support high-concurrency requests without compromising consistency.
- Extensible architecture for future identity integrations.
- Facilitate testing using service mocks and message simulations.

## Alternatives

| Alternative                     | Description                              | Pros                                  | Cons                             | Decision |
|---------------------------------|------------------------------------------|---------------------------------------|----------------------------------|----------|
| Monolithic creation endpoint    | Single service handles both entities     | Simple implementation                 | Hard to scale, difficult to test | Rejected |
| Microservices + Saga + RabbitMQ | Distributed, decoupled services          | Scalable, resilient, atomic creation  | Higher operational complexity    | Selected |

## Pending Issues
- How to monitor and recover failed Sagas efficiently?
- Should temporary caching be standardized across all high-concurrency workflows?
- Integration plan for additional identity providers in future releases?