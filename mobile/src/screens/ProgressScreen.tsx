import React from 'react';
import { View, StyleSheet, ScrollView } from 'react-native';
import { AppText } from '../components/AppText';
import { useTheme } from '../theme/useTheme';
import { AaharaCard } from '../components/AaharaCard';

export const ProgressScreen = () => {
  const theme = useTheme();
  
  return (
    <ScrollView style={[styles.container, { backgroundColor: theme.background }]} contentContainerStyle={styles.content}>
      <AppText variant="display" style={styles.header}>Progress</AppText>

      <AppText variant="subheading" style={styles.sectionTitle}>Weight</AppText>
      <AaharaCard style={styles.chartCard} padding="lg">
        <View style={styles.chartHeader}>
          <AppText variant="metric">75.2 kg</AppText>
          <AppText variant="bodySmall" color="success">-1.5 kg this month</AppText>
        </View>
        <View style={[styles.mockChart, { backgroundColor: theme.surfaceMuted }]} />
      </AaharaCard>

      <AppText variant="subheading" style={styles.sectionTitle}>Nutrition</AppText>
      <View style={styles.row}>
        <AaharaCard style={styles.halfCard} padding="md">
          <AppText variant="bodySmall" color="secondary">Average calories</AppText>
          <AppText variant="metric" style={{marginTop: 8}}>1,940</AppText>
          <AppText variant="caption" color="muted">kcal/day</AppText>
        </AaharaCard>

        <AaharaCard style={styles.halfCard} padding="md">
          <AppText variant="bodySmall" color="secondary">Average protein</AppText>
          <AppText variant="metric" style={{marginTop: 8}}>108</AppText>
          <AppText variant="caption" color="muted">g/day</AppText>
        </AaharaCard>
      </View>

      <AppText variant="subheading" style={styles.sectionTitle}>Strength</AppText>
      <AaharaCard style={styles.chartCard} padding="lg">
        <View style={styles.chartHeader}>
          <AppText variant="button">Bench Press</AppText>
          <AppText variant="metric" color="brand">60 kg</AppText>
        </View>
        <View style={[styles.mockChart, { backgroundColor: theme.surfaceMuted, height: 80 }]} />
      </AaharaCard>

      <AppText variant="subheading" style={styles.sectionTitle}>Workout frequency</AppText>
      <AaharaCard style={styles.statCard} padding="md">
        <AppText variant="metric">3</AppText>
        <AppText variant="body" color="secondary" style={{marginLeft: 8}}>sessions / week</AppText>
      </AaharaCard>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  content: {
    padding: 24,
    paddingBottom: 60,
  },
  header: {
    marginBottom: 24,
  },
  sectionTitle: {
    marginBottom: 16,
    marginTop: 8,
  },
  chartCard: {
    marginBottom: 24,
  },
  chartHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'baseline',
    marginBottom: 16,
  },
  mockChart: {
    height: 120,
    borderRadius: 8,
    width: '100%',
  },
  row: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 24,
  },
  halfCard: {
    flex: 1,
    marginHorizontal: 4,
  },
  statCard: {
    flexDirection: 'row',
    alignItems: 'baseline',
    marginBottom: 24,
  }
});
