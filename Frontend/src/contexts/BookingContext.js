import React, {createContext, useContext, useState} from 'react';
import * as api from "../api";
import {LoadingContext} from "./LoadingContext";
import {ConnectionContext} from "./ConnectionContext";

export const BookingContext = createContext();

export const BookingContextProvider = ({ children }) => {
    const { setIsLoading, setLoadingMessage, setIsLoadingError } = useContext(LoadingContext);
    const { pax, selectedConnection } = useContext(ConnectionContext);
    const [contact, setContact] = useState({
        email: "",
        phone: "",
        firstName: "",
        lastName: "",
        institution: "",
    });

    async function bookConnection() {
        setIsLoading(true);
        const booking = await api.bookConnection(selectedConnection, pax, contact);
        if (booking != null) {
            setIsLoadingError(false);
            setLoadingMessage("Ihre Buchung wurde erfolgreich gespeichert. Sie werden in Kürze eine Bestätigung per Email erhalten.")
        } else {
            setIsLoadingError(true);
            setLoadingMessage("Ein Fehler beim erstellen Ihrer Buchung ist aufgetreten. Womöglich ist die gewählte Verbindung nicht mehr verfügbar. Bitte versuchen Sie es erneut.");
        }
    }

    return (
        <BookingContext.Provider value={{ contact, setContact, bookConnection }}>
            {children}
        </BookingContext.Provider>
    );
};