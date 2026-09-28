# Aahara Phase 3: Nutrition Data Foundation

## 1. Nutrition Data Architecture
Aahara separates canonical master food data from historical user diary entries and custom user-created foods. 
- **Food:** Core identity of an edible item (id, name, tamilName, brand, category, foodState, status, ownerUserId).
- **FoodPortion:** Represents serving definitions (id, description, gramWeight).
- **FoodNutrient:** Represents standard macro/micronutrients per 100g, dynamically mapped using `NutrientType` (now expanded to 26+ nutrients).
- **FoodAlias:** Supports English and Tamil alternative names/transliterations for optimized searching.

## 2. Source & Provenance Strategy
All foods ingested from external datasets track their exact origin:
- `source`: E.g. "IFCT 2017" or "USDA"
- `datasetVersion`: E.g. "v1"
- `sourceRecordId`: Original canonical ID from the source dataset.
- `license`: Distribution terms.

*Note: Publicly downloadable does not automatically mean commercially redistributable. Aahara designates sources that require evaluation as `LEGAL_REVIEW_REQUIRED`.*

## 3. Dataset Versioning & Idempotency
- Re-importing a dataset operates idempotently by checking the unique composite key `(source, datasetVersion, sourceRecordId)`.
- Updates do not blindly overwrite historical diary entries (see Historical Snapshot Contract).

## 4. Import Pipeline
The `DataImportService` implements a strict pipeline:
1. **Load/Parse:** Reads JSON/CSV.
2. **Normalization:** Trims strings, collapses whitespace.
3. **Idempotency:** Looks up existing records.
4. **Validation:** Rejects negative nutrients. Checks macroscopic energy estimates.
5. **Ingestion:** Writes Food, Portions, Nutrients, and Aliases transactionally.

## 5. Historical Snapshot Contract
To protect user diaries from master data mutations, every `DiaryEntry` stores a `nutrients_snapshot_json` at the time of logging:
```json
{
  "schemaVersion": 1,
  "food": { "id": "...", "name": "Apple", "source": "USDA", "datasetVersion": "v1" },
  "portion": { "id": "...", "description": "1 Medium" },
  "nutrients": { "CALORIES": 52.0, "PROTEIN": 0.3 }
}
```
If the original food is deprecated, updated, or modified, the diary entry's numeric calculation and visual string rendering remain strictly frozen.

## 6. Search Strategy
- Foods are searched using a combination of direct string matching (`LOWER(f.name) LIKE ...`) and exact/prefix mapping against `FoodAlias`.
- All searches are deterministic, paginated, and non-fuzzy to prevent expensive unbounded database table scans.

## 7. Status & Separation
Foods default to `ACTIVE`. 
Custom user foods are marked as `USER_CREATED` and tied via `ownerUserId`.
Deprecating a food sets its status to `DEPRECATED`, hiding it from standard searches but retaining it in the database for foreign key constraints. Hard-deletes are forbidden for active dietary histories.
