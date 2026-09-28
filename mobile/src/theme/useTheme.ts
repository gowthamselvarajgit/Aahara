import { useColorScheme } from 'react-native';
import { lightTheme, darkTheme } from './colors';

export const useTheme = () => {
  const colorScheme = useColorScheme();
  const isDark = colorScheme === 'dark';
  return isDark ? darkTheme : lightTheme;
};
