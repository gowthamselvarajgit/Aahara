# Phase 1.5 Contracts

## Numeric and Percentage Representation

Aahara supports displaying nutrition and progress metrics in both absolute numeric values and percentages. To ensure data integrity, historical reproducibility, and robust UI rendering, the backend domain model and API contracts adhere to the following principles.

### 1. Absolute Numeric Values
The backend acts as the source of truth for all quantitative measurements (grams, milligrams, calories, milliliters). Numeric values are persisted with high precision in the database. Percentages are dynamically derived, not redundantly stored.

### 2. Target Completion Percentages
Used to track progress against a defined daily target (e.g., Calories: 1650 / 2200 kcal).
- **Formula:** `(Current Value / Target Value) * 100`
- **Missing Target:** If a nutrient has no established target or reference value for the user, no percentage is calculated (returns `null`), and the UI will strictly display the numeric amount.
- **Over-completion:** Percentages can gracefully exceed 100% (e.g., 120%) without throwing errors.

### 3. Macro Calorie-Composition Percentages
Used to represent the caloric distribution of the user's *actual intake*, independently of their targets.
- **Formula:** `((Macro Grams * Atwater Factor) / Total Logged Calories) * 100`
- **Atwater Factors Used:** Protein (4 kcal/g), Carbohydrates (4 kcal/g), Fat (9 kcal/g).
- This metric represents "Protein contributed 30% of logged calories today", ensuring target vs. composition metrics remain distinctly separate in API payloads.

### 4. Reference-Based Micronutrient Percentages
Certain micronutrients (Iron, Calcium, Vitamin C) utilize population-based reference daily intake values (e.g., ICMR-NIN RDA) when user-specific targets are unavailable.
- If a standard reference exists, it acts as the denominator.
- If no reference exists, the API provides only the absolute logged numeric value.

### 5. Progress Percentages
General metrics like weight goal progress or workout volume use the exact same calculation pattern.
- Every percentage must have a strictly defined denominator/baseline.
- Meaningless percentages (e.g., comparing arbitrary unrelated measurements) are strictly prohibited at the domain level.

### 6. Denominator Requirements (Zero-Safety)
- **Zero Denominator Protection:** Division-by-zero is strictly checked. If the denominator (Target, Reference, or Logged Total Calories) is zero or null, the calculated percentage cleanly returns `null`.
- The system will *never* invent percentages if the denominator is unavailable.

### 7. Rounding and Precision Policy
- **Storage:** Database decimals retain precision (`DECIMAL(8,2)` or `DECIMAL(10,3)` depending on the metric).
- **Calculations:** Internal Java `BigDecimal` calculations retain up to 4 decimal places of precision (`RoundingMode.HALF_UP`).
- **Presentation:** The API exposes the precise numeric and fractional percentage (e.g., `72.45%`). The mobile UI is responsible for formatting this to a whole number (`72%`) for display. Calculation precision is never destroyed prematurely.

### 8. Historical-Data Behavior
Historical diary snapshots strictly persist the *underlying numeric nutrient quantities* consumed (e.g., 20g of Protein).
- We **DO NOT** persist historical percentage completions (e.g., `protein_percentage: 68`).
- Storing percentages would permanently corrupt historical context if the user later updates their body weight and targets.
- For historical review, percentages are dynamically re-calculated on the fly using the *target configuration that was active on that historical date* (if target versioning is implemented) or omitted in favor of pure numeric display.
