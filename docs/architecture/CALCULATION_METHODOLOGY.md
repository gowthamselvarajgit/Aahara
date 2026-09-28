# Calculation Methodology

## BMR / REE
**Mifflin-St Jeor Equation** is used due to its high accuracy across diverse populations.
- Men: `(10 * weight_kg) + (6.25 * height_cm) - (5 * age_years) + 5`
- Women: `(10 * weight_kg) + (6.25 * height_cm) - (5 * age_years) - 161`

## Total Daily Energy Expenditure (TDEE)
`TDEE = BMR * Activity Multiplier`
- Sedentary: 1.2
- Lightly Active: 1.375
- Moderately Active: 1.55
- Very Active: 1.725
- Extra Active: 1.9

## Goal Adjustment
- LOSE_WEIGHT: TDEE - 500 kcal
- MAINTAIN_WEIGHT: TDEE
- GAIN_WEIGHT: TDEE + 500 kcal

## Macronutrient Split (Configurable per user)
Default Methodology (based on ICMR 2020 RDA guidelines for active populations):
- **Protein**: 1.0g per kg of body weight (capped at 35% of total calories to prevent extreme outputs).
- **Fat**: 30% of target calories.
- **Carbohydrates**: Remainder of target calories.

*Note: The previous assumption of 30/40/30 was an arbitrary ratio and has been replaced with this weight-based calculation to align more closely with physiological guidelines.*

## Estimated 1RM
**Epley Formula**: `Weight * (1 + (Reps / 30))`
- Limitations: Less accurate for rep ranges > 10.
