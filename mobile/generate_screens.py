import os

screens = ['HomeScreen', 'FoodScreen', 'WorkoutScreen', 'ProgressScreen', 'ProfileScreen']

template = """import React from 'react';
import { View, StyleSheet } from 'react-native';
import { AppText } from '../components/AppText';
import { useTheme } from '../theme/useTheme';

export const {name} = () => {{
  const theme = useTheme();
  
  return (
    <View style={[styles.container, {{ backgroundColor: theme.background }}]}>
      <AppText variant="h1">{name}</AppText>
      <AppText variant="body" color="muted">Foundation scaffold</AppText>
    </View>
  );
}};

const styles = StyleSheet.create({{
  container: {{
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    padding: 16
  }}
}});
"""

for screen in screens:
    with open(f"D:/Aahara/mobile/src/screens/{screen}.tsx", "w") as f:
        f.write(template.format(name=screen))

print("Created screens.")
