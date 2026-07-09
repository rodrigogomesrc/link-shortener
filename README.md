# Short URL Platform

A distributed URL shortener platform built to study and apply modern software architecture concepts, cloud-native technologies, design patterns, and backend engineering best practices.

This project is not only focused on delivering features, but also on exploring how scalable and resilient systems are designed in real-world environments.

---

# Purpose

The main goal of this project is to serve as a practical learning environment for:

- Distributed systems
- Microservices architecture
- Event-driven communication
- Authentication and authorization
- Scalability strategies
- Fault tolerance
- Backend engineering best practices

---

# Architecture Overview

The platform is being designed around a microservices architecture with independent services responsible for:

- URL creation
- URL redirection
- Analytics processing
- User management
- Authentication and authorization

The system also includes:

- API Gateway
- Service Discovery (to be implemented)
- Centralized Configuration
- Asynchronous messaging 

---

# Technologies

## Already Applied

- **Java 25**
- **Spring Boot**
- **Spring Cloud**
- **Spring Gateway**
- **Spring Config Server**
- **Redis**
- **Resilience4J**
- **RabbitMQ**
- **JUnit**
- **Keycloak**
- **OAuth2**

## Planned / In Progress

- **Spring Discovery Server**
- **React**
- **K6**
- **Grafana**
- **Cypress**
---

# Architectural Patterns

## Already Applied 

- **Microservices**
- **CQRS**
- **Fault Tolerance**
- **Circuit Breaker**
- **Rate Limiter**

## Planned 

- Asynchronous Communication (Messaging)

---

# Engineering Practices

## Already Applied

- **Clean Code**
- **SOLID**

## Planned / In Progress

- CI/CD
- Observability (Monitoring)
- Structured Logging
- Load Testing
- Automatic Testing

---

# Main Components

| Service | Responsibility | Status |
|---|---|---|
| API Gateway | Central entry point for requests | Created |
| Short URL Service | URL creation and management | Created |
| Redirect Service | Fast URL resolution and redirection | Created |
| Projector Service | Project the write model to the read model  | Created |
| Analytics Service | Asynchronous analytics processing | Planned |
| User Service | User management | Planned |
| Keycloak | Authentication and authorization | Planned |

---

# Current Architectural Goals

- Low-latency redirects
- Scalable read operations
- Asynchronous analytics processing
- Service decoupling
- Independent service scaling
- Centralized authentication
- Fault-tolerant communication

---

# Future Improvements

- Distributed tracing
- Metrics dashboard
- Real-time analytics
- Multi-instance deployment
- Read replicas

---

# Status

This project is under active development and continuously evolving as new concepts and technologies are studied and applied.
