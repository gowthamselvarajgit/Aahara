export interface User {
  id: string;
  email: string;
  profile?: UserProfile;
}

export interface UserProfile {
  id: string;
  userId: string;
  targetCalories?: number;
}

export interface Food {
  id: string;
  name: string;
  tamilName?: string;
  brand?: string;
  category?: string;
  foodState?: 'RAW' | 'COOKED' | 'BOILED' | 'FRIED' | 'STEAMED' | 'BAKED' | 'PREPARED' | 'PACKAGED' | 'UNKNOWN';
  source?: string;
  datasetVersion?: string;
  sourceRecordId?: string;
  license?: string;
  status?: 'ACTIVE' | 'DEPRECATED' | 'LEGAL_REVIEW_REQUIRED' | 'USER_CREATED' | 'INTERNAL_CURATED';
  isVerified?: boolean;
  ownerUserId?: string;
}

export interface FoodPortion {
  id: string;
  foodId: string;
  description: string;
  gramWeight: number;
}

export interface FoodNutrient {
  id: string;
  foodId: string;
  nutrientType: string;
  amountPer100g: number;
}

export interface DiaryEntry {
  id: string;
  userId: string;
  entryDate: string; // ISO Date
  mealType: 'BREAKFAST' | 'LUNCH' | 'DINNER' | 'SNACK';
  foodId: string;
  portionId?: string;
  quantity: number;
  nutrientsSnapshotJson: string; // Stored JSON
  clientId?: string;
  syncId?: string;
  version: number;
}

export interface ApiError {
  status: number;
  message: string;
  details?: string[];
  timestamp?: string;
}

export interface PaginatedResponse<T> {
  items: T[];
  page: number;
  pageSize: number;
  totalItems: number;
  totalPages: number;
}
