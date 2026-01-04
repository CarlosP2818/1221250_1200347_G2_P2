# ADD 10 – Elasticity / Auto-Scaling

*Attribute-Driven Design (ADD) Full Documentation*

---

## 1. Overview

This ADD focuses on **Elasticity and Auto-Scaling** for the LMS microservices reengineering project. The goal is to ensure **efficient use of hardware**, support **peak demand**, and maintain **high performance and availability**.

- **Objective:** Automatically scale microservices based on runtime load, minimizing idle resource consumption.
- **Functional Scope:** Support all LMS services (Books, Authors, Genres, Readers, Users) under variable traffic.
- **Quality Attributes Addressed:** Elasticity, Performance, Availability, Resource Efficiency.
- **Compatibility:** Scaling actions must not disrupt API contracts for existing clients.
- **Extensibility:** New services can adopt the same scaling patterns without changes.

---

## Step 1. Review Inputs

### 1.1 Objective

- Dynamically allocate resources to services based on **current workload**.
- Minimize **hardware usage** during low demand.
- Handle **sporadic peak loads** (>Y requests/period) efficiently.
- Maintain **minimal impact** on clients and other services.

### 1.2 Problem Statement

- Monolithic LMS cannot dynamically scale under load, leading to **performance degradation**.
- Fixed resource allocation wastes **CPU and memory** during low demand.
- Services cannot scale independently, impacting **availability and response times**.

### 1.3 Requirements (SMART)

| Type           | Requirement                                                                                   |
|----------------|-----------------------------------------------------------------------------------------------|
| **Specific**   | Implement automatic scaling scripts to dynamically adjust service instances based on load.    |
| **Measurable** | Validate scaling behavior through load tests and monitoring metrics.                           |
| **Attainable** | Implementable using **Docker/Kubernetes autoscaling, metrics collection, and scaling scripts**.|
| **Relevant**   | Essential to meet peak demand performance goals and optimize hardware usage.                  |
| **Time-Bound** | Implemented in the current sprint/project.                                                    |

**Variation and Evolution Points**

- **Variation points:** Scaling thresholds, metrics types (CPU, memory, request count).
- **Evolution points:** New services adopt scaling policies without affecting others.

---

## Step 2: Establish Goals and Select Inputs

### 2.1 Drivers

| Type             | Driver                                                                                      |
|-----------------|---------------------------------------------------------------------------------------------|
| **Functional**  | Automatically scale services to handle variable traffic.                                     |
| **Quality**     | Elasticity, Performance, Availability, Resource Efficiency.                                  |
| **Constraints** | Scaling actions must not cause downtime; low-impact on clients.                              |
| **Business**    | Optimize resource usage and maintain SLA under peak demand.                                   |

### 2.2 Design Decisions

- **Monitoring:** Collect metrics for CPU, memory, request rate per service.
- **Scaling Mechanism:** Scripts automatically adjust service replicas based on thresholds.
- **Service Isolation:** Each service scales independently.
- **Fallback:** Ensure minimum instances always running to maintain availability.
- **Orchestration:** Use Docker Swarm/Kubernetes for container scaling.

---

## Step 3 – Choose Elements to Decompose

### 3.1 Selected Elements

| Element            | Purpose                                                  |
|-------------------|----------------------------------------------------------|
| Metrics Collector  | Collects CPU, memory, and request rate metrics          |
| Scaling Scripts    | Executes scaling actions based on thresholds           |
| Service Containers | Individual microservices that can be replicated         |
| Load Balancer      | Distributes traffic to active service instances         |
| Orchestration Tool | Manages scaling operations and container lifecycle     |

### 3.2 Rationale for Selection

- Independent scaling avoids **over-provisioning**.
- Metrics-driven decisions ensure **responsive elasticity**.
- Orchestrator ensures **consistent, automated scaling** without downtime.

---

## Step 4 – Choose Design Concepts

### 4.1 Tactics

| Quality Attribute | Tactic                     | Description                                    | Implementation                                         |
|------------------|----------------------------|------------------------------------------------|-------------------------------------------------------|
| Elasticity       | Dynamic Resource Allocation | Scale resources according to workload          | Autoscaling scripts triggered by metrics              |
| Availability     | Minimum Service Instances   | Keep at least 1-2 instances running           | Orchestrator ensures minimum replicas                |
| Performance      | Horizontal Scaling          | Add more instances to handle increased load   | Docker/K8s scale commands                             |
| Maintainability  | Modular Scaling             | Each service scales independently             | Scripts per service, isolated configurations         |
| Testability      | Load Test Validation        | Verify scaling under realistic traffic        | Automated load tests and metrics monitoring          |

### 4.2 Reference Architecture / Patterns

- **Horizontal Pod/Container Scaling:** Add/remove service replicas.
- **Metrics-driven Autoscaling:** CPU, memory, request count triggers.
- **Service Isolation:** Independent scaling for each microservice.
- **Load Balancing:** Distribute traffic to scaled instances.

### 4.3 Architectural Design Alternatives and Rationale

| Alternative                    | Description                                   | Advantages                        | Disadvantages                  | Decision    |
|--------------------------------|-----------------------------------------------|-----------------------------------|--------------------------------|-------------|
| Manual scaling                 | Dev manually adjusts instances               | Simple, no automation             | Slow response, human error      | ❌ Rejected |
| Fixed replicas                 | Each service has static number of instances | Predictable resource usage        | Waste during low load, insufficient under peaks | ❌ Rejected |
| Auto-scaling with scripts      | Dynamic adjustment based on metrics         | Efficient, responsive, elastic    | Slight orchestration complexity | ✅ Selected |

---

## Step 5 – Instantiate Elements, Allocate Responsibilities, Define Interfaces

| Component           | Responsibility                                    | Interface/Methods                         |
|--------------------|--------------------------------------------------|------------------------------------------|
| Metrics Collector   | Collect CPU, memory, request rate                | collectCPU(), collectMemory(), collectRequests() |
| Scaling Scripts     | Adjust number of service instances               | scaleUp(service), scaleDown(service)     |
| Service Containers  | Run LMS microservices independently              | REST APIs, event endpoints                |
| Load Balancer       | Route traffic to active instances                | distributeRequest(), healthCheck()       |
| Orchestration Tool  | Manage scaling, deployment, lifecycle           | autoscale(), monitorMetrics()            |

---

## Step 6 – Sketch Views and Record Decisions

### 6.1 Trade-Off Analysis

| Decision                       | Benefit                                     | Cost                                     |
|--------------------------------|--------------------------------------------|-----------------------------------------|
| Metrics-driven auto-scaling     | Efficient resource usage, responsive        | Slight complexity, needs monitoring     |
| Horizontal scaling per service  | Independent scaling for high-demand services| Increased orchestration effort           |
| Minimum replicas                | Maintains availability                       | Slight resource overhead                 |

### 6.2 Outcome

- LMS microservices **scale automatically** according to demand.
- Efficient use of **hardware resources** during low load.
- Supports **peak load handling** without downtime.
- Minimal client impact, **high availability** ensured.

---

## Step 7 – Perform Analysis of Current Design and Review Iteration Goals

- Monolithic LMS cannot handle **sporadic high demand** efficiently.
- Auto-scaling scripts with metrics **solve elasticity issues**.
- Each microservice **scales independently** to optimize resources.
- Orchestrator ensures **consistent, automated scaling**.

**Result:** LMS microservices are **elastic and resource-efficient**, ready to handle peak demand and minimize idle hardware usage.
