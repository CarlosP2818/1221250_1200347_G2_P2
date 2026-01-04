# ADD 16 – Operability

*Attribute-Driven Design (ADD) Full Documentation*

---

## 1. Overview

This ADD focuses on **Operability**: ensuring LMS microservices are **easy to operate, monitor, and manage**.

- **Objective:** Provide observability, automated monitoring, and fault management.
- **Quality Attributes Addressed:** Operability, Observability, Maintainability.
- **Functional Scope:** All microservices, health checks, logs, dashboards, and alerting.

---

## Step 1 – Review Inputs

### 1.1 Objective
- Centralize logging, metrics, and monitoring.
- Implement health checks and automated alerts.
- Provide operational dashboards for runtime visibility.

### 1.2 Problem Statement
- Monolithic LMS had **limited operational visibility**.
- Troubleshooting and failure detection were slow.
- Scaling and maintenance required **manual intervention**.

### 1.3 Requirements (SMART)

| Type           | Requirement                                                  |
|----------------|--------------------------------------------------------------|
| **Specific**   | Implement centralized monitoring and logging.               |
| **Measurable** | Alerts triggered within <5 min for failed services.         |
| **Attainable** | Use Prometheus, Grafana, ELK stack, and health endpoints.   |
| **Relevant**   | Reduce downtime and operational risk.                       |
| **Time-Bound** | Completed within LMS reengineering sprint.                  |

---

## Step 2 – Establish Goals

| Type             | Driver                                         |
|-----------------|------------------------------------------------|
| Functional       | Health checks, logs, metrics                  |
| Quality          | Observability, Fault Detection                |
| Constraints      | Use container-based monitoring tools          |
| Business         | Fast issue detection, SLA adherence           |

### 2.2 Design Decisions
- **Centralized Logging:** ELK stack or Loki.
- **Metrics & Dashboards:** Prometheus + Grafana.
- **Health Checks:** Endpoint-based readiness/liveness checks.
- **Alerting:** Email/Slack notifications for failures.

---

## Step 3 – Elements to Decompose

| Element           | Purpose                                  |
|------------------|-----------------------------------------|
| Logs              | Record service events                    |
| Metrics           | Track performance, errors, traffic       |
| Health Checks     | Detect service availability              |
| Dashboard         | Visualize system health                  |
| Alert System      | Notify operators of issues               |

---

## Step 4 – Design Concepts

| Quality Attribute | Tactic                       | Description                     | Implementation                   |
|-----------------|-------------------------------|---------------------------------|---------------------------------|
| Operability     | Centralized Logging           | Collect logs from all services  | ELK/Loki stack                  |
| Operability     | Health Checks                 | Monitor service availability    | /health endpoints, readiness/liveness probes |
| Observability   | Metrics & Dashboards          | Monitor performance and usage   | Prometheus + Grafana             |
| Operability     | Alerting                      | Notify on failures              | Slack/Email alerts               |

---

## Step 5 – Instantiate Elements

| Component          | Responsibility                               | Interface/Methods                  |
|-------------------|----------------------------------------------|----------------------------------|
| Logging Stack      | Collect service logs                          | log(), error(), info()            |
| Prometheus/Grafana | Monitor metrics and display dashboards       | collectMetrics(), displayDashboard() |
| Health Endpoints   | Report service readiness                      | GET /health, GET /ready           |
| Alerting System    | Send alerts when thresholds breached         | triggerAlert(service, metric)     |

---

## Step 6 – Trade-Off Analysis

| Decision                   | Benefit                                  | Cost                               |
|----------------------------|-----------------------------------------|-----------------------------------|
| Centralized logging         | Easier troubleshooting                   | Extra storage and setup           |
| Metrics dashboards          | Real-time visibility                      | Maintenance effort                 |
| Health checks & alerts      | Faster failure response                    | Requires integration with pipeline |

---

## Step 7 – Outcome

- LMS is **observable, operable, and manageable**.
- Failures are **detected early** and notified automatically.
- Operations team can **proactively monitor** system health.