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

      <AppText variant="subheading" style={styles.sectionTitle}>Categories</AppText>
      <ScrollView horizontal showsHorizontalScrollIndicator={false} style={styles.categoriesScroll}>
        <View style={styles.categories}>
          {['Breakfast', 'Lunch', 'Dinner', 'Snacks', 'Drinks'].map((cat, index) => (
            <View 
              key={cat} 
              style={[
                styles.categoryBadge, 
                { 
                  backgroundColor: index === 0 ? theme.primary : theme.surfaceElevated, 
                  borderRadius: theme.radius.full,
                  borderColor: index === 0 ? theme.primary : theme.border,
                  borderWidth: index === 0 ? 0 : 1,
                }
              ]}
            >
              <AppText variant="bodySmall" style={{ color: index === 0 ? '#FFF' : theme.textPrimary }}>
                {cat}
              </AppText>
            </View>
          ))}
        </View>
      </ScrollView>

      <AppText variant="subheading" style={styles.sectionTitle}>Recent foods</AppText>
      
      <AaharaCard style={styles.foodCard} padding="md">
        <ThreeDAsset assetName="idli" size={70} />
        <View style={styles.foodInfo}>
          <AppText variant="button">Idli</AppText>
          <AppText variant="bodySmall" color="secondary">இட்லி</AppText>
        </View>
        <AppText variant="metric" color="nutrition">60 <AppText variant="caption" color="muted">kcal</AppText></AppText>
      </AaharaCard>

      <AaharaCard style={styles.foodCard} padding="md">
        <ThreeDAsset assetName="dosa" size={70} />
        <View style={styles.foodInfo}>
          <AppText variant="button">Dosa</AppText>
          <AppText variant="bodySmall" color="secondary">தோசை</AppText>
        </View>
        <AppText variant="metric" color="nutrition">130 <AppText variant="caption" color="muted">kcal</AppText></AppText>
      </AaharaCard>

      <AaharaCard style={styles.foodCard} padding="md">
        <ThreeDAsset assetName="pongal" size={70} />
        <View style={styles.foodInfo}>
          <AppText variant="button">Pongal</AppText>
          <AppText variant="bodySmall" color="secondary">பொங்கல்</AppText>
        </View>
        <AppText variant="metric" color="nutrition">210 <AppText variant="caption" color="muted">kcal</AppText></AppText>
      </AaharaCard>

      <AaharaCard style={styles.foodCard} padding="md">
        <ThreeDAsset assetName="chicken_biryani" size={70} />
        <View style={styles.foodInfo}>
          <AppText variant="button">Chicken Biryani</AppText>
          <AppText variant="bodySmall" color="secondary">சிக்கன் பிரியாணி</AppText>
        </View>
        <AppText variant="metric" color="nutrition">400 <AppText variant="caption" color="muted">kcal</AppText></AppText>
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
  searchContainer: {
    paddingHorizontal: 20,
    paddingVertical: 12,
    marginBottom: 24,
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
  categoriesScroll: {
    marginHorizontal: -24,
    marginBottom: 24,
  },
  categories: {
    flexDirection: 'row',
    paddingHorizontal: 24,
    gap: 12,
  },
  categoryBadge: {
    paddingHorizontal: 20,
    paddingVertical: 10,
  }
});
