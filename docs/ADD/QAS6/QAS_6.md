# ADD 6 – Fault Tolerance / Health Monitoring

*Attribute-Driven Design (ADD) Full Documentation*

---

## 1. Overview

This ADD focuses on **Fault Tolerance and Health Monitoring** for the LMS microservices. The goal is to ensure the system remains **available and resilient** under failures, hardware issues, or software errors.

- **Objective:** Detect failures early and recover automatically to maintain high availability.
- **Functional Scope:** All LMS microservices (Books, Authors, Genres, Readers, Users).
- **Quality Attributes Addressed:** Availability, Fault Tolerance, Resilience, Observability.
- **Compatibility:** Existing clients should not experience errors due to service failures.
- **Extensibility:** New services adopt the same health monitoring and fault tolerance patterns.

---

## Step 1 – Review Inputs

### 1.1 Objective

- Detect service failures through **automated health checks**.
- Recover failed services automatically via **orchestration and scaling**.
- Ensure **high availability** under load and failure conditions.
- Provide **observability** for proactive maintenance.

### 1.2 Problem Statement

- Monolithic LMS suffers from **single points of failure**.
- A failed module affects the **entire system**, reducing availability.
- Manual recovery is slow, impacting **user experience and SLA compliance**.

### 1.3 Requirements (SMART)

| Type           | Requirement                                                                                   |
|----------------|-----------------------------------------------------------------------------------------------|
| **Specific**   | Implement automated health checks, auto-recovery, and monitoring for all microservices.      |
| **Measurable** | Service uptime > 99.9%; failed services automatically restarted within acceptable time.      |
| **Attainable** | Use container orchestration, monitoring tools, and retries for fault recovery.               |
| **Relevant**   | Critical to maintain user trust, system reliability, and SLA compliance.                     |
| **Time-Bound** | Implemented during the current LMS reengineering sprint.                                      |

**Variation and Evolution Points**

- **Variation points:** Monitoring frequency, retry intervals, alert thresholds.
- **Evolution points:** New services automatically inherit health checks and fault recovery rules.

---

## Step 2 – Establish Goals and Select Inputs

### 2.1 Drivers

| Type             | Driver                                                                                      |
|-----------------|---------------------------------------------------------------------------------------------|
| **Functional**  | Detect failures, recover services, maintain uptime.                                          |
| **Quality**     | Fault Tolerance, Availability, Resilience, Observability.                                    |
| **Constraints** | Minimal impact on existing services; automatic recovery required.                            |
| **Business**    | Ensure service reliability for library operations and external integrations.                 |

### 2.2 Design Decisions

- **Health Checks:** Periodic monitoring of service endpoints and dependencies.
- **Auto-Recovery:** Restart or reschedule failed service instances automatically.
- **Circuit Breakers:** Prevent cascading failures across services.
- **Redundancy:** Maintain multiple instances for high-availability services.
- **Monitoring & Alerts:** Use dashboards and alerts to track failures and recovery.

---

## Step 3 – Choose Elements to Decompose

### 3.1 Selected Elements

| Element                  | Purpose                                                   |
|--------------------------|-----------------------------------------------------------|
| Health Check Endpoints    | Detect service status and response                       |
| Orchestrator / Scheduler | Restart or reschedule failed services                    |
| Circuit Breakers          | Prevent failure propagation between services             |
| Monitoring Dashboard      | Visualize system health, metrics, and anomalies          |
| Alerting System           | Notify operators of failures or recovery actions         |

### 3.2 Rationale for Selection

- **Early detection** prevents downtime from propagating.
- **Automatic recovery** minimizes manual intervention and SLA impact.
- **Circuit breakers** protect dependent services from cascading failures.
- **Monitoring and alerts** enable proactive system management.

---

## Step 4 – Choose Design Concepts

### 4.1 Tactics

