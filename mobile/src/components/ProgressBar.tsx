import React from 'react';
import { View, StyleSheet, ViewStyle } from 'react-native';
import { useTheme } from '../theme/useTheme';

interface ProgressBarProps {
  progress: number; // 0 to 1
  color?: string;
  backgroundColor?: string;
  height?: number;
  style?: ViewStyle;
}

export const ProgressBar: React.FC<ProgressBarProps> = ({
  progress,
  color,
  backgroundColor,
  height = 8,
  style,
}) => {
  const theme = useTheme();
  
  const clampedProgress = Math.min(Math.max(progress, 0), 1);
  const barColor = color || theme.primary;
  const bgColor = backgroundColor || theme.surfaceMuted;

  return (
    <View style={[styles.container, { height, backgroundColor: bgColor, borderRadius: height / 2 }, style]}>
      <View
        style={[
          styles.fill,
          {
            width: `${clampedProgress * 100}%`,
            backgroundColor: barColor,
            borderRadius: height / 2,
          }
        ]}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    width: '100%',
    overflow: 'hidden',
  },
  fill: {
    height: '100%',
  },
});
