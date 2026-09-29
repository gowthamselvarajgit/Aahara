import React from 'react';
import { View, StyleSheet, Dimensions } from 'react-native';
import { AppText } from '../components/AppText';
import { useTheme } from '../theme/useTheme';
import { AppButton } from '../components/AppButton';
import { ThreeDAsset } from '../components/ThreeDAsset';

export const OnboardingScreen = ({ navigation }: any) => {
  const theme = useTheme();
  
  return (
    <View style={[styles.container, { backgroundColor: theme.background }]}>
      <View style={styles.heroSection}>
        <ThreeDAsset assetName="idli" size={200} style={styles.asset} />
      </View>
      
      <View style={[styles.contentSection, { backgroundColor: theme.surface, borderTopLeftRadius: theme.radius.xl, borderTopRightRadius: theme.radius.xl }]}>
        <AppText variant="display" style={styles.title}>Aahara</AppText>
        <AppText variant="subheading" color="secondary" style={styles.subtitle}>
          Track your South Indian meals and fitness journey with ease.
        </AppText>
        
        <View style={styles.buttonContainer}>
          <AppButton 
            title="Get Started" 
            onPress={() => navigation.navigate('MainTabs')} 
            size="lg"
            style={styles.button}
          />
        </View>
      </View>
    </View>
  );
};

const { height } = Dimensions.get('window');

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  heroSection: {
    height: height * 0.55,
    justifyContent: 'center',
    alignItems: 'center',
  },
  asset: {
    transform: [{ scale: 1.2 }],
  },
  contentSection: {
    flex: 1,
    padding: 32,
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: -10 },
    shadowOpacity: 0.05,
    shadowRadius: 20,
    elevation: 20,
  },
  title: {
    marginTop: 16,
    marginBottom: 16,
    textAlign: 'center',
  },
  subtitle: {
    textAlign: 'center',
    marginBottom: 48,
    lineHeight: 28,
  },
  buttonContainer: {
    width: '100%',
    marginTop: 'auto',
    marginBottom: 24,
  },
  button: {
    width: '100%',
  }
});
