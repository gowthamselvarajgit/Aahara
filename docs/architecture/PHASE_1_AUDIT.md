# Phase 1 Architecture Audit

## A. What was correct
- Normalized MySQL schema.
- Primary key strategy using UUIDv4 for all tables to support offline sync.
- Use of Flyway for versioned migrations.
- Epley 1RM formula implementation was fundamentally correct.
- Mifflin-St Jeor usage for TDEE.

## B. What was incorrect
- **Dangerous Cascades:** `FOREIGN KEY ... ON DELETE CASCADE` on historical tables like `diary_entries` referencing master data (`foods`). If a food was deleted, it would destroy user history.
- **Macronutrient Claims:** Claiming 30/40/30 was an "ICMR standard" was inaccurate. ICMR defines protein based on body weight (0.83g-1.0g/kg).
- **Medical Presentation of Water:** Water target was hard-coded to 35ml/kg without documenting it as a non-clinical baseline estimate.

## C. What was incomplete
- **Historical Nutrition Snapshots:** Diary entries snapshotted 4 macros but failed to support the 26+ micronutrients without schema bloat.
- **Licensing:** IFCT commercial licensing was too optimistically described.

## D. What was potentially risky
- Tests lacked edge-case validation for the `OneRepMaxService`.
- Allowing JPA to validate physical schema constraints (`ddl-auto=validate`) caused failures due to Spring's naming strategy mismatches with Flyway (e.g., `target_carbsg` vs `target_carbs_g`).

## E. What was corrected
- **V3 Flyway Migration Created:**
  - Dropped dangerous `CASCADE` foreign keys and replaced with `RESTRICT` for `diary_entries` and `workout_sets`.
  - Added `nutrients_snapshot_json` (JSON type) to `diary_entries` to immutably snapshot the entire 26+ nutrient profile at logging time.
- **Calculation Service:** Rewrote protein logic to 1.0g/kg body weight, bounded by a 35% calorie safety cap. Fat set to 30%, Carbs set to remainder.
- **Licensing Docs:** IFCT explicitly marked `REQUIRES LEGAL REVIEW` for commercial payload distribution.
- **Tests Added:** Added `OneRepMaxServiceTest` validating edge cases (0 reps, negative weights). Updated `CalculationServiceTest`.

## F. What remains intentionally deferred
- REST Controllers.
- Mobile UI and Data Sync implementation.
- Spring Security OAuth mechanisms.

## G. Database migration changes
- Applied `V3__audit_corrections.sql`.

## H. Calculation changes
- Updated protein derivation methodology.
- Documented water estimates as non-clinical.

## I. Data licensing concerns
- Re-emphasized the necessity of legal clearance for IFCT data if used in a paid application context.

## J. Testing changes
- Tests fully disconnected from local MySQL credentials, utilizing H2 memory DB.
