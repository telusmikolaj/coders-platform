Coders Platform

Project Description

"Coders" is a coding task management system comprised of four microservices, integrated with GitHub for task verification and grading. The goal of the project is to provide a robust and scalable platform for teachers to assign programming tasks and for students to submit their solutions.

The project aims to follow best practices of software development, continuous integration and deployment using Docker, Kubernetes and Github Actions.

Microservices

The application is divided into the following microservices:

User Service: Manages user accounts including login, registration, and profile management.
Task Service: Manages tasks including creation, editing, and deletion of tasks.
Submission Service: Handles task submissions by students and the evaluation of those submissions.
Grading Service: Processes the results of JUnit tests and sends grading results to the Submission Service.
All microservices are individually containerized using Docker and managed by Kubernetes.

Technology Stack

Backend: Spring Boot (Java)
Database: PostgreSQL, MongoDB
Containerization: Docker
Orchestration: Kubernetes
CI/CD: Github Actions
Logging: Elasticsearch, Logstash, Kibana (ELK Stack)
Monitoring: Prometheus and Grafana
Security: Keycloak
Installation

Detailed instructions for setting up each microservice, along with their respective Docker and Kubernetes configurations, are provided within the individual directories for each microservice. Please refer to these instructions for setting up the development and production environments.