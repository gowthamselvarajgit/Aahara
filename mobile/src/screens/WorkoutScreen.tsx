import React from 'react';
import { View, StyleSheet, ScrollView } from 'react-native';
import { AppText } from '../components/AppText';
import { useTheme } from '../theme/useTheme';
import { AaharaCard } from '../components/AaharaCard';
import { ThreeDAsset } from '../components/ThreeDAsset';

export const WorkoutScreen = () => {
  const theme = useTheme();
  
  return (
    <ScrollView style={[styles.container, { backgroundColor: theme.background }]} contentContainerStyle={styles.content}>
      <AppText variant="subheading" color="secondary" style={styles.dateLabel}>Today's Workout</AppText>
      <AppText variant="display" style={styles.header}>Upper Body</AppText>

      <View style={styles.heroAssetContainer}>
        <ThreeDAsset assetName="dumbbell" size={200} />
      </View>
      
      <View style={styles.durationBadge}>
        <AppText variant="metric" color="brand">45 min</AppText>
      </View>

      <AppText variant="subheading" style={styles.sectionTitle}>Exercises</AppText>

      <AaharaCard style={styles.exerciseCard} padding="md">
        <ThreeDAsset assetName="dumbbell" size={60} />
        <View style={styles.exerciseInfo}>
          <AppText variant="button">Bench Press</AppText>
          <AppText variant="bodySmall" color="secondary">Chest · Triceps</AppText>
          <AppText variant="caption" color="muted" style={{marginTop: 4}}>4 sets</AppText>
        </View>
        <AppText variant="metric">60 kg × 8</AppText>
      </AaharaCard>

      <AaharaCard style={styles.exerciseCard} padding="md">
        <ThreeDAsset assetName="dumbbell" size={60} />
        <View style={styles.exerciseInfo}>
          <AppText variant="button">Incline Dumbbell Press</AppText>
          <AppText variant="bodySmall" color="secondary">Chest · Shoulders</AppText>
          <AppText variant="caption" color="muted" style={{marginTop: 4}}>3 sets</AppText>
        </View>
        <AppText variant="metric">22 kg × 10</AppText>
      </AaharaCard>

      <AaharaCard style={styles.exerciseCard} padding="md">
        <ThreeDAsset assetName="dumbbell" size={60} />
        <View style={styles.exerciseInfo}>
          <AppText variant="button">Cable Row</AppText>
          <AppText variant="bodySmall" color="secondary">Back · Biceps</AppText>
          <AppText variant="caption" color="muted" style={{marginTop: 4}}>4 sets</AppText>
        </View>
        <AppText variant="metric">50 kg × 10</AppText>
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
  dateLabel: {
    marginTop: 16,
  },
  header: {
    marginBottom: 24,
  },
  heroAssetContainer: {
    alignItems: 'center',
    marginVertical: 20,
  },
  durationBadge: {
    alignSelf: 'flex-start',
    paddingHorizontal: 16,
    paddingVertical: 8,
    borderRadius: 9999,
    backgroundColor: '#F9EBE6', // workoutSoft equivalent
    marginBottom: 32,
  },
  sectionTitle: {
    marginBottom: 16,
  },
  exerciseCard: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 12,
  },
  exerciseInfo: {
    flex: 1,
    marginLeft: 16,
    marginRight: 16,
  }
});
