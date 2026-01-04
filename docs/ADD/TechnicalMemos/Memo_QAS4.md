# Technical Memo – QAS 4: Availability

## Issue
Ensure the LMS system remains available during service failures, infrastructure faults, or high-load scenarios.

## Problem
The current monolithic LMS has limited availability:

- Single points of failure affect the entire system.
- No automated recovery or failover mechanisms.
- Users experience downtime during service interruptions.

## Summary of Solution
Adopt a distributed microservices architecture with **resilience patterns**, **orchestration**, and **fallback mechanisms**:

- **Independent Microservices:** BookService, AuthorService, GenreService, ReaderService, UserService.
- **API Gateway:** Routes requests and handles degraded responses.
- **Load Balancer:** Distributes traffic to healthy instances.
- **Orchestrator (Kubernetes):** Auto-restarts failed instances, manages scaling, and performs health checks.
- **Circuit Breakers & Retry:** Protects services from cascading failures.
- **Distributed Cache:** Reduces load on services recovering from failures.

## Factors
- System must achieve **high availability** (uptime ≥ 99.9%).
- Failures should be **isolated** to individual services.
- Minimal impact on clients during recovery.
- Scalable to support future services and high-demand peaks.

## Solution
- **Independent services because:** Isolate failures and allow horizontal scaling.
- **Circuit breakers & retry because:** Prevent cascading failures and improve resilience.
- **Load balancer + orchestrator because:** Automatically redirect traffic and recover failed instances.
- **API Gateway fallback because:** Maintain functionality and user experience during partial failures.
- **Distributed cache because:** Reduce service load and improve performance during recovery.

## Motivation
- Ensure uninterrupted LMS operations for librarians and users.
- Enable rapid recovery from failures without service interruption.
- Facilitate scaling, monitoring, and future growth.
- Improve overall system reliability and maintain client compatibility.

## Alternatives

| Alternative                         | Description                             | Pros                               | Cons                                    | Decision  |
|-------------------------------------|-----------------------------------------|------------------------------------|-----------------------------------------|-----------|
| Monolithic refactoring              | Add redundancy to the existing monolith | Easier initial deployment          | High downtime risk, low fault isolation | Rejected  |
| Microservices + Resilience Patterns | Independent services with failover      | High availability, fault isolation | Higher operational complexity           | Selected  |

## Pending Issues
- How to define SLA thresholds for automatic failover?
- Should recovery strategies be standardized across all services?
- Monitoring of cascading failures and proactive alerts.