import os

path = "backend/src/main/java/com/aahara/backend/service/DiaryEntryService.java"
with open(path, "r") as f:
    content = f.read()

unit_method = """
    private String getUnitForNutrient(String type) {
        if ("CALORIES".equals(type)) return "kcal";
        if (type.contains("VITAMIN_B12") || type.contains("FOLATE") || type.contains("VITAMIN_D")) return "mcg";
        if (type.contains("SODIUM") || type.contains("POTASSIUM") || type.contains("CALCIUM") || type.contains("IRON") || type.contains("CHOLESTEROL") || type.contains("MAGNESIUM") || type.contains("ZINC") || type.contains("VITAMIN_C")) return "mg";
        return "g";
    }
"""

content = content[:content.rfind("}")] + unit_method + "}\n"

with open(path, "w") as f:
    f.write(content)
