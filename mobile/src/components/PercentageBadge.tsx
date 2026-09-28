import React from 'react';
import { View, StyleSheet } from 'react-native';
import { AppText } from './AppText';
import { useTheme } from '../theme/useTheme';

interface Props {
  current: number;
  target: number;
  unit: string;
  label: string;
}

export const PercentageBadge: React.FC<Props> = ({ current, target, unit, label }) => {
  const theme = useTheme();
  
  // Do not store precomputed percentage in DB. Calculate safely at render time.
  const percentage = target > 0 ? Math.round((current / target) * 100) : 0;
  
  return (
    <View style={[styles.container, { backgroundColor: theme.surface, borderColor: theme.border }]}>
      <AppText variant="caption" color="muted">{label}</AppText>
      <AppText variant="h2">{current} / {target} {unit}</AppText>
      <View style={[styles.badge, { backgroundColor: theme.primary }]}>
        <AppText color="default" style={{ color: '#fff' }} variant="caption">
          {percentage}%
        </AppText>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    padding: 12,
    borderRadius: 8,
    borderWidth: 1,
    alignItems: 'center',
    marginVertical: 4,
  },
  badge: {
    marginTop: 8,
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 12,
  }
});
