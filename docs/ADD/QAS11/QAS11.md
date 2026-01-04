# ADD 11 – Performance & Load Testing

*Attribute-Driven Design (ADD) Full Documentation*

---

## 1. Overview

This ADD focuses on **Performance and Load Testing** for the LMS microservices reengineering project. The goal is to ensure the system meets **non-functional requirements** under high demand, validating **scalability, response times, and stability**.

- **Objective:** Measure and validate LMS performance under **normal and peak loads**, achieving **+25% improvement** over the monolithic system.
- **Functional Scope:** All LMS services, including **Books, Authors, Genres, Readers, and Users**.
- **Quality Attributes:** Performance, Scalability, Availability, Resource Efficiency.
- **Compatibility:** Testing should not disrupt existing client operations.
- **Extensibility:** Load testing framework can be extended for new services in future sprints.

---

## Step 1. Review Inputs

### 1.1 Objective

- Evaluate **system behavior under varying loads** (>Y requests/period).
- Compare **microservices architecture performance** against the previous monolithic LMS.
- Identify **bottlenecks** in service orchestration, messaging, and database access.
- Support **capacity planning and scaling decisions**.

### 1.2 Problem Statement

- Monolithic LMS experiences **degraded response times** under high load.
- Performance metrics for distributed services are unavailable.
- Without load testing, **SLA and availability targets** cannot be validated.

### 1.3 Requirements (SMART)

| Type           | Requirement                                                                                   |
|----------------|-----------------------------------------------------------------------------------------------|
| **Specific**   | Validate LMS performance under normal and peak load conditions using automated tests.        |
| **Measurable** | Achieve +25% improvement in response times under high demand; identify scalability limits.    |
| **Attainable** | Implementable using tools such as **JMeter, Gatling, or Locust**.                             |
| **Relevant**   | Critical to ensure SLA and user experience expectations are met.                              |
| **Time-Bound** | Completed in the current sprint/project for performance validation.                            |

**Variation / Evolution Points**

- **Variation points:** Load patterns, request types, user concurrency levels.
- **Evolution points:** Framework can scale to additional services or future features.

---

## Step 2. Establish Goals and Select Inputs

### 2.1 Drivers

| Type             | Driver                                                                                      |
|-----------------|---------------------------------------------------------------------------------------------|
| **Functional**  | Validate LMS transactions (Book/Author/Genre creation, Reader/User creation) under load.    |
| **Quality**     | Performance, Scalability, Availability, Elasticity.                                         |
| **Constraints** | Tests must simulate realistic load without disrupting production.                           |
| **Business**    | Ensure LMS maintains SLA during peak academic periods or critical user operations.         |

### 2.2 Design Decisions

- **Load Testing Tools:** JMeter, Gatling, or Locust for stress and load testing.
- **Metrics Collection:** Capture response times, throughput, CPU/memory usage.
- **Scalability Validation:** Test auto-scaling and elasticity under peak conditions.
- **Bottleneck Identification:** Monitor orchestration, messaging, and database queries.
- **Reporting:** Dashboards and reports for analysis and future capacity planning.

---

## Step 3. Choose Elements to Decompose

| Element           | Purpose                                                        |
|-----------------|----------------------------------------------------------------|
| Load Testing Suite | Simulate user traffic and peak load scenarios                  |
| Metrics Collector  | Capture performance metrics (CPU, memory, throughput)         |
| Monitoring Tools   | Visualize real-time metrics during tests                      |
| Service Endpoints  | APIs of microservices to be tested                             |
| Reporting Engine   | Generate performance reports and identify bottlenecks          |

### 3.2 Rationale for Selection

- Enables **objective SLA validation**.
- Identifies **bottlenecks before production**.
- Ensures **consistent scalability and performance** under load.

---

## Step 4. Choose Design Concepts

### 4.1 Tactics

| Quality Attribute | Tactic                        | Description                                      | Implementation                        |
|------------------|-------------------------------|-------------------------------------------------|--------------------------------------|
| Performance      | Load & Stress Testing         | Simulate high demand and measure response times | JMeter/Gatling/Locust scripts        |
| Scalability      | Scenario-based Testing        | Validate auto-scaling and elasticity            | Load tests at different levels       |
| Availability     | Failover Simulation           | Test system resilience during failures          | Controlled service failures          |
| Resource Efficiency | Resource Monitoring          | Observe CPU/memory consumption                  | Prometheus/Grafana dashboards        |
| Maintainability  | Automated Reporting           | Consolidate results for analysis                | CSV/HTML dashboards                  |

---

## Step 5. Instantiate Elements, Allocate Responsibilities, Define Interfaces

| Component           | Responsibility                                         | Interface/Methods                       |
|--------------------|-------------------------------------------------------|----------------------------------------|
| Load Testing Suite  | Simulate user traffic                                   | simulateLoad(), simulateStress()       |
| Metrics Collector   | Capture performance metrics                             | collectCPU(), collectMemory(), collectLatency() |
| Monitoring Tools    | Visualize real-time metrics                             | showMetrics(), alertOnThreshold()      |
| Service Endpoints   | Receive simulated requests                               | REST/GraphQL API endpoints             |
| Reporting Engine    | Consolidate results and generate reports               | generateReport(), exportCSV()          |

---

## Step 6. Sketch Views and Record Decisions

### 6.1 Trade-Off Analysis

| Decision                            | Benefit                                         | Cost                                         |
|-------------------------------------|------------------------------------------------|---------------------------------------------|
| Automated load/stress testing        | Reproduces production scenarios accurately     | Scripts require setup and maintenance       |
| Metrics collection & monitoring      | Identifies bottlenecks and scaling capacity    | Minimal overhead for metrics collection     |
| Reporting & dashboards               | Facilitates analysis and planning              | Requires configuration and tool integration |

### 6.2 Outcome

- LMS microservices are validated for **performance, scalability, and availability**.
- **Bottlenecks identified** before production deployment.
- Supports **capacity planning and auto-scaling tuning**.
- Extensible framework for testing **future services**.

---

## Step 7. Perform Analysis of Current Design and Review Iteration Goals

- Monolithic LMS cannot reliably measure distributed service performance.
- Automated load testing validates **response times, throughput, and elasticity**.
- Results provide **data for optimization, scaling, and capacity planning**.

**Result:** LMS microservices are **load-tested**, ensuring SLA compliance, scalability, consistent performance, and readiness for peak traffic.