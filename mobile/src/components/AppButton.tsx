import React from 'react';
import { TouchableOpacity, StyleSheet, ActivityIndicator, ViewStyle, TextStyle } from 'react-native';
import { useTheme } from '../theme/useTheme';
import { AppText } from './AppText';

interface AppButtonProps {
  title: string;
  onPress: () => void;
  variant?: 'primary' | 'secondary' | 'outline' | 'ghost';
  size?: 'sm' | 'md' | 'lg';
  loading?: boolean;
  disabled?: boolean;
  style?: ViewStyle;
  textStyle?: TextStyle;
  icon?: React.ReactNode;
}

export const AppButton: React.FC<AppButtonProps> = ({
  title,
  onPress,
  variant = 'primary',
  size = 'md',
  loading = false,
  disabled = false,
  style,
  textStyle,
  icon,
}) => {
  const theme = useTheme();

  const getBackgroundColor = () => {
    if (disabled) return theme.surfaceMuted;
    switch (variant) {
      case 'primary': return theme.primary;
      case 'secondary': return theme.primarySoft;
      case 'outline': return 'transparent';
      case 'ghost': return 'transparent';
      default: return theme.primary;
    }
  };

  const getTextColor = () => {
    if (disabled) return theme.textMuted;
    switch (variant) {
      case 'primary': return '#FFFFFF';
      case 'secondary': return theme.primary;
      case 'outline': return theme.primary;
      case 'ghost': return theme.primary;
      default: return '#FFFFFF';
    }
  };

  const getBorderColor = () => {
    if (variant === 'outline') return disabled ? theme.border : theme.primary;
    return 'transparent';
  };

  const getHeight = () => {
    switch (size) {
      case 'sm': return 36;
      case 'lg': return 56;
      case 'md':
      default: return 48;
    }
  };

  return (
    <TouchableOpacity
      onPress={onPress}
      disabled={disabled || loading}
      activeOpacity={0.8}
      style={[
        styles.button,
        {
          backgroundColor: getBackgroundColor(),
          borderColor: getBorderColor(),
          borderWidth: variant === 'outline' ? 1.5 : 0,
          height: getHeight(),
          borderRadius: theme.radius.full, // pill shaped
        },
        style,
      ]}
    >
      {loading ? (
        <ActivityIndicator color={getTextColor()} />
      ) : (
        <>
          {icon && icon}
          <AppText
            variant="button"
            style={[
              { color: getTextColor(), marginLeft: icon ? theme.spacing.sm : 0 },
              textStyle,
            ]}
          >
            {title}
          </AppText>
        </>
      )}
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  button: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    paddingHorizontal: 24,
  },
});
