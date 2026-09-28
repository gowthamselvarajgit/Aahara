# Data Import Pipeline

## Overview
The food data pipeline transforms raw CSV/JSON dumps from sources (USDA, ICMR) into Aahara's normalized database format.

## Stages
1. **Raw Source**: Original CSVs placed in `data/raw/`.
2. **Parser**: Scripts read the source format mapping IDs to standard fields.
3. **Normalization**: 
   - Energy is checked against macros (P*4 + C*4 + F*9). Discrepancies > 10% are flagged.
   - States (raw, cooked) are explicitly extracted from the name.
4. **Deduplication**: Alias matching.
5. **Quality Validation**: Null checks for core macros.
6. **Database Seed**: Processed data is exported as SQL or JSON for Flyway/Seeder consumption.

*Note: In V1, we will insert a curated priority dataset of Tamil/Indian foods directly via Flyway baseline migrations.*
