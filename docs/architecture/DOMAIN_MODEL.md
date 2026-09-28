# Domain Model

## Identity Domain
- **User**: The root entity for authentication and ownership.
- **UserProfile**: Biometrics, targets, and goals. 

## Nutrition Master Domain
- **Food**: Represents a consumable item. Has a `state` (raw, cooked) and `source` metadata.
- **FoodPortion**: Defines serving sizes (e.g., "1 cup", "1 piece") and their weight in grams.
- **FoodNutrient**: Contains the value of a specific nutrient (e.g., Protein, Vitamin C) per 100g of the Food.

## User Nutrition Domain
- **DiaryEntry**: A historical log of food consumed. It snapshots the name, serving, and core macros to preserve history.

## Fitness Master Domain
- **Exercise**: A specific movement. Contains muscle group, equipment, and tracking type.

## User Fitness Domain
- **Routine**: A template containing an ordered list of exercises and target parameters.
- **WorkoutSession**: A historical instance of a workout, bounded by a start and end time.
- **WorkoutSet**: A single set (weight/reps/duration/distance) tied to a session and an exercise.
