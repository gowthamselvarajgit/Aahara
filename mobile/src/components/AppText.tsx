import React from 'react';
import { Text, TextProps, StyleSheet } from 'react-native';
import { useTheme } from '../theme/useTheme';

interface AppTextProps extends TextProps {
  variant?: 'h1' | 'h2' | 'body' | 'caption';
  color?: 'default' | 'muted' | 'primary' | 'error';
}

export const AppText: React.FC<AppTextProps> = ({ 
  style, 
  variant = 'body', 
  color = 'default', 
  children, 
  ...props 
}) => {
  const theme = useTheme();

  const getFontSize = () => {
    switch(variant) {
      case 'h1': return 24;
      case 'h2': return 20;
      case 'caption': return 12;
      case 'body':
      default: return 16;
    }
  };

  const getColor = () => {
    switch(color) {
      case 'muted': return theme.mutedText;
      case 'primary': return theme.primary;
      case 'error': return theme.error;
      case 'default':
      default: return theme.text;
    }
  };

  return (
    <Text 
      style={[
        { 
          fontSize: getFontSize(), 
          color: getColor(),
          fontWeight: variant === 'h1' || variant === 'h2' ? 'bold' : 'normal'
        }, 
        style
      ]} 
      {...props}
    >
      {children}
    </Text>
  );
};