| Quality Attribute      | Tactic                   | Description                                           | Implementation                                      |
|------------------------|--------------------------|-----------------------------------------------------|----------------------------------------------------|
| Availability           | Redundancy               | Maintain multiple instances per service            | Kubernetes/Docker Swarm replica management         |
| Fault Tolerance        | Auto-Recovery            | Restart failed instances automatically             | Orchestrator triggers container restart           |
| Fault Containment      | Circuit Breaker          | Prevent cascading failures                          | Service-level circuit breaker implementation       |
| Observability          | Health Checks & Monitoring | Detect failures and alert operators                | HTTP/REST endpoints, Prometheus/Grafana dashboards|
| Resilience             | Retry Policies           | Retry transient failures                            | Configured per service with backoff               |

### 4.2 Reference Architecture / Patterns

- **Microservices per service instance** with independent database and deployment.
- **Health Check Endpoints**: `/health` endpoint for each service.
- **Orchestration Platform**: Kubernetes or Docker Swarm handles auto-recovery.
- **Circuit Breaker Pattern**: Avoids propagation of service failures.
- **Monitoring & Alerting**: Prometheus, Grafana, and notifications (Slack/Email).

### 4.3 Architectural Design Alternatives and Rationale

| Alternative                    | Description                                  | Advantages                        | Disadvantages                  | Decision    |
|--------------------------------|----------------------------------------------|-----------------------------------|--------------------------------|-------------|
| Manual monitoring & restart    | Human operator monitors and restarts services| Low technical complexity           | Slow, error-prone, high downtime| ❌ Rejected |
| Basic logging only             | Collect errors without automated recovery    | Easy to implement                  | Failures affect SLA             | ❌ Rejected |
| Automated health monitoring + recovery | Health checks + auto-restart + circuit breakers | High availability, fast recovery | Requires orchestration and configuration | ✅ Selected |

---

## Step 5 – Instantiate Elements, Allocate Responsibilities, Define Interfaces

| Component                  | Responsibility                                        | Interface/Methods                               |
|----------------------------|----------------------------------------------------|------------------------------------------------|
| Health Check Endpoint       | Provide service health status                        | GET /health                                     |
| Orchestrator / Scheduler    | Restart or reschedule failed services               | restartService(), reschedule()                 |
| Circuit Breaker             | Prevent cascading failures                          | open(), close(), halfOpen()                     |
| Monitoring Dashboard         | Visualize metrics, track failures and recovery      | showMetrics(), alertOnFailure()                |
| Alerting System             | Notify operators of service failures               | sendAlert(), resolveAlert()                     |

---

## Step 6 – Sketch Views and Record Decisions

### 6.1 Trade-Off Analysis

| Decision                               | Benefit                                     | Cost                                     |
|----------------------------------------|--------------------------------------------|-----------------------------------------|
| Automated health checks                 | Detect failures early, fast recovery       | Slight overhead on service                |
| Circuit breakers                         | Contain failure propagation                | Additional configuration and complexity  |
| Redundant service instances             | Improve uptime, maintain availability      | More resources required                   |
| Monitoring & alerts                     | Proactive detection and response           | Setup and maintenance effort              |

### 6.2 Outcome

- LMS microservices detect failures **immediately**.
- **Automatic recovery** restores service availability quickly.
- **Circuit breakers** prevent failure propagation to other services.
- Operators have **real-time visibility** via dashboards and alerts.
- Overall system **resilience and uptime** are improved.

---

## Step 7 – Perform Analysis of Current Design and Review Iteration Goals

- Monolithic LMS had **single points of failure** causing system downtime.
- Microservices with **health checks, auto-recovery, and circuit breakers** maintain availability.
- Monitoring and alerting allow **proactive intervention** if needed.
- Ensures LMS services meet **availability and fault-tolerance requirements**.

**Result:** LMS microservices are **resilient**, **self-healing**, and maintain high **availability**, with automated monitoring, recovery, and fault containment.
