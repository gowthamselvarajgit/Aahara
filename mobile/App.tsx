import React, { useEffect } from 'react';
import { SafeAreaProvider } from 'react-native-safe-area-context';
import { NavigationContainer, DefaultTheme, DarkTheme as NavDarkTheme } from '@react-navigation/native';
import { useColorScheme, View } from 'react-native';
import { RootNavigator } from './src/navigation/RootNavigator';
import { initDb } from './src/database/sqlite/db';
import { useAuthStore } from './src/store/useAuthStore';
import { AppText } from './src/components/AppText';

const App = () => {
  const isDarkMode = useColorScheme() === 'dark';
  const { isLoading, checkAuth } = useAuthStore();
  const [dbInitialized, setDbInitialized] = React.useState(false);

  useEffect(() => {
    const bootstrap = async () => {
      try {
        await initDb();
        setDbInitialized(true);
        await checkAuth();
      } catch (e) {
        console.error('Bootstrap failed', e);
      }
    };
    bootstrap();
  }, [checkAuth]);

  if (!dbInitialized || isLoading) {
    return (
      <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
        <AppText>Loading Aahara...</AppText>
      </View>
    );
  }

  // Real auth routing would go here. For now we are building the authenticated scaffold.
  return (
    <SafeAreaProvider>
      <NavigationContainer theme={isDarkMode ? NavDarkTheme : DefaultTheme}>
        <RootNavigator />
      </NavigationContainer>
    </SafeAreaProvider>
  );
};

export default App;
