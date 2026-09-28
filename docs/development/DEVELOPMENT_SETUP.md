# Development Setup

This document describes how to set up the development environment for **Aahara** on a Windows machine.

## Prerequisites
- **Operating System**: Windows 10/11
- **Node.js**: v22.x (installed)
- **npm**: v10.x (installed)
- **Java**: JDK 21 (install required for Spring Boot).
- **Maven**: 3.9.9 installed in `D:\Aahara\tools\apache-maven-3.9.9`. `PATH` and `JAVA_HOME` (`C:\Users\gowth\.jdks\ms-21.0.6`) are configured.
- **Gradle**: *optional* – required only if you prefer Gradle over Maven for the backend.
- **Android SDK**: *required* for React Native Android builds. Install via Android Studio.
- **Android Studio**: *recommended* for emulator and SDK management.
- **MySQL**: v8.x (installed). Ensure you have a user with privileges to create databases.
- **Git**: installed (v2.45)
- **Python**: v3.13 (installed) – useful for data pipelines.
- **Docker**: *optional* – can be used to run MySQL or other services in containers.

## Steps
1. **Clone the repository** (already local).
2. **Install Node dependencies**:
   ```
   cd mobile
   npm install
   ```
3. **Backend setup**:
   ```
   cd ../backend
   mvn clean install   # after Maven is installed
   ```
4. **Create `.env`** from the example:
   ```
   cp .env.example .env
   ```
   Fill in the appropriate values.
5. **Database**:
   - Ensure MySQL is running.
   - Create the `health_tracker` database (see `docs/database/DATABASE_ARCHITECTURE.md`).
   - Run Flyway migrations (will be added later).
6. **Android setup**:
   - Install Android Studio.
   - Accept SDK licenses.
   - Add `%ANDROID_HOME%` to your environment variables.
7. **Run the app**:
   - Mobile (dev): `npm run android` or `npm run ios`.
   - Backend: `mvn spring-boot:run`.

Refer to each sub‑project's `README.md` for more details.

## Architecture Documentation
Before contributing, please review the core design documents located in `docs/architecture/`:
- [ARCHITECTURE.md](docs/architecture/ARCHITECTURE.md)
- [DATABASE_DESIGN.md](docs/architecture/DATABASE_DESIGN.md)
- [DOMAIN_MODEL.md](docs/architecture/DOMAIN_MODEL.md)
- [CALCULATION_METHODOLOGY.md](docs/architecture/CALCULATION_METHODOLOGY.md)
- [DATA_SOURCES.md](docs/architecture/DATA_SOURCES.md)
- [DATA_IMPORT_PIPELINE.md](docs/architecture/DATA_IMPORT_PIPELINE.md)
- [API_DESIGN.md](docs/architecture/API_DESIGN.md)
- [SYNC_STRATEGY.md](docs/architecture/SYNC_STRATEGY.md)
