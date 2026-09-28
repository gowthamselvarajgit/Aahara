# Phase 2A API Security - Hardened

## 1. Authentication Architecture
The authentication system uses an external identity provider mapped to an internal `User` entity.
- Users authenticate via external providers (or DEV test provider).
- The identity maps to a JWT (HS256) standard payload.
- Spring Security uses a stateless `JwtAuthenticationFilter` to validate the `Authorization: Bearer <token>` header on incoming requests.

## 2. JWT Strategy & Configuration Hardening
- **Implementation:** `jjwt` (io.jsonwebtoken) library.
- **Signing Algorithm:** HMAC-SHA (HS256) using a 256-bit+ secret.
- **Secret Management:** 
  - The JWT Secret is supplied via the environment property `APP_JWT_SECRET`. 
  - The application configuration strictly requires this property via `@Value("${app.jwt.secret}")`. 
  - There is NO hardcoded fallback. If `APP_JWT_SECRET` is missing, Spring context fails to start, ensuring the application fails safely rather than running with an insecure default.
- **Validation:** The filter strictly verifies signature, expiration, and formatting. Invalid or missing tokens return a standard 401 Unauthorized error response.

## 3. Test JWT Secret
- Tests are isolated from production credentials.
- `application-test.properties` overrides `app.jwt.secret` with a dedicated dummy string (`aVeryLongAndSecureSecretKeyForTestingPurposesOnly1234567890!`), guaranteeing tests pass locally or on CI without exposing or requiring developer credentials.

## 4. Development Authentication Behavior
A `DevAuthController` (`/api/dev/auth/login`) is exposed solely for local development and test automation.
- **Profile Boundary:** Explicitly annotated with `@Profile({"dev", "test"})`. The production application startup will completely ignore this endpoint.
- **Impersonation Prevention:** Bound by `app.dev.allowed-email` (defaulting to `test@aahara.local`) or any email ending in `@test.com`. Arbitrary users cannot be impersonated via this endpoint.

## 5. Application User Mapping
The `User` entity includes fields explicitly tracked by Flyway V4 (`auth_provider`, `auth_provider_id`), verified locally against the MySQL `aahara` database.
- A unique index prevents identifier collisions across OIDC platforms.
- Does not strictly assume a Google identity.

## 6. Ownership Enforcement
- All secure API operations pull `authentication.getPrincipal()` to verify ownership.
- Client-supplied `userId` paths/bodies are strictly disregarded for identity mapping, fully mitigating IDOR against authentication endpoints.

## 7. API Error Contract
Errors map to a deterministic JSON layout without leaking stack traces or sensitive security components.
```json
{
  "timestamp": "2026-09-28T10:15:00",
  "status": 401,
  "code": "UNAUTHORIZED",
  "message": "JWT expired or signature invalid",
  "path": "/api/foods"
}
```

## 8. Public vs Protected Endpoints
**Intentionally Public:**
- `GET /api/public/ping` - Server health ping.
- `POST /api/dev/auth/login` (Only available in dev/test profiles).

**Protected (Require JWT):**
- `POST /api/calculations/targets`
- `GET /api/foods`
- `GET /api/foods/{id}`
- All future User CRUD operations.

## 9. Logging Rules
- No JWTs, passwords, or Authorization headers are logged by the application.
- Configuration loading ensures no explicit passwords output to console.

## 10. Local Environment Setup
- Developers should copy `.env.example` to `.env`.
- `.env` strictly remains in `.gitignore`.
- `.env.example` contains placeholders only, instructing developers to manually supply a strong string (at least 32 characters) for `APP_JWT_SECRET`.
