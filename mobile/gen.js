const fs = require('fs');

const screens = ['HomeScreen', 'FoodScreen', 'WorkoutScreen', 'ProgressScreen', 'ProfileScreen'];

screens.forEach(screen => {
  const content = `import React from 'react';
import { View, StyleSheet } from 'react-native';
import { AppText } from '../components/AppText';
import { useTheme } from '../theme/useTheme';

export const ${screen} = () => {
  const theme = useTheme();
  return (
    <View style={[styles.container, { backgroundColor: theme.background }]}>
      <AppText variant="h1">${screen}</AppText>
      <AppText variant="body" color="muted">Foundation scaffold</AppText>
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, alignItems: 'center', justifyContent: 'center', padding: 16 }
});
`;
  fs.writeFileSync(`D:/Aahara/mobile/src/screens/${screen}.tsx`, content);
});
