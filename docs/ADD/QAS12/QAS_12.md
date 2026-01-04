# ADD 12 – Release Strategy / Exposure

*Attribute-Driven Design (ADD) Full Documentation*

---

## 1. Overview

This ADD focuses on **Release Strategy and Exposure Management** for LMS microservices. The goal is to enable **controlled, gradual, and safe release of new features** while maintaining system stability and client compatibility.

- **Objective:** Deploy new features or services progressively to production, minimizing risk and user impact.
- **Functional Scope:** All LMS microservices (Books, Authors, Genres, Readers, Users).
- **Quality Attributes Addressed:** Deployability, Availability, Fault Tolerance, Modifiability, Observability.
- **Compatibility:** Existing clients should not experience errors due to feature rollout.
- **Extensibility:** Framework supports new release strategies for future services.

---

## Step 1 – Review Inputs

### 1.1 Objective

- Enable **gradual/progressive exposure** of new functionality.
- Support **dark launches** to test features internally without affecting end users.
- Provide **kill switch** capability to disable features immediately in case of problems.
- Implement **A/B testing** or **beta access** to validate user impact and adoption.

### 1.2 Problem Statement

- Full-scale deployment of new features risks **breaking existing clients**.
- Without controlled release, failures affect **availability, SLA, and user experience**.
- Lack of exposure management hinders **data-driven feature validation**.

### 1.3 Requirements (SMART)

| Type           | Requirement                                                                                   |
|----------------|-----------------------------------------------------------------------------------------------|
| **Specific**   | Implement release strategies: gradual, dark launch, kill switch, A/B testing.                 |
| **Measurable** | Pipeline logs and runtime metrics confirm correct exposure and rollback behavior.              |
| **Attainable** | Achievable using **feature flags, routing configuration, and CI/CD automation**.             |
| **Relevant**   | Critical for safe feature rollout, minimizing risk and user disruption.                        |
| **Time-Bound** | Implemented during the LMS microservices reengineering sprint.                                 |

**Variation and Evolution Points**

- **Variation points:** Exposure percentages, target user groups, feature flags.
- **Evolution points:** Supports additional release strategies for new services or experimental features.

---

## Step 2 – Establish Goals and Select Inputs

### 2.1 Drivers

| Type             | Driver                                                                                      |
|-----------------|---------------------------------------------------------------------------------------------|
| **Functional**  | Gradual release, feature toggles, beta access, A/B testing.                                   |
| **Quality**     | Availability, Deployability, Fault Tolerance, Observability.                                  |
| **Constraints** | Must not impact existing clients or violate SLAs.                                            |
| **Business**    | Enable safe validation of new features and data-driven adoption decisions.                  |

### 2.2 Design Decisions

- **Feature Flags:** Enable/disable features dynamically without redeploying.
- **Gradual Exposure:** Release features to a subset of users or traffic.
- **Dark Launch:** Internal testing of features without external visibility.
- **Kill Switch:** Immediate rollback capability for problematic features.
- **A/B Testing:** Route traffic to different feature versions to measure effectiveness.
- **Monitoring:** Collect metrics and logs to observe user behavior and detect anomalies.

---

## Step 3 – Choose Elements to Decompose

### 3.1 Selected Elements

| Element                  | Purpose                                                  |
|--------------------------|----------------------------------------------------------|
| Feature Flag Service      | Enable or disable features at runtime                    |
| Traffic Router / Gateway  | Route users based on exposure rules (gradual, A/B)      |
| CI/CD Pipeline            | Automate feature release and configuration              |
| Monitoring & Metrics      | Collect logs and usage metrics during rollout           |
| Rollback Mechanism        | Kill switch to revert features immediately             |

### 3.2 Rationale for Selection

- Allows **controlled, risk-mitigated feature deployment**.
- Ensures **rapid response to failures** without affecting production.
- Provides **visibility for adoption and user feedback** before full rollout.

---

## Step 4 – Choose Design Concepts

### 4.1 Tactics

| Quality Attribute      | Tactic                       | Description                                          | Implementation                                           |
|------------------------|------------------------------|----------------------------------------------------|---------------------------------------------------------|
| Deployability          | Feature Flags                | Toggle features without redeployment               | Use configuration service or environment flags        |
| Availability           | Gradual / Canary Release     | Expose features to subset of users                | Route percentage of traffic to new version            |
| Fault Tolerance        | Kill Switch                  | Disable problematic features immediately          | Pipeline or feature flag triggers rollback            |
| Observability          | Metrics & Monitoring         | Track adoption, errors, and performance           | Prometheus/Grafana dashboards                          |
| Testability            | Dark Launch                  | Internal feature validation                        | Limit exposure to internal users or staging           |
| Modifiability          | A/B Testing                  | Route users to different versions for evaluation  | Gateway routing rules or load balancer configuration |

### 4.2 Reference Architecture / Patterns

- **Feature Flag Service:** Centralized control for toggling features.
- **Traffic Routing / Gateway:** Route users according to exposure rules.
- **Blue-Green or Canary Integration:** Combine exposure strategy with deployment strategy.
- **Monitoring & Logging:** Track feature usage, errors, and adoption metrics.

### 4.3 Architectural Design Alternatives and Rationale

| Alternative                    | Description                                  | Advantages                        | Disadvantages                  | Decision    |
|--------------------------------|----------------------------------------------|-----------------------------------|--------------------------------|-------------|
| Full release                    | Deploy feature to all users immediately      | Simple, fast                       | High risk, no rollback          | ❌ Rejected |
| Manual rollout                  | Developer controls exposure manually         | Low setup                           | Error-prone, slow               | ❌ Rejected |
| Controlled release with feature flags | Gradual, A/B, dark launch with kill switch | Safe, flexible, measurable        | Requires pipeline and monitoring setup | ✅ Selected |

---

## Step 5 – Instantiate Elements, Allocate Responsibilities, Define Interfaces

| Component                  | Responsibility                                      | Interface/Methods                               |
|----------------------------|----------------------------------------------------|------------------------------------------------|
| Feature Flag Service        | Enable/disable features dynamically               | enableFeature(), disableFeature(), setPercentage() |
| Traffic Router / Gateway    | Route users according to strategy                 | routeUser(), configureA/B(), configureCanary()|
| CI/CD Pipeline              | Automate deployment and feature exposure         | deployFeature(), updateFlags()                 |
| Monitoring & Metrics        | Collect usage and error metrics                   | trackUsage(), trackErrors(), reportMetrics()   |
| Rollback Mechanism          | Revert features immediately if failure detected  | triggerKillSwitch(), rollbackFeature()        |

---

## Step 6 – Sketch Views and Record Decisions

### 6.1 Trade-Off Analysis

| Decision                               | Benefit                                     | Cost                                     |
|----------------------------------------|--------------------------------------------|-----------------------------------------|
| Feature flags + progressive release    | Safe deployment, minimal user impact       | Pipeline and service setup complexity   |
| Dark launch                             | Validate internally before external release| Requires staging or internal users      |
| Kill switch                             | Immediate rollback capability               | Slight operational overhead             |
| A/B testing                             | Measure adoption and performance            | Additional routing and metric collection |

### 6.2 Outcome

- New features can be **safely released progressively**.
- **Internal validation** possible before full rollout.
- **Rapid rollback** ensures stability and availability.
- Provides **data-driven insights** via A/B testing and metrics.

---

## Step 7 – Perform Analysis of Current Design and Review Iteration Goals

- Monolithic LMS lacked **controlled release**, risking client disruption.
- Independent microservices allow **feature-specific exposure** using pipelines and flags.
- Metrics-driven approach enables **safe adoption and rollback**.
- Supports **continuous delivery and progressive exposure** of new functionality.

**Result:** LMS microservices support **gradual release, dark launch, kill switch, and A/B testing**, ensuring **controlled, safe, and observable feature deployment**.
