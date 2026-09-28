export const lightTheme = {
  background: '#F9FAFB',
  surface: '#FFFFFF',
  primary: '#059669', // Calm green
  secondary: '#0EA5E9',
  text: '#111827',
  mutedText: '#6B7280',
  border: '#E5E7EB',
  success: '#10B981',
  warning: '#F59E0B',
  error: '#EF4444',
  spacing: {
    xs: 4,
    sm: 8,
    md: 16,
    lg: 24,
    xl: 32,
  },
  radius: {
    sm: 4,
    md: 8,
    lg: 12,
    xl: 16,
    full: 9999,
  }
};

export const darkTheme = {
  ...lightTheme,
  background: '#111827',
  surface: '#1F2937',
  text: '#F9FAFB',
  mutedText: '#9CA3AF',
  border: '#374151',
};

export type Theme = typeof lightTheme;
