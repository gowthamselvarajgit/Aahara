import React from 'react';
import { Image, ImageProps, View, StyleSheet, ActivityIndicator } from 'react-native';
import { useTheme } from '../theme/useTheme';

interface ThreeDAssetProps extends Omit<ImageProps, 'source'> {
  assetName: 'idli' | 'dosa' | 'pongal' | 'water' | 'dumbbell';
  size?: number;
  width?: number;
  height?: number;
}

const assets = {
  idli: require('../assets/images/idli.jpg'),
  dosa: require('../assets/images/dosa.jpg'),
  pongal: require('../assets/images/pongal.jpg'),
  water: require('../assets/images/water.jpg'),
  dumbbell: require('../assets/images/dumbbell.jpg'),
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
