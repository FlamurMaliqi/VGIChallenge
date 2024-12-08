import React, {createContext, useContext, useEffect, useState} from 'react';
import * as api from '../api';
import {LoadingContext} from "./LoadingContext";

export const AdminContext = createContext();

export const AdminContextProvider = ({ children }) => {
    const { setIsLoading, setIsLoadingError, setLoadingMessage } = useContext(LoadingContext);
    const [bookings, setBookings] = useState([]);

    useEffect(() => {
        async function getBookings() {
            setIsLoadingError(false);
            setLoadingMessage("Buchungen ab dem heutigem Datum werden geladen...");
            setIsLoading(true);
            try {
                setLoadingMessage("Buchungen ab dem heutigem Datum werden geladen...");
                const fetchedBookings = await api.getBookings();
                if (fetchedBookings == null) {
                    setIsLoadingError(true);
                    setLoadingMessage("Buchungen konnten nicht geladen werden...");
                } else {
                    setBookings(fetchedBookings);
                    setIsLoading(false);
                }
            } catch (error) {
                setIsLoadingError(true);
                setLoadingMessage("Ein unerwarteter Fehler ist aufgetreten. Bitte überprüfen Sie Ihre Netzwerkverbindung.")
                console.log(error);
            }
        }
        getBookings();
    }, []);  // Empty dependency array means this runs only once when the component mounts


    return (
        <AdminContext.Provider value={{bookings, setBookings}}>
            {children}
        </AdminContext.Provider>
    );
};
