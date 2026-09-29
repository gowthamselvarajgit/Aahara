import React from 'react';
import { View, StyleSheet, ViewStyle } from 'react-native';
import { useTheme } from '../theme/useTheme';
import { AppText } from './AppText';
import { AaharaCard } from './AaharaCard';
import { ProgressBar } from './ProgressBar';

interface MetricCardProps {
  title: string;
  value: string | number;
  subtitle?: string;
  progress?: number;
  color?: string;
  icon?: React.ReactNode;
  style?: ViewStyle;
}

export const MetricCard: React.FC<MetricCardProps> = ({
  title,
  value,
  subtitle,
  progress,
  color,
  icon,
  style,
}) => {
  const theme = useTheme();
  const themeColor = color || theme.primary;

  return (
    <AaharaCard style={style} padding="md">
      <View style={styles.header}>
        <View style={styles.titleContainer}>
          {icon && <View style={styles.iconContainer}>{icon}</View>}
          <AppText variant="bodySmall" color="secondary">{title}</AppText>
        </View>
      </View>
      
      <View style={styles.content}>
        <AppText variant="metric">{value}</AppText>
        {subtitle && (
          <AppText variant="caption" color="muted" style={styles.subtitle}>
            {subtitle}
          </AppText>
        )}
      </View>

      {progress !== undefined && (
        <ProgressBar
          progress={progress}
          color={themeColor}
          style={styles.progress}
        />
      )}
    </AaharaCard>
  );
};

const styles = StyleSheet.create({
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 8,
  },
  titleContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  iconContainer: {
    marginRight: 4,
  },
  content: {
    flexDirection: 'row',
    alignItems: 'baseline',
    gap: 4,
    marginBottom: 12,
  },
  subtitle: {
    marginLeft: 4,
  },
  progress: {
    marginTop: 'auto',
  },
});
