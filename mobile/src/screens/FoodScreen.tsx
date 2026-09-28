import React from 'react';
import { View, StyleSheet, ScrollView, TextInput } from 'react-native';
import { AppText } from '../components/AppText';
import { useTheme } from '../theme/useTheme';
import { AaharaCard } from '../components/AaharaCard';
import { ThreeDAsset } from '../components/ThreeDAsset';

export const FoodScreen = () => {
  const theme = useTheme();
  
  return (
    <ScrollView style={[styles.container, { backgroundColor: theme.background }]} contentContainerStyle={styles.content}>
      <AppText variant="display" style={styles.header}>Food</AppText>
      
      <View style={[styles.searchContainer, { backgroundColor: theme.surfaceMuted, borderRadius: theme.radius.full }]}>
        <TextInput 
          placeholder="Search foods" 
          placeholderTextColor={theme.textMuted}
          style={[styles.searchInput, { color: theme.textPrimary }]}
        />
      </View>

      <AppText variant="subheading" style={styles.sectionTitle}>Recent foods</AppText>
      
      <AaharaCard style={styles.foodCard} padding="md">
        <ThreeDAsset assetName="idli" size={70} />
        <View style={styles.foodInfo}>
          <AppText variant="button">Idli</AppText>
          <AppText variant="bodySmall" color="secondary">இட்லி</AppText>
        </View>
      </AaharaCard>

      <AaharaCard style={styles.foodCard} padding="md">
        <ThreeDAsset assetName="dosa" size={70} />
        <View style={styles.foodInfo}>
          <AppText variant="button">Dosa</AppText>
          <AppText variant="bodySmall" color="secondary">தோசை</AppText>
        </View>
      </AaharaCard>

      <AaharaCard style={styles.foodCard} padding="md">
        <ThreeDAsset assetName="pongal" size={70} />
        <View style={styles.foodInfo}>
          <AppText variant="button">Pongal</AppText>
          <AppText variant="bodySmall" color="secondary">பொங்கல்</AppText>
        </View>
      </AaharaCard>

      <AppText variant="subheading" style={styles.sectionTitle}>Categories</AppText>
      <View style={styles.categories}>
        {['Breakfast', 'Lunch', 'Dinner', 'Snacks', 'Drinks'].map((cat) => (
          <View key={cat} style={[styles.categoryBadge, { backgroundColor: theme.surfaceElevated, borderRadius: theme.radius.full }]}>
            <AppText variant="bodySmall">{cat}</AppText>
          </View>
        ))}
      </View>
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
  searchContainer: {
    paddingHorizontal: 20,
    paddingVertical: 12,
    marginBottom: 32,
  },
  searchInput: {
    fontSize: 16,
  },
  sectionTitle: {
    marginBottom: 16,
    marginTop: 8,
  },
  foodCard: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 12,
  },
  foodInfo: {
    flex: 1,
    marginLeft: 16,
  },
  categories: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 12,
  },
  categoryBadge: {
    paddingHorizontal: 16,
    paddingVertical: 8,
  }
});
