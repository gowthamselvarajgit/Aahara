import React from 'react';
import { View, StyleSheet, ScrollView } from 'react-native';
import { AppText } from '../components/AppText';
import { useTheme } from '../theme/useTheme';
import { AaharaCard } from '../components/AaharaCard';
import { AppButton } from '../components/AppButton';

export const ProfileScreen = () => {
  const theme = useTheme();
  
  const renderSettingRow = (label: string) => (
    <View style={[styles.settingRow, { borderBottomColor: theme.border }]}>
      <AppText variant="body">{label}</AppText>
      <AppText variant="body" color="muted">→</AppText>
    </View>
  );

  return (
    <ScrollView style={[styles.container, { backgroundColor: theme.background }]} contentContainerStyle={styles.content}>
      <View style={styles.header}>
        <View style={[styles.avatar, { backgroundColor: theme.surfaceElevated }]} />
        <AppText variant="heading" style={styles.name}>Gowtham</AppText>
        <AppText variant="body" color="secondary">Nutrition & Fitness</AppText>
      </View>

      <AaharaCard style={styles.card} padding="lg">
        {renderSettingRow('Your profile')}
        {renderSettingRow('Nutrition targets')}
        {renderSettingRow('Workout settings')}
        {renderSettingRow('Units')}
      </AaharaCard>

      <AaharaCard style={styles.card} padding="lg">
        {renderSettingRow('Appearance')}
        {renderSettingRow('Account')}
        {renderSettingRow('Privacy')}
      </AaharaCard>

      <AppButton title="Log Out" onPress={() => {}} variant="outline" />
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
    alignItems: 'center',
    marginBottom: 32,
    marginTop: 16,
  },
  avatar: {
    width: 100,
    height: 100,
    borderRadius: 50,
    marginBottom: 16,
  },
  name: {
    marginBottom: 4,
  },
  card: {
    marginBottom: 24,
  },
  settingRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingVertical: 16,
    borderBottomWidth: 1,
  }
});
