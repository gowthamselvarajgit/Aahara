# Aahara Phase 4: Mobile Foundation

## React Native Architecture
Aahara Mobile uses a robust React Native + TypeScript foundation. It relies on the standard RN CLI (not Expo) to ensure long-term uncompromised access to native Android/iOS APIs.

## Navigation Architecture
Powered by `React Navigation`, `RootNavigator` hosts a Stack containing `MainTabs`, which delegates to five core foundation screens:
- `HomeScreen`
- `FoodScreen` 
- `WorkoutScreen`
- `ProgressScreen`
- `ProfileScreen`

## State & Data
- **Zustand:** Responsible purely for transient UI and application state (e.g. AuthStore, NetworkStore).
- **SQLite:** Responsible for robust, persistent Offline-First domain data (`local_foods`, `local_diary_entries`). 
- **Repository Pattern:** Separates database access out of components (`DiaryRepository.ts`).

## Offline-First Principles
All user logging flows through local SQLite first. Operations instantly write to local tables with a metadata `sync_status = 'pending_create' | 'pending_update' | 'pending_delete'`. The UI instantly reflects local reads.

## API & Authentication
- **API Client:** Configurable Axios instance (`apiClient.ts`) mapped cleanly to `API_BASE_URL`. Automatically handles HTTP structured error interception matching Spring Boot's API contract.
- **Authentication:** `useAuthStore.ts` delegates Token I/O to platform-agnostic secure storage abstractions.

## Sync Architecture & Conflict Handling
A foundational `SyncService` is established to independently orchestrate `pull/push` reconciliations. 
Currently deferred to Phase 5. In the future, 409 responses from backend optimistic locking will be tagged locally to resolve conflicts based on canonical server timestamps.

## WHAT PHASE 4 DOES NOT IMPLEMENT
- No complete food logging UI.
- No complete workout UI.
- No Google OAuth production integration (purely architectural stub).
- No large food dataset SQLite imports.
- No AI or recommendations.
- No production bi-directional sync engine.
- No social features.
