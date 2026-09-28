export const lightTheme = {
  background: '#F9FAED', // warm cream / soft off-white
  surface: '#FFFFFF',
  surfaceElevated: '#FFFFFF',
  surfaceMuted: '#F0F2E6',
  textPrimary: '#2D332F', // charcoal
  textSecondary: '#546059',
  textMuted: '#8B9992',
  primary: '#2B5A41', // deep natural green
  primarySoft: '#E6F0EA', 
  nutrition: '#4A7C59', // fresh green
  nutritionSoft: '#EBF3ED',
  workout: '#D97757', // subtle warm accent
  workoutSoft: '#F9EBE6',
  success: '#38A169',
  warning: '#DD6B20',
  error: '#E53E3E',
  border: '#E2E8E4',
  spacing: {
    xs: 4,
    sm: 8,
    md: 16,
    lg: 24,
    xl: 32,
    xxl: 48,
  },
  radius: {
    sm: 8,
    md: 16,
    lg: 24, // 20-28dp card corner radius
    xl: 32,
    full: 9999,
  }
};

export const darkTheme = {
  ...lightTheme,
  background: '#121614', // deep charcoal
  surface: '#1E2421',
  surfaceElevated: '#262D29',
  surfaceMuted: '#1A1F1C',
  textPrimary: '#F2F5F3',
  textSecondary: '#A9B5AE',
  textMuted: '#6D7973',
  primary: '#85C7A3', 
  primarySoft: '#1A2F24',
  nutrition: '#66A979',
  nutritionSoft: '#1C3123',
  workout: '#E29A80',
  workoutSoft: '#3D251D',
  border: '#36403A',
};

export type Theme = typeof lightTheme;
