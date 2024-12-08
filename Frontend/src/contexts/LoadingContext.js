import React, {createContext, useState} from 'react';

export const LoadingContext = createContext();

// Loading Provider component
export const LoadingProvider = ({ children }) => {
    const [isLoading, setIsLoading] = useState(false);
    const [loadingMessage, setLoadingMessage] = useState("Brumm Brumm...");
    const [isLoadingError, setIsLoadingError] = useState(false);


    return (
        <LoadingContext.Provider value={{ isLoading, setIsLoading, loadingMessage, setLoadingMessage, isLoadingError, setIsLoadingError }}>
            {children}
        </LoadingContext.Provider>
    );
};