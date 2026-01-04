# ADD 14 – Deployment & Rollout Strategy

*Attribute-Driven Design (ADD) Full Documentation*

---

## 1. Overview

This ADD focuses on **deployment and rollout strategies** for LMS microservices. The goal is to **release new service versions safely, progressively, and with minimal impact on users**.

- **Objective:** Enable zero-downtime deployments with controlled exposure.
- **Functional Scope:** All LMS microservices (Books, Authors, Genres, Readers, Users).
- **Quality Attributes Addressed:** Availability, Deployability, Fault Tolerance, Observability.
- **Compatibility:** Existing clients remain unaffected during deployment.
- **Extensibility:** Strategy accommodates new services and release types (internal beta, gradual release, A/B testing).

---

## Step 1 – Review Inputs

### 1.1 Objective

- Deploy new versions **without affecting running services**.
- Reduce risk via **progressive rollout strategies**.
- Enable **automatic rollback** if issues are detected.
- Provide **visibility into deployment status** for operators.

### 1.2 Problem Statement

- Monolithic LMS deployments caused **downtime and user disruption**.
- Rolling out new versions is **risky without isolation**.
- Manual intervention slows release and increases **human error**.

### 1.3 Requirements (SMART)

| Type           | Requirement                                                                                   |
|----------------|-----------------------------------------------------------------------------------------------|
| **Specific**   | Deploy services using Canary, Blue/Green, or Rolling Update strategies.                        |
| **Measurable** | Zero-downtime deployment; automated rollback triggers if validation fails.                    |
| **Attainable** | Use CI/CD pipelines, load balancers, feature flags, and monitoring dashboards.                |
| **Relevant**   | Reduces service downtime, improves user experience, and minimizes deployment risk.            |
| **Time-Bound** | Implemented for all services in the current LMS reengineering sprint.                         |

**Variation and Evolution Points**

- **Variation points:** Canary percentage, duration of testing, approval gates.
- **Evolution points:** Additional rollout strategies (A/B testing, dark launches) can be added without affecting existing services.

---

## Step 2 – Establish Goals and Select Inputs

### 2.1 Drivers

| Type             | Driver                                                                                      |
|-----------------|---------------------------------------------------------------------------------------------|
| **Functional**  | Progressive rollout, safe deployments, automatic rollback                                   |
| **Quality**     | Availability, Deployability, Fault Tolerance, Observability                                  |
| **Constraints** | CI/CD-driven deployment, minimal user impact                                                 |
| **Business**    | Maintain SLA and user trust; reduce downtime risk                                           |

### 2.2 Design Decisions

- **Blue/Green Deployments:** Deploy new version in parallel; switch traffic after validation.
- **Canary Releases:** Deploy to a subset of users first, monitor behavior, then expand.
- **Rolling Updates:** Update services incrementally across instances.
- **Automated Rollback:** Revert deployment if health checks fail or metrics degrade.
- **Monitoring & Alerting:** Track errors, response times, and user experience during rollout.

---

## Step 3 – Choose Elements to Decompose

### 3.1 Selected Elements

| Element                  | Purpose                                                   |
|--------------------------|-----------------------------------------------------------|
| CI/CD Pipeline            | Automate deployment, approvals, and rollout strategy      |
| Load Balancer             | Control traffic between old and new versions              |
| Feature Flags             | Enable selective exposure of new functionality           |
| Health Checks             | Validate service readiness and detect failures            |
| Monitoring Dashboard      | Track errors, latency, and performance metrics           |
| Rollback Mechanism        | Revert to previous stable version automatically          |

### 3.2 Rationale for Selection

- **Automation** reduces human error and speeds deployment.
- **Progressive rollout** reduces risk of widespread failures.
- **Monitoring and rollback** protect availability and user experience.
- **Feature flags** allow safe internal testing before full release.

---

## Step 4 – Choose Design Concepts

### 4.1 Tactics

