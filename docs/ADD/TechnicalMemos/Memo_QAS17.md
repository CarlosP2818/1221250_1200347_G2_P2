# Technical Memo – QAS 17: Testability

## Issue
Monolithic LMS was **difficult to test in isolation**, making it hard to ensure reliability and correctness across service changes.

## Problem
- Changes in one part of the system could **break unrelated functionality**.
- No automated validation of service contracts or integration points.
- Lack of isolated testing prevents safe continuous delivery and automated regression detection.

## Summary of Solution
Implement **testability for LMS microservices** with automated pipelines:

- **Unit Tests:** Test individual service classes in isolation.
- **Integration Tests:** Validate interactions between services in controlled environments.
- **CDC Tests:** Consumer-driven contract validation for event/message interactions.
- **Load & Performance Tests:** Detect performance bottlenecks and scalability issues.
- **CI/CD Integration:** Automate test execution, reporting, and validation.

## Factors
- Services must be **isolatable** for accurate unit testing.
- Integration and contract testing must validate **cross-service interactions**.
- Automated pipelines enforce **continuous validation**.
- Support **future service additions** without reworking test infrastructure.

## Solution
- **Service Isolation & Mocking:** Use Mockito or test doubles for unit tests.
- **CDC Testing:** Validate asynchronous messaging via Pact.
- **Pipeline Integration:** Run all tests automatically using Jenkins, GitHub Actions, or equivalent.
- **Load/Performance Validation:** Use JMeter or Gatling to simulate traffic and detect scaling issues.

## Motivation
- Ensure LMS services are **reliable and maintainable**.
- Detect regressions **before production deployment**.
- Support **continuous delivery** and safe evolution of microservices.
- Provide **confidence in integration and contract compliance**.

## Alternatives

| Alternative                     | Description                            | Advantages                      | Disadvantages                          | Decision |
|---------------------------------|----------------------------------------|---------------------------------|----------------------------------------|----------|
| Manual testing                  | Run tests manually                     | Simple to start                 | Time-consuming, error-prone            | Rejected |
| Unit tests only                 | Test classes independently             | Fast execution                  | Misses integration and contract issues | Rejected |
| Full automated testing pipeline | Unit + integration + CDC + load tests  | Reliable, continuous validation | Setup effort and pipeline complexity   | Selected |

## Pending Issues
- Maintaining contract tests as events or APIs evolve.
- Scaling pipeline execution as microservices count grows.
- Integrating load/performance tests without affecting production data.
