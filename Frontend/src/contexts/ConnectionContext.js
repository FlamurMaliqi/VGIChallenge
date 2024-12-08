import React, {createContext, useContext, useEffect, useState} from 'react';
import * as api from '../api';
import {LoadingContext} from "./LoadingContext";

export const ConnectionContext = createContext();

export const ConnectionContextProvider = ({ children }) => {
    const { setIsLoading, setIsLoadingError, setLoadingMessage } = useContext(LoadingContext);
    const [stops, setStops] = useState([]);
    const [fromStop, setFromStop] = useState({});
    const [fromStopName, setFromStopName] = useState("");
    const [fromStopNameSuggestions, setFromStopNameSuggestions] = useState([]);
    const [toStop, setToStop] = useState({});
    const [toStopName, setToStopName] = useState("");
    const [toStopNameSuggestions, setToStopNameSuggestions] = useState([]);
    const [departure, setDeparture] = useState(new Date());
    const [arrival, setArrival] = useState(new Date());
    const [pax, setPax] = useState(20);
    const [toggle, setToggle] = useState("Abfahrt");
    const [isFirstSearch, setIsFirstSearch] = useState(true);
    const [selectedConnection, setSelectedConnection] = useState(null);
    const [connections, setConnections] = useState([]);

    useEffect(() => {
            async function getStops() {
                setIsLoadingError(false);
                setLoadingMessage("Brumm Brumm...");
                setIsLoading(true);
                try {
                    const fetchedStops = await api.getStops();
                    setStops(fetchedStops);
                    setIsLoading(false);
                } catch (error) {
                    setIsLoadingError(true);
                    setLoadingMessage("Ein unerwarteter Fehler ist aufgetreten. Bitte überprüfen Sie Ihre Netzwerkverbindung.")
                    console.log(error);
                }
            }
            getStops();
    }, []);  // Empty dependency array means this runs only once when the component mounts

    async function findConnections(prepend = false) {
        setIsLoadingError(false);
        setLoadingMessage("Verbindungen suchen...")
        setIsLoading(true);
        const connections = await api.findConnections(fromStop, toStop, departure, arrival, pax);

        // Sort the new connections based on the departure time (ascending)
        const sortedConnections = connections.sort((a, b) => {
            const aDeparture = new Date(a.from.departure);
            const bDeparture = new Date(b.from.departure);
            return aDeparture - bDeparture; // Ascending order
        });

        // Function to check for duplicates
        const isDuplicate = (newConnection, connectionsList) => {
            return connectionsList.some(existingConnection => {
                return (
                    existingConnection.from.departure === newConnection.from.departure &&
                    existingConnection.to.arrival === newConnection.to.arrival &&
                    existingConnection.from.stopName === newConnection.from.stopName &&
                    existingConnection.to.stopName === newConnection.to.stopName
                );
            });
        };

        // Filter out duplicates from the sorted connections
        const filteredConnections = sortedConnections.filter(connection => {
            return !isDuplicate(connection, prepend ? connections : []);
        });


        if (prepend) {
            // Prepend the new connections to the existing ones
            setConnections((prevConnections) => {
                const combined = [...filteredConnections, ...prevConnections];
                // Sort combined list based on departure time of 'fromStop'
                return combined.sort((a, b) => {
                    const aDeparture = new Date(a.from.departure);
                    const bDeparture = new Date(b.from.departure);
                    return aDeparture - bDeparture; // Ascending order
                });
            });
        } else {
            setConnections(connections);
        }

        if (isFirstSearch) {
            setIsFirstSearch(false);
        }
        setIsLoading(false);
    }

    return (
        <ConnectionContext.Provider
            value={{
                fromStop,
                setFromStop,
                fromStopName,
                setFromStopName,
                fromStopNameSuggestions,
                setFromStopNameSuggestions,
                toStop,
                setToStop,
                toStopName,
                setToStopName,
                toStopNameSuggestions,
                setToStopNameSuggestions,
                departure,
                setDeparture,
                arrival,
                setArrival,
                pax,
                setPax,
                toggle,
                setToggle,
                stops,
                isFirstSearch,
                setIsFirstSearch,
                connections,
                setConnections,
                selectedConnection,
                setSelectedConnection,
                findConnections
            }}
        >
            {children}
        </ConnectionContext.Provider>
    );
};
