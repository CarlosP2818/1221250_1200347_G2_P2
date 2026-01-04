# ADD 15 – Interoperability

*Attribute-Driven Design (ADD) Full Documentation*

---

## 1. Overview

This ADD focuses on **Interoperability** of LMS microservices, ensuring seamless integration with **internal and external systems**.

- **Objective:** Enable LMS services to communicate reliably with other systems via standard protocols and contracts.
- **Functional Scope:** All microservices, external APIs, and event-driven systems.
- **Quality Attributes Addressed:** Interoperability, Compatibility, Maintainability.
- **Extensibility:** New services can integrate without redesigning existing interfaces.

---

## Step 1 – Review Inputs

### 1.1 Objective
- Standardize APIs and messaging for consistent interaction.
- Support synchronous (REST) and asynchronous (events/messages) communication.
- Validate integration points via Consumer-Driven Contracts (CDC).

### 1.2 Problem Statement
- Monolithic LMS had tight coupling and proprietary interfaces.
- Integrating new systems caused **complex adaptations**.
- Lack of standardization reduces reusability and increases integration risk.

### 1.3 Requirements (SMART)

| Type           | Requirement                                                                 |
|----------------|----------------------------------------------------------------------------|
| **Specific**   | Expose services via standard REST APIs and message contracts.              |
| **Measurable** | CDC tests validate correct consumption/production of messages.             |
| **Attainable** | Use OpenAPI specs, Pact files, Kafka/RabbitMQ for asynchronous messaging.  |
| **Relevant**   | Essential for integration with external systems and future LMS extensions. |
| **Time-Bound** | Implemented during the LMS reengineering sprint.                            |

---

## Step 2 – Establish Goals and Select Inputs

| Type             | Driver                                                        |
|-----------------|---------------------------------------------------------------|
| **Functional**  | Expose services via standard interfaces                        |
| **Quality**     | Compatibility, Integration, Reusability                       |
| **Constraints** | REST, messaging protocols, CDC contracts                       |
| **Business**    | Facilitate integration with other library or enterprise systems |

### 2.2 Design Decisions
- Use **OpenAPI/Swagger** for REST contract documentation.
- Use **CDC tests (Pact)** to validate event-driven communication.
- Support **asynchronous messaging** with Kafka/RabbitMQ.

---

## Step 3 – Choose Elements to Decompose

| Element                  | Purpose                                                |
|--------------------------|--------------------------------------------------------|
| REST APIs                 | Standard communication interface                        |
| Message Broker            | Asynchronous communication between services            |
| CDC Tests                 | Validate contract adherence                              |
| OpenAPI Specs             | Document APIs for integration                            |

---

## Step 4 – Choose Design Concepts

| Quality Attribute      | Tactic                       | Description                         | Implementation                        |
|------------------------|------------------------------|-------------------------------------|--------------------------------------|
| Interoperability       | Standardized APIs            | Uniform API structure               | REST endpoints with OpenAPI          |
| Interoperability       | Event-driven contracts       | Events follow defined contract      | Pact/CDC tests, Kafka/RabbitMQ      |
| Maintainability        | Versioning                   | Backward-compatible API evolution  | Semantic versioning                  |

---

## Step 5 – Instantiate Elements, Allocate Responsibilities

| Component          | Responsibility                                        | Interface/Methods                     |
|-------------------|------------------------------------------------------|--------------------------------------|
| API Gateway        | Expose and route service APIs                        | REST endpoints, routing rules         |
| Message Broker     | Manage asynchronous events                           | publishEvent(), subscribeEvent()     |
| Service APIs       | Offer service functionality                          | CRUD endpoints, event hooks          |
| CDC Tests          | Validate integration contracts                        | pactVerify(), publish/consume events |

---

## Step 6 – Trade-Off Analysis

| Decision                       | Benefit                             | Cost                                |
|--------------------------------|------------------------------------|------------------------------------|
| Standardized REST APIs          | Ease integration                   | Requires API design discipline      |
| CDC tests for events            | Reliable async communication       | Extra test maintenance              |
| Message Broker                  | Decouples services                  | Added infrastructure complexity     |

---

## Step 7 – Analysis & Outcome

- LMS microservices communicate **reliably via standard interfaces**.
- Integration with external systems is **predictable and safe**.
- CDC tests ensure **event-driven interoperability**.
- Outcome: LMS is **future-proof for new integrations**.