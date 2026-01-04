# ADD 11 – Performance & Load Testing

*Attribute-Driven Design (ADD) Full Documentation*

---

## 1. Overview

This ADD focuses on **Performance and Load Testing** for the LMS microservices reengineering project. The goal is to ensure the system meets **non-functional requirements** under high demand, validating **scalability, response times, and stability**.

- **Objective:** Measure and validate LMS performance under **peak and normal loads**, ensuring **+25% improvement** over the monolithic system.
- **Functional Scope:** All LMS services, including **Books, Authors, Genres, Readers, and Users**.
- **Quality Attributes Addressed:** Performance, Scalability, Availability, Resource Efficiency.
- **Compatibility:** Testing should not disrupt existing client operations.
- **Extensibility:** Load testing framework can be extended for new services in future sprints.

---

## Step 1. Review Inputs

### 1.1 Objective

- Evaluate **system behavior under varying loads** (>Y requests/period).
- Compare **microservices architecture performance** against previous monolithic LMS.
- Identify **bottlenecks** in service orchestration, messaging, and database access.
- Support **capacity planning and scaling decisions**.

### 1.2 Problem Statement

- Monolithic LMS experiences **degraded response times** during high demand.
- Performance metrics are unavailable for distributed services.
- Without load testing, **SLA and availability targets** cannot be validated.

### 1.3 Requirements (SMART)

| Type           | Requirement                                                                                   |
|----------------|-----------------------------------------------------------------------------------------------|
| **Specific**   | Validate LMS performance under peak and normal load conditions using automated tests.         |
| **Measurable** | Achieve +25% improvement in response times under high demand; identify scaling limits.        |
| **Attainable** | Implementable using load testing tools such as **JMeter, Gatling, or Locust**.               |
| **Relevant**   | Critical to ensure system meets performance SLAs and user experience expectations.            |
| **Time-Bound** | Completed in the current sprint/project for performance validation.                           |

**Variation and Evolution Points**

- **Variation points:** Load patterns, request types, user concurrency levels.
- **Evolution points:** Testing framework can scale to additional services or future features.

---

## Step 2: Establish Goals and Select Inputs

### 2.1 Drivers

| Type             | Driver                                                                                      |
|-----------------|---------------------------------------------------------------------------------------------|
| **Functional**  | Validate LMS transactions (Book/Author/Genre creation, Reader/User creation) under load.     |
| **Quality**     | Performance, Scalability, Availability, Elasticity.                                          |
| **Constraints** | Tests must simulate realistic load without disrupting production.                            |
| **Business**    | Ensure LMS maintains SLA during peak academic periods or user operations.                   |

### 2.2 Design
