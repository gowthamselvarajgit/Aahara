# Phase 2B User Data CRUD & Offline Sync Foundation

## 1. Domain Model
The Phase 2B architecture extends the core V2 schema to support User Data CRUD operations while ensuring a rock-solid Offline Sync Foundation.
Key Entities:
- **DiaryEntry**: Logs food consumption. References `Food` and `FoodPortion`.
- **WaterLog**: Logs water consumption in milliliters.
- **WorkoutSession**: A distinct workout occurrence linked to a user.
- **WorkoutSet**: A nested entity capturing strength/cardio details (weight, reps, duration).

## 2. Ownership Model
Security and Ownership are strictly derived from the validated JWT token in the `SecurityContext`.
- Client-supplied `userId` paths or request body properties are systematically rejected or disregarded.
- `DiaryEntryService`, `WaterLogService`, and `WorkoutSessionService` explicitly accept the `userId` passed from the Controller (which obtains it from `authentication.getName()`).
- Nested resources like `WorkoutSet` enforce ownership by validating that the parent `WorkoutSession` belongs to the authenticated user.

## 3. API Endpoints
**Diary API**
- `POST /api/diary` - Create new diary entry.
- `GET /api/diary?from=&to=&page=&size=` - Paginated date range lookup.
- `GET /api/diary/{id}` - Fetch single entry.
- `DELETE /api/diary/{id}` - Soft delete entry.
- `GET /api/diary/summary?date=` - Fetches the daily calculated summary (macros/water).

**Water API**
- `POST /api/water`
- `GET /api/water?date=`
- `DELETE /api/water/{id}`

**Workout API**
- `POST /api/workouts`
- `GET /api/workouts?from=&to=&page=&size=`
- `GET /api/workouts/{id}`
- `PUT /api/workouts/{id}`
- `DELETE /api/workouts/{id}`

**Workout Sets API**
- `POST /api/workouts/{sessionId}/sets`
- `GET /api/workouts/{sessionId}/sets`
- `DELETE /api/workouts/{sessionId}/sets/{setId}`

## 4. DTO Contracts
Entities are fully shielded. 
- Input requires `*RequestDto` (e.g. `DiaryEntryRequestDto`).
- Output uses `*ResponseDto` populated via service-level mapping. 

## 5. Validation Rules
Jakarta Bean Validation enforces fundamental safety:
- Date fields cannot be null.
- Quantities, amounts, reps, weights must be `@Positive` or `@PositiveOrZero`.
- `FoodId` and `SessionId` must exist before creating relations. 

## 6. Nutrition Snapshot Schema
Historical immutability is vital.
- The `DiaryEntry` calculates nutrition *on the server* upon creation (using the `Food` and `FoodPortion` models).
- Values are hard-copied into snapshot columns (`calories_snapshot`, `protein_snapshot`, `food_name_snapshot`). 
- Master food definitions (`Food`, `FoodNutrient`) use `ON DELETE RESTRICT` (established in V3) preventing destructive cascading that ruins historical snapshots. 

## 7. Daily Summary Calculations
`DailySummaryService` computes absolute totals for calories, protein, carbs, fat, fiber, and water for a given day.
- Derived percentages (`current / target * 100`) are computed dynamically and are *never* persisted.
- Division by zero is protected (returns `null` if the target is 0 or absent).
- Values above 100% are fully permitted.

## 8. Delete Semantics
- User data (Diary, Water, Workouts, Sets) is **Soft Deleted** by populating the `deleted_at` timestamp.
- This ensures offline mobile clients can still download tombstone records and delete them locally during sync.

## 9. Transactions
All write operations (`create`, `update`, `delete`) in the services are annotated with `@Transactional`. 
Read operations use `@Transactional(readOnly = true)`.

## 10. Pagination
- Handled natively via Spring Data `Pageable`.
- Maximum page sizes are enforced in controllers (`size = Math.min(size, 100)`).

## 11. Offline Sync Architecture
To accommodate the future React Native SQLite client:
- **Timestamps**: Every table utilizes `created_at` and `updated_at`.
- **Soft Deletes**: `deleted_at` enables downstream removal.
- **Idempotency**: `client_id` added to track UUIDs generated on the mobile device. Database constraints enforce `UNIQUE(user_id, client_id)` preventing duplicate records caused by retry loops on bad network connections.
- **Concurrency**: Added `@Version` to entities leveraging JPA Optimistic Locking to gracefully reject stale updates.

## 12. Security Model
- JWT explicitly determines `userId`.
- No endpoints expose passwords or PII.
- All errors funnel through `GlobalExceptionHandler`.
