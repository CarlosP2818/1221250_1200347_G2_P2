# ADD 17 – Testability

*Attribute-Driven Design (ADD) Full Documentation*

---

## 1. Overview

This ADD focuses on **Testability** of LMS microservices to enable **automated and reliable testing**.

- **Objective:** Facilitate unit, integration, CDC, and load testing for all microservices.
- **Quality Attributes Addressed:** Testability, Maintainability, Reliability.

---

## Step 1 – Review Inputs

### 1.1 Objective
- Enable **isolated testing of microservices**.
- Support **mocking, stubbing, and CDC tests**.
- Integrate tests into **CI/CD pipelines**.

### 1.2 Problem Statement
- Monolithic LMS was **difficult to test in isolation**.
- Changes risked **breaking unrelated functionality**.
- No automated contract or integration validation.

### 1.3 Requirements (SMART)

| Type           | Requirement                                             |
|----------------|---------------------------------------------------------|
| Specific       | Support unit, integration, and CDC testing per service |
| Measurable     | All tests run automatically in pipeline with success  |
| Attainable     | Use JUnit, Mockito, Pact, and CI/CD integration        |
| Relevant       | Ensures safe evolution and continuous delivery        |
| Time-Bound     | Implemented in LMS microservices reengineering sprint  |

---

## Step 2 – Establish Goals

| Type             | Driver                                  |
|-----------------|----------------------------------------|
| Functional       | Unit, integration, contract tests      |
| Quality          | Testability, Reliability               |
| Constraints      | Services must be isolatable            |
| Business         | Reduce risk of failures in production  |

### 2.2 Design Decisions
- **Unit tests:** Service classes tested independently.
- **Integration tests:** Test interactions between services using test environments.
- **CDC tests:** Validate event/message contracts between producers and consumers.
- **Pipeline integration:** Run all tests automatically on CI/CD.

---

## Step 3 – Elements to Decompose

| Element          | Purpose                              |
|-----------------|--------------------------------------|
| Service Classes  | Unit testable components              |
| Mocks/Stubs      | Simulate dependencies                |
| CDC Tests        | Validate event-driven interactions   |
| CI/CD Pipeline   | Automate test execution              |

---

## Step 4 – Design Concepts

| Quality Attribute | Tactic                     | Description                            | Implementation                  |
|-----------------|-----------------------------|----------------------------------------|--------------------------------|
| Testability     | Isolation & Mocking        | Test classes independently              | Mockito, Test doubles           |
| Testability     | CDC Tests                  | Validate inter-service contracts       | Pact tests, CI/CD integration   |
| Testability     | Pipeline Integration       | Run tests automatically                 | Jenkins/GitHub Actions          |
| Reliability     | Load & Performance Testing | Detect performance issues               | JMeter, Gatling                  |

---

## Step 5 – Instantiate Elements

| Component          | Responsibility                       | Interface/Methods                  |
|-------------------|--------------------------------------|----------------------------------|
| Unit Tests         | Test service classes in isolation    | assertEquals(), mock(), verify() |
| Integration Tests  | Validate interactions across services | REST calls, message simulations   |
| CDC Tests          | Contract verification                | pactVerify(), consumeEvent()      |
| CI/CD Pipeline     | Execute tests automatically          | pipeline scripts, test reports   |

---

## Step 6 – Trade-Off Analysis

| Decision                   | Benefit                                   | Cost                                |
|----------------------------|-------------------------------------------|------------------------------------|
| Service isolation          | Reliable unit testing                      | Requires test doubles/mocks        |
| CDC tests                  | Prevent integration failures               | Extra maintenance for contracts    |
| Pipeline integration       | Continuous validation                       | Setup complexity                   |
| Load tests                 | Detect scaling/performance issues          | Longer pipeline execution time     |

---

## Step 7 – Outcome

- LMS microservices are **fully testable, reliable, and maintainable**.
- Automated pipelines **validate functionality and contracts**.
- Safe evolution with **minimal risk of regression**.  