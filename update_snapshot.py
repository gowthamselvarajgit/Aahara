import os

def update_diary_snapshot():
    path = "backend/src/main/java/com/aahara/backend/service/DiaryEntryService.java"
    with open(path, "r") as f:
        content = f.read()

    old_food_block = """        Map<String, Object> foodMap = new HashMap<>();
        foodMap.put("id", food.getId());
        foodMap.put("name", food.getName());
        foodMap.put("source", food.getSource());
        foodMap.put("datasetVersion", food.getDatasetVersion());
        snapshotMap.put("food", foodMap);
        
        if (portion != null) {
            Map<String, Object> portionMap = new HashMap<>();
            portionMap.put("id", portion.getId());
            portionMap.put("description", portion.getDescription());
            snapshotMap.put("portion", portionMap);
        }

        Map<String, BigDecimal> nutrientVals = new HashMap<>();
        
        for (FoodNutrient n : nutrients) {
            BigDecimal val = n.getAmountPer100g().multiply(factor).setScale(2, RoundingMode.HALF_UP);
            nutrientVals.put(n.getNutrientType().name(), val);
            
            switch (n.getNutrientType().name()) {
                case "CALORIES": entry.setCaloriesSnapshot(val); break;
                case "PROTEIN": entry.setProteinSnapshot(val); break;
                case "CARBOHYDRATE": entry.setCarbsSnapshot(val); break;
                case "FAT": entry.setFatSnapshot(val); break;
                case "FIBER": entry.setFiberSnapshot(val); break;
            }
        }
        snapshotMap.put("nutrients", nutrientVals);"""

    new_food_block = """        Map<String, Object> foodMap = new HashMap<>();
        foodMap.put("id", food.getId());
        foodMap.put("name", food.getName());
        foodMap.put("source", food.getSource());
        foodMap.put("datasetVersion", food.getDatasetVersion());
        foodMap.put("sourceRecordId", food.getSourceRecordId());
        snapshotMap.put("food", foodMap);
        
        Map<String, Object> portionMap = new HashMap<>();
        if (portion != null) {
            portionMap.put("id", portion.getId());
            portionMap.put("description", portion.getDescription());
            portionMap.put("quantity", request.getQuantity());
            portionMap.put("gramEquivalent", portion.getGramWeight().multiply(request.getQuantity()));
        } else {
            portionMap.put("description", "100g");
            portionMap.put("quantity", request.getQuantity());
            portionMap.put("gramEquivalent", new BigDecimal("100").multiply(request.getQuantity()));
        }
        snapshotMap.put("portion", portionMap);

        Map<String, Object> nutrientVals = new HashMap<>();
        
        for (FoodNutrient n : nutrients) {
            BigDecimal val = n.getAmountPer100g().multiply(factor).setScale(2, RoundingMode.HALF_UP);
            
            Map<String, Object> nutDetail = new HashMap<>();
            nutDetail.put("value", val);
            nutDetail.put("unit", getUnitForNutrient(n.getNutrientType().name()));
            nutrientVals.put(n.getNutrientType().name(), nutDetail);
            
            switch (n.getNutrientType().name()) {
                case "CALORIES": entry.setCaloriesSnapshot(val); break;
                case "PROTEIN": entry.setProteinSnapshot(val); break;
                case "CARBOHYDRATE": entry.setCarbsSnapshot(val); break;
                case "FAT": entry.setFatSnapshot(val); break;
                case "FIBER": entry.setFiberSnapshot(val); break;
            }
        }
        snapshotMap.put("nutrients", nutrientVals);"""

    content = content.replace(old_food_block, new_food_block)
    
    unit_method = """
    private String getUnitForNutrient(String type) {
        if ("CALORIES".equals(type)) return "kcal";
        if (type.contains("VITAMIN_B12") || type.contains("FOLATE") || type.contains("VITAMIN_D")) return "mcg";
        if (type.contains("SODIUM") || type.contains("POTASSIUM") || type.contains("CALCIUM") || type.contains("IRON") || type.contains("CHOLESTEROL") || type.contains("MAGNESIUM") || type.contains("ZINC") || type.contains("VITAMIN_C")) return "mg";
        return "g";
    }
"""
    # Insert helper method at end of class
    content = content.replace("}\n", unit_method + "}\n", 1)

    with open(path, "w") as f:
        f.write(content)

update_diary_snapshot()
print("Updated DiaryEntryService snapshot logic.")
