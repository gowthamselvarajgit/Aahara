# Architecture Overview

Aahara is organized as a **monorepo** containing two primary sub‑projects:

```
Aahara/
├─ mobile/          # React Native (TypeScript) client
├─ backend/         # Spring Boot (Java) API server
├─ docs/            # Project documentation
└─ ...
```

### Local‑First Flow
1. **React Native** stores user data in an on‑device **SQLite** database.
2. An **application sync layer** (future) keeps a copy of the data locally and periodically pushes/pulls to the **backend REST API**.
3. The **backend** persists data in **MySQL** and provides additional business‑logic, authentication, and integration points.

The mobile app can operate fully offline; all core calculations (once implemented) will run on‑device using the local copy of the food and exercise datasets.

### Backend Layered Architecture (Spring Boot)
- **Controller** – HTTP endpoint definitions.
- **Service** – Business logic, transaction handling.
- **Domain / Model** – Core entities.
- **Repository** – JPA data access.
- **DTO / Mapper** – API contracts and conversion.
- **Configuration** – Beans, security, Flyway, validation.

The layered approach keeps concerns separate, eases testing, and supports future extensions (e.g., micro‑services).
