# API Design

## Principles
- RESTful resource naming.
- Plural nouns for collections (`/api/foods`, `/api/workouts`).
- Request and Response DTOs to hide internal JPA entities.
- Standard HTTP status codes (200 OK, 201 Created, 400 Bad Request, 404 Not Found).

## Core Endpoints
- `GET /api/ping` - Health check.
- `POST /api/users` - Create user.
- `GET /api/users/{id}/profile` - Get biometrics and targets.
- `PUT /api/users/{id}/profile` - Update biometrics (recalculates targets).
- `GET /api/foods/search?q={query}` - Search foods (supports English/Tamil).
- `POST /api/diary` - Log a food entry.
- `GET /api/diary?date={yyyy-MM-dd}` - Get food logs for a date.
- `POST /api/workouts` - Log a workout session.
- `GET /api/workouts/history` - Paginated workout history.
