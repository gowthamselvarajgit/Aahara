import React from 'react';
import { Image, ImageProps, View, StyleSheet, ActivityIndicator } from 'react-native';
import { useTheme } from '../theme/useTheme';

interface ThreeDAssetProps extends Omit<ImageProps, 'source'> {
  assetName: 'idli' | 'dosa' | 'pongal' | 'water' | 'dumbbell' | 'chicken_biryani' | 'bench_press' | 'barbell_squat';
  size?: number;
  width?: number;
  height?: number;
}

const assets = {
  idli: require('../assets/images/food/idli.jpg'),
  dosa: require('../assets/images/food/dosa.jpg'),
  pongal: require('../assets/images/pongal.jpg'), // Assuming pongal is in root or we should point it properly. Let's point to food if available, else root. Wait, I'll point to root first.
  water: require('../assets/images/water.jpg'),
  dumbbell: require('../assets/images/dumbbell.jpg'),
  chicken_biryani: require('../assets/images/food/chicken_biryani.jpg'),
  bench_press: require('../assets/images/exercises/bench_press.jpg'),
  barbell_squat: require('../assets/images/exercises/barbell_squat.jpg'),
};

export const ThreeDAsset: React.FC<ThreeDAssetProps> = ({ 
  assetName, 
  size = 120,
  width,
  height,
  style,
  ...props 
}) => {
  const [loading, setLoading] = React.useState(true);
  const theme = useTheme();
  
  const w = width || size;
  const h = height || size;

  return (
    <View style={[styles.container, { width: w, height: h }, style]}>
      {loading && (
        <View style={[styles.loader, { backgroundColor: theme.surfaceMuted, borderRadius: theme.radius.md }]}>
          <ActivityIndicator color={theme.primary} />
        </View>
      )}
      <Image
        source={assets[assetName]}
        style={[
          styles.image, 
          { width: w, height: h, borderRadius: theme.radius.md }
        ]}
        onLoad={() => setLoading(false)}
        {...props}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    overflow: 'hidden',
    position: 'relative',
    justifyContent: 'center',
    alignItems: 'center',
  },
  loader: {
    ...StyleSheet.absoluteFill,
    justifyContent: 'center',
    alignItems: 'center',
    zIndex: 1,
  },
  image: {
    resizeMode: 'cover',
  }
});
