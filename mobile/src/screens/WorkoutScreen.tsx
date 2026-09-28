import React from 'react';
import { View, StyleSheet } from 'react-native';
import { AppText } from '../components/AppText';
import { useTheme } from '../theme/useTheme';

export const WorkoutScreen = () => {
  const theme = useTheme();
  return (
    <View style={[styles.container, { backgroundColor: theme.background }]}>
      <AppText variant="h1">WorkoutScreen</AppText>
      <AppText variant="body" color="muted">Foundation scaffold</AppText>
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, alignItems: 'center', justifyContent: 'center', padding: 16 }
});
