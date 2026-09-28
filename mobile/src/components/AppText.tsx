import React from 'react';
import { Text, TextProps } from 'react-native';
import { useTheme } from '../theme/useTheme';
import { typography } from '../theme/typography';

interface AppTextProps extends TextProps {
  variant?: keyof typeof typography;
  color?: 'primary' | 'secondary' | 'muted' | 'brand' | 'error' | 'success' | 'warning';
}

export const AppText: React.FC<AppTextProps> = ({ 
  style, 
  variant = 'body', 
  color = 'primary', 
  children, 
  ...props 
}) => {
  const theme = useTheme();

  const getColor = () => {
    switch(color) {
      case 'secondary': return theme.textSecondary;
      case 'muted': return theme.textMuted;
      case 'brand': return theme.primary;
      case 'error': return theme.error;
      case 'success': return theme.success;
      case 'warning': return theme.warning;
      case 'primary':
      default: return theme.textPrimary;
    }
  };

  return (
    <Text 
      style={[
        typography[variant],
        { color: getColor() },
        style
      ]} 
      {...props}
    >
      {children}
    </Text>
  );
};