| Quality Attribute      | Tactic                       | Description                                           | Implementation                                      |
|------------------------|------------------------------|-----------------------------------------------------|----------------------------------------------------|
| Availability           | Blue/Green Deployment         | Parallel environment with switch-over              | Deploy new version in parallel, shift traffic     |
| Availability           | Canary Release                | Gradual traffic exposure                             | Route % of users to new version, monitor metrics  |
| Fault Tolerance        | Automated Rollback            | Revert faulty deployment automatically             | CI/CD triggers rollback on failed tests           |
| Observability          | Monitoring & Health Checks    | Detect failures and performance degradation        | Prometheus/Grafana dashboards, alerting          |
| Deployability          | CI/CD Pipeline Integration    | Automate deployment, approvals, and rollout       | GitOps or Jenkins/CI pipeline with deployment scripts |

### 4.2 Reference Architecture / Patterns

- **CI/CD Pipeline:** Orchestrates deployments, approvals, and rollbacks.
- **Load Balancer / API Gateway:** Routes traffic to old/new versions based on strategy.
- **Feature Flags:** Control gradual exposure and beta testing.
- **Monitoring & Alerts:** Prometheus, Grafana, Slack/Email notifications.

### 4.3 Architectural Design Alternatives and Rationale

| Alternative                    | Description                                  | Advantages                        | Disadvantages                  | Decision    |
|--------------------------------|----------------------------------------------|-----------------------------------|--------------------------------|-------------|
| Full immediate release          | Deploy new version to all users at once     | Simple, fast                       | High risk of downtime/errors    | ❌ Rejected |
| Manual staged release           | Human-managed incremental rollout          | Low technical effort                | Slow, error-prone               | ❌ Rejected |
| Automated progressive rollout   | Canary, Blue/Green, Rolling Update         | Zero-downtime, low-risk, observable| Requires pipeline and orchestration | ✅ Selected |

---

## Step 5 – Instantiate Elements, Allocate Responsibilities, Define Interfaces

| Component                  | Responsibility                                        | Interface/Methods                               |
|----------------------------|----------------------------------------------------|------------------------------------------------|
| CI/CD Pipeline               | Automate build, deploy, rollout, and rollback       | pipeline scripts, approval gates              |
| Load Balancer / API Gateway  | Route traffic between versions                        | routeTraffic(oldVersion, newVersion, %users) |
| Feature Flags                | Enable gradual exposure or beta testing             | enableFeature(flag), disableFeature(flag)    |
| Health Checks                | Validate new deployment before full exposure        | GET /health, alertOnFailure()                |
| Monitoring Dashboard         | Observe rollout performance and errors              | showMetrics(), sendAlert()                    |
| Rollback Mechanism           | Revert to previous stable deployment                | triggerRollback(version)                      |

---

## Step 6 – Sketch Views and Record Decisions

### 6.1 Trade-Off Analysis

| Decision                               | Benefit                                     | Cost                                     |
|----------------------------------------|--------------------------------------------|-----------------------------------------|
| Canary Release                          | Low-risk gradual rollout                    | Slightly longer deployment time          |
| Blue/Green Deployment                    | Zero-downtime switch-over                    | Requires duplicate environment resources |
| Rolling Update                           | Incremental update without full restart    | Coordination complexity                   |
| Automated Rollback                        | Protect availability and user experience   | Must define rollback conditions carefully|
| Monitoring & Alerts                       | Early detection of failures                 | Initial setup and maintenance effort     |

### 6.2 Outcome

- New LMS versions are **released progressively**, with controlled user exposure.
- **Zero downtime** for production traffic.
- **Automatic rollback** ensures fault tolerance.
- Operators have **real-time visibility** into deployment health.
- Release strategies are **repeatable and safe** for all microservices.

---

## Step 7 – Perform Analysis of Current Design and Review Iteration Goals

- Monolithic LMS deployments caused **service downtime and high risk**.
- Progressive deployment strategies ensure **availability and fault tolerance**.
- Monitoring and rollback mechanisms protect **user experience**.
- CI/CD integration enables **repeatable, safe, and automated releases**.

**Result:** LMS microservices can be deployed safely using **Canary, Blue/Green, or Rolling Update strategies**, ensuring high availability, controlled exposure, and minimal operational risk.
