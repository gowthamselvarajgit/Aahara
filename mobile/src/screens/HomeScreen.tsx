import React from 'react';
import { View, StyleSheet, ScrollView, TouchableOpacity } from 'react-native';
import { AppText } from '../components/AppText';
import { useTheme } from '../theme/useTheme';
import { AaharaCard } from '../components/AaharaCard';
import { ThreeDAsset } from '../components/ThreeDAsset';
import { ProgressBar } from '../components/ProgressBar';

export const HomeScreen = () => {
  const theme = useTheme();
  
  return (
    <ScrollView style={[styles.container, { backgroundColor: theme.background }]} contentContainerStyle={styles.content}>
      <View style={styles.header}>
        <AppText variant="heading">Hello, Gowtham 👋</AppText>
        <AppText variant="body" color="secondary">Monday, 28 September</AppText>
      </View>

      <View style={styles.dateSelector}>
        {['S', 'M', 'T', 'W', 'T', 'F', 'S'].map((day, i) => (
          <TouchableOpacity key={i} style={[styles.dateBubble, i === 1 && { backgroundColor: theme.primary }]}>
            <AppText variant="caption" style={{ color: i === 1 ? '#FFF' : theme.textSecondary }}>{day}</AppText>
            <AppText variant="bodySmall" style={{ color: i === 1 ? '#FFF' : theme.textPrimary }}>{27 + i}</AppText>
          </TouchableOpacity>
        ))}
      </View>

      <AaharaCard style={styles.heroCard} padding="lg">
        <View style={styles.heroContent}>
          <View style={styles.heroText}>
            <AppText variant="caption" color="brand">TODAY</AppText>
            <View style={styles.heroNumber}>
              <AppText variant="largeNumber">1,650</AppText>
              <AppText variant="body" color="muted"> / 2,200 kcal</AppText>
            </View>
            <ProgressBar progress={0.75} style={styles.mainProgress} />
            <AppText variant="bodySmall" color="secondary" style={styles.progressText}>75% Calories logged</AppText>
          </View>
          <ThreeDAsset assetName="idli" size={100} style={styles.heroAsset} />
        </View>
      </AaharaCard>

      <View style={styles.macroRow}>
        <AaharaCard variant="muted" style={styles.macroCard} padding="md">
          <AppText variant="caption" color="secondary">PROTEIN</AppText>
          <View style={styles.macroValue}>
            <AppText variant="metric">82</AppText>
            <AppText variant="caption" color="muted"> / 120 g</AppText>
          </View>
          <ProgressBar progress={82/120} height={4} />
        </AaharaCard>
        
        <AaharaCard variant="muted" style={styles.macroCard} padding="md">
          <AppText variant="caption" color="secondary">CARBS</AppText>
          <View style={styles.macroValue}>
            <AppText variant="metric">180</AppText>
            <AppText variant="caption" color="muted"> / 250 g</AppText>
          </View>
          <ProgressBar progress={180/250} height={4} />
        </AaharaCard>

        <AaharaCard variant="muted" style={styles.macroCard} padding="md">
          <AppText variant="caption" color="secondary">FAT</AppText>
          <View style={styles.macroValue}>
            <AppText variant="metric">55</AppText>
            <AppText variant="caption" color="muted"> / 73 g</AppText>
          </View>
          <ProgressBar progress={55/73} height={4} />
        </AaharaCard>
      </View>

      <AppText variant="subheading" style={styles.sectionTitle}>Today's meals</AppText>
      <AaharaCard style={styles.mealCard} padding="md">
        <ThreeDAsset assetName="idli" size={60} />
        <View style={styles.mealInfo}>
          <AppText variant="button">Breakfast</AppText>
          <AppText variant="bodySmall" color="secondary">3 Idli (இட்லி), Sambar</AppText>
        </View>
        <AppText variant="metric">350</AppText>
      </AaharaCard>

      <AaharaCard style={styles.mealCard} padding="md">
        <ThreeDAsset assetName="pongal" size={60} />
        <View style={styles.mealInfo}>
          <AppText variant="button">Lunch</AppText>
          <AppText variant="bodySmall" color="secondary">Ven Pongal (வெண் பொங்கல்)</AppText>
        </View>
        <AppText variant="metric">420</AppText>
      </AaharaCard>

      <AppText variant="subheading" style={styles.sectionTitle}>Hydration</AppText>
      <AaharaCard style={styles.hydrationCard}>
        <View style={styles.hydrationContent}>
          <View style={styles.hydrationInfo}>
            <AppText variant="largeNumber">1.8<AppText variant="body" color="muted"> / 2.5 L</AppText></AppText>
            <ProgressBar progress={1.8/2.5} color="#4FC3F7" style={styles.waterProgress} />
            <AppText variant="bodySmall" color="secondary">72% of daily goal</AppText>
          </View>
          <ThreeDAsset assetName="water" size={80} />
        </View>
      </AaharaCard>

      <AppText variant="subheading" style={styles.sectionTitle}>Today's workout</AppText>
      <AaharaCard style={styles.workoutCard}>
        <View style={styles.workoutContent}>
          <View>
            <AppText variant="button">Upper Body</AppText>
            <AppText variant="bodySmall" color="secondary">45 min</AppText>
          </View>
          <ThreeDAsset assetName="dumbbell" size={80} />
        </View>
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
  dateSelector: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 24,
  },
  dateBubble: {
    alignItems: 'center',
    paddingVertical: 12,
    paddingHorizontal: 12,
    borderRadius: 24,
  },
  heroCard: {
    marginBottom: 24,
  },
  heroContent: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  heroText: {
    flex: 1,
  },
  heroNumber: {
    flexDirection: 'row',
    alignItems: 'baseline',
    marginVertical: 4,
  },
  mainProgress: {
    marginVertical: 8,
  },
  progressText: {
    marginTop: 4,
  },
  heroAsset: {
    marginLeft: 16,
  },
  macroRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 32,
  },
  macroCard: {
    flex: 1,
    marginHorizontal: 4,
  },
  macroValue: {
    flexDirection: 'row',
    alignItems: 'baseline',
    marginVertical: 8,
  },
  sectionTitle: {
    marginBottom: 16,
    marginTop: 8,
  },
  mealCard: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 12,
  },
  mealInfo: {
    flex: 1,
    marginLeft: 16,
  },
  hydrationCard: {
    marginBottom: 32,
  },
  hydrationContent: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  hydrationInfo: {
    flex: 1,
    marginRight: 16,
  },
  waterProgress: {
    marginVertical: 8,
  },
  workoutCard: {
    marginBottom: 32,
  },
  workoutContent: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
});
