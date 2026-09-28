import NetInfo from '@react-native-community/netinfo';
import { useEffect, useState } from 'react';

export const useNetworkStatus = () => {
  const [isOnline, setIsOnline] = useState<boolean>(true);

  useEffect(() => {
    const unsubscribe = NetInfo.addEventListener(state => {
      setIsOnline(!!state.isConnected && !!state.isInternetReachable !== false);
    });
    
    // Initial fetch
    NetInfo.fetch().then(state => {
      setIsOnline(!!state.isConnected && !!state.isInternetReachable !== false);
    });

    return () => {
      unsubscribe();
    };
  }, []);

  return { isOnline };
};
