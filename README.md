# Spring Security Study

This project is a personal study and reference implementation of Spring Security.
It is based on concepts and practices explained in *[Spring Security: Start Here](https://www.manning.com/books/spring-start-here)* by *Laurentiu Spilca*, but goes beyond simply following the book.

The security concepts are implemented in the context of a basic Identity & Access Management (IAM) service, including user provisioning, account lifecycle operations, role management, token lifecycle management, and multiple authentication flows.

The goal of this project is twofold:
- To deepen my understanding of Spring Security by implementing the security layer in a clean, structured way.
- To create a reusable reference project I can revisit when applying security in future Spring projects.

While inspired by the book, the implementation reflects my own design decisions, explorations, and thought process.

## Technical Highlights
- **Spring Boot + Spring Security**: Core setup for authentication and authorization.
- **JWT-based Authentication**: Stateless security layer for API endpoints.
- **OAuth2 Login**: Integration with an external identity provider for modern authentication.
- **Token Lifecycle Management**: Refresh token support and revocation handling.
- **User Lifecycle Management**: User registration, invite-based onboarding, account blocking, role assignment.
- **Role-based Access Control (RBAC)**: Fine-grained authorization within an identity and access management domain.
- **Clean Architecture**: Organized structure for clarity and future reusability.
- **Docker**: Containerized PostgreSQL setup for consistent local development environment.

### Planned
- **Testing Strategy (BDD)**: Behavior-driven testing approach to validate critical IAM flows.
- **Database Migrations (Flyway)**: Structured schema versioning and repeatable database setup.


> This repository is actively maintained and continues to evolve as new features are added.
