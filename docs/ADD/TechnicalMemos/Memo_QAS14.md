# Technical Memo – QAS 14: Deployment & Rollout Strategy

## Issue
Monolithic LMS deployments caused **downtime and user disruption**, and rolling out new versions manually is **risky and error-prone**.

## Problem
- Deploying new versions without isolation can **break running services**.
- Manual intervention slows deployment and increases **human error**.
- Lack of controlled rollout prevents **zero-downtime releases**.
- Operators have limited visibility into deployment health and status.

## Summary of Solution
Implement **automated, progressive deployment strategies** for LMS microservices:

- **CI/CD Pipeline:** Automates build, deploy, rollout, and rollback.
- **Load Balancer / API Gateway:** Routes traffic between old and new versions.
- **Feature Flags:** Enable selective exposure or beta testing of new functionality.
- **Health Checks:** Validate readiness of new deployments before full exposure.
- **Monitoring Dashboard:** Provides real-time visibility into deployment performance and errors.
- **Rollback Mechanism:** Automatically revert to previous stable version if issues occur.

## Factors
- Maintain **availability and SLA compliance** during deployment.
- Reduce risk of **widespread failures** with progressive rollout.
- Provide **real-time visibility** for operators.
- Support **future release types** (A/B testing, dark launches) seamlessly.

## Solution
- **Blue/Green Deployment:** Deploy new version in parallel; switch traffic after validation.
- **Canary Release:** Route a subset of users to new version; monitor metrics before full rollout.
- **Rolling Update:** Incrementally update service instances to minimize disruption.
- **Automated Rollback:** Revert deployment automatically on health check failures or metric degradation.
- **Monitoring & Alerts:** Track latency, errors, and performance throughout rollout.

## Motivation
- Ensure **zero-downtime deployments**.
- Minimize operational risk while releasing new features.
- Maintain **high availability and fault tolerance**.
- Provide **repeatable, safe, and observable release strategies** for all LMS services.

## Alternatives

| Alternative                   | Description                         | Advantages                          | Disadvantages                       | Decision |
|-------------------------------|-------------------------------------|-------------------------------------|-------------------------------------|----------|
| Full immediate release        | Deploy feature to all users at once | Simple, fast                        | High risk of downtime/errors        | Rejected |
| Manual staged release         | Human-managed incremental rollout   | Low technical effort                | Slow, error-prone                   | Rejected |
| Automated progressive rollout | Canary, Blue/Green, Rolling Update  | Zero-downtime, low-risk, observable | Requires pipeline and orchestration | Selected |

## Pending Issues
- Tune canary percentage and rollout duration for optimal risk mitigation.
- Integrate rollback criteria with monitoring thresholds carefully.
- Ensure operators are trained on dashboard and alert responses.