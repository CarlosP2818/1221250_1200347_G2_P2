# Technical Memo – QAS 10: Elasticity / Auto-Scaling

## Issue
The monolithic LMS cannot dynamically adjust resources under variable load, leading to **performance degradation** and **wasted resources** during low traffic periods.

## Problem
- Fixed resource allocation prevents handling **peak loads efficiently**.
- Services cannot scale independently; a single overloaded module can degrade the entire system.
- Manual or static scaling wastes hardware during idle periods and risks downtime under spikes.

## Summary of Solution
Implement **metrics-driven auto-scaling** for LMS microservices:

- **Metrics Collector:** Collect CPU, memory, and request rate per service.
- **Scaling Scripts:** Trigger horizontal scaling (up/down) based on thresholds.
- **Orchestration Tool:** Docker/Kubernetes manages replicas and ensures **minimum service instances** for availability.
- **Load Balancer:** Routes traffic to active instances automatically.
- **Service Isolation:** Each microservice scales independently without impacting others.

## Factors
- Maintain **API contract** for existing clients.
- Handle **sporadic peak loads** without downtime.
- Optimize hardware usage during **low-demand periods**.
- Enable **new services** to adopt the same scaling rules automatically.

## Solution
- **Dynamic Resource Allocation:** Scale replicas based on workload.
- **Horizontal Scaling:** Add/remove instances to match demand.
- **Minimum Service Instances:** Maintain at least 1-2 replicas to guarantee availability.
- **Modular Scaling:** Each service scales independently to optimize resources.
- **Load Test Validation:** Verify behavior under realistic traffic patterns.

## Motivation
- Achieve **elasticity**, reducing idle hardware consumption.
- Ensure **high performance** under peak loads.
- Maintain **availability** and SLA compliance.
- Provide **resource efficiency** while supporting future service growth.

## Alternatives

| Alternative               | Description                            | Advantages                        | Disadvantages                    | Decision |
|---------------------------|----------------------------------------|-----------------------------------|----------------------------------|----------|
| Manual scaling            | Dev manually adjusts instances         | Simple, no automation             | Slow response, human error       | Rejected |
| Fixed replicas            | Static number of instances per service | Predictable resource usage        | Wasteful, insufficient for peaks | Rejected |
| Auto-scaling with scripts | Dynamic scaling based on metrics       | Efficient, responsive, elastic    | Slight orchestration complexity  | Selected |

## Pending Issues
- Tuning scaling thresholds for optimal performance.
- Coordinating auto-scaling with other LMS quality attributes (availability, fault tolerance).
- Monitoring for abnormal spikes that may trigger rapid scaling oscillations.