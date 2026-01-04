# Technical Memo – QAS 16: Operability

## Issue
Monolithic LMS had **limited operational visibility**, making **failure detection, troubleshooting, and scaling** slow and error-prone.

## Problem
- Lack of centralized logs and metrics makes identifying issues difficult.
- Manual intervention is required for maintenance and fault recovery.
- No automated alerts or dashboards reduce operational efficiency.
- Scaling and monitoring are inconsistent across services.

## Summary of Solution
Implement **operability and observability** for LMS microservices:

- **Centralized Logging:** Collect logs from all services (ELK/Loki stack).
- **Metrics & Dashboards:** Monitor system performance and usage (Prometheus + Grafana).
- **Health Checks:** Readiness and liveness endpoints to detect service availability.
- **Alerting System:** Automated notifications (Slack/Email) on failures.
- **Operational Visibility:** Dashboards and alerts enable proactive maintenance.

## Factors
- Detect failures **quickly (<5 min)** to minimize downtime.
- Provide **real-time system visibility** for operators.
- Ensure **scalable monitoring** for all microservices.
- Use **container-friendly tools** compatible with orchestrated environments.

## Solution
- **Centralized Logging:** Aggregate logs from all services.
- **Metrics Collection:** Track CPU, memory, requests, and errors.
- **Health Endpoints:** Standardized `/health` and `/ready` endpoints.
- **Dashboards:** Visualize system state and performance trends.
- **Automated Alerts:** Notify operators on service failures or threshold breaches.

## Motivation
- Increase **operational efficiency** and maintainability.
- Enable **early detection** of failures.
- Reduce **SLA violations** and downtime.
- Support **proactive monitoring** and troubleshooting.

## Alternatives

| Alternative              | Description                                | Advantages                        | Disadvantages                     | Decision |
|--------------------------|--------------------------------------------|-----------------------------------|-----------------------------------|----------|
| Local logs per service   | Logs stored per container                  | Simple to implement               | Hard to correlate, no aggregation | Rejected |
| Basic metrics only       | CPU/memory stats per service               | Low setup effort                  | Limited visibility                | Rejected |
| Full observability stack | Logging + metrics + health checks + alerts | Centralized, proactive monitoring | Requires setup and maintenance    | Selected |

## Pending Issues
- Scaling alert thresholds as service count increases.
- Ensuring dashboards are updated for new microservices.
- Integrating operability tools with CI/CD pipelines for automated reporting.
