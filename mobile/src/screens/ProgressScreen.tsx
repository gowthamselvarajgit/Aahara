import React from 'react';
import { View, StyleSheet, ScrollView } from 'react-native';
import { AppText } from '../components/AppText';
import { useTheme } from '../theme/useTheme';
import { AaharaCard } from '../components/AaharaCard';
import { MetricCard } from '../components/MetricCard';
import { ProgressBar } from '../components/ProgressBar';

export const ProgressScreen = () => {
  const theme = useTheme();
  
  return (
    <ScrollView style={[styles.container, { backgroundColor: theme.background }]} contentContainerStyle={styles.content}>
      <AppText variant="display" style={styles.header}>Progress</AppText>

      <AppText variant="subheading" style={styles.sectionTitle}>Weight Goal</AppText>
      <AaharaCard style={styles.chartCard} padding="lg">
        <View style={styles.chartHeader}>
          <View>
            <AppText variant="bodySmall" color="secondary">Current</AppText>
            <AppText variant="metric">75.2 kg</AppText>
          </View>
          <View style={{ alignItems: 'flex-end' }}>
            <AppText variant="bodySmall" color="secondary">Target</AppText>
            <AppText variant="metric">70.0 kg</AppText>
          </View>
        </View>
        
        <ProgressBar progress={0.65} color={theme.primary} height={12} style={styles.progressBar} />
        
        <View style={styles.chartFooter}>
          <AppText variant="caption" color="success">↓ 1.5 kg this month</AppText>
          <AppText variant="caption" color="muted">5.2 kg to go</AppText>
        </View>
      </AaharaCard>

      <AppText variant="subheading" style={styles.sectionTitle}>Nutrition</AppText>
      <View style={styles.row}>
        <MetricCard 
          title="Avg Calories" 
          value="1,940" 
          subtitle="kcal/day" 
          color={theme.nutrition} 
          style={styles.halfCard} 
        />
        <MetricCard 
          title="Avg Protein" 
          value="108" 
          subtitle="g/day" 
          color={theme.primary} 
          style={styles.halfCard} 
        />
      </View>

      <AppText variant="subheading" style={styles.sectionTitle}>Strength</AppText>
      <AaharaCard style={styles.chartCard} padding="lg">
        <View style={styles.chartHeader}>
          <AppText variant="button">Bench Press</AppText>
          <AppText variant="metric" color="workout">60 kg</AppText>
        </View>
        {/* Mock Chart Area */}
        <View style={styles.mockChartContainer}>
          <View style={[styles.mockBar, { height: '30%' }]} />
          <View style={[styles.mockBar, { height: '45%' }]} />
          <View style={[styles.mockBar, { height: '60%' }]} />
          <View style={[styles.mockBar, { height: '80%' }]} />
          <View style={[styles.mockBar, { height: '100%', backgroundColor: theme.workout }]} />
        </View>
        <View style={styles.chartFooter}>
          <AppText variant="caption" color="muted">Week 1</AppText>
          <AppText variant="caption" color="workout">Current</AppText>
        </View>
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
  progressBar: {
    marginVertical: 12,
  },
  chartFooter: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginTop: 8,
  },
  mockChartContainer: {
    height: 120,
    flexDirection: 'row',
    alignItems: 'flex-end',
    justifyContent: 'space-between',
    paddingTop: 16,
    marginBottom: 8,
  },
  mockBar: {
    width: 32,
    backgroundColor: '#E6F0EA', // primarySoft
    borderRadius: 4,
  },
  row: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 24,
    marginHorizontal: -4,
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
