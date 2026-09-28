import React from 'react';
import { View, ViewProps, StyleSheet, TouchableOpacity } from 'react-native';
import { useTheme } from '../theme/useTheme';

interface AaharaCardProps extends ViewProps {
  variant?: 'elevated' | 'muted' | 'default';
  padding?: 'sm' | 'md' | 'lg';
  onPress?: () => void;
}

export const AaharaCard: React.FC<AaharaCardProps> = ({ 
  style, 
  variant = 'elevated',
  padding = 'lg',
  onPress,
  children,
  ...props 
}) => {
  const theme = useTheme();

  const getBackgroundColor = () => {
    if (variant === 'elevated') return theme.surfaceElevated;
    if (variant === 'muted') return theme.surfaceMuted;
    return theme.surface;
  };

  const getElevation = () => {
    if (variant === 'elevated') {
      return {
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 4 },
        shadowOpacity: 0.05,
        shadowRadius: 12,
        elevation: 3,
      };
    }
    return {};
  };

  const content = (
    <View
      style={[
        {
          backgroundColor: getBackgroundColor(),
          borderRadius: theme.radius.lg,
          padding: theme.spacing[padding],
        },
        getElevation(),
        style
      ]}
      {...props}
    >
      {children}
    </View>
  );

  if (onPress) {
    return (
      <TouchableOpacity activeOpacity={0.8} onPress={onPress}>
        {content}
      </TouchableOpacity>
    );
  }

  return content;
};
