import React, {useContext, useRef, useState} from 'react';
import '../css/connection-list.css';
import Loader from "./Loader";
import {LoadingContext} from "../contexts/LoadingContext";
import {ConnectionContext} from "../contexts/ConnectionContext";
import Connection from "./Connection";

function ConnectionList() {
    const { isLoading } = useContext(LoadingContext);
    const { isFirstSearch, departure, setDeparture, arrival, setArrival, connections, setConnections, setSelectedConnection, findConnections} = useContext(ConnectionContext);
    const [isSorted, setIsSorted] = useState(false);
    const [isHideBlocked, setIsHideBlocked] = useState(false);

    const originalConnections = useRef(connections); // Store the original order using useRef

    const toggleSortByOccupation = () => {
        if (isSorted) {
            // If already sorted, restore original order
            setConnections([...originalConnections.current]);
        } else {
            // Sort by expectedOccupation ascending
            // Sort by the highest expected occupation within each connection
            setConnections([...connections].sort((a, b) => {
                const maxOccupationA = Math.max(...a.connectionSegments.map(seg => seg.expectedOccupation));
                const maxOccupationB = Math.max(...b.connectionSegments.map(seg => seg.expectedOccupation));
                return maxOccupationA - maxOccupationB;
            }));
        }
        setIsSorted(!isSorted); // Toggle the state
    };

    const toggleHideBooked = () => {
        setIsHideBlocked(!isHideBlocked);
    };

    const fetchEarlierConnections = () => {
        // TODO FIX
        if (departure != null) {
            const departureDate = new Date(departure);
            departureDate.setHours(departureDate.getHours() - 1);
            setDeparture(departureDate);
            // Call API
            findConnections(true)
        } else if (arrival != null) {
            const arrivalDate = new Date(arrival);
            arrivalDate.setHours(arrivalDate.getHours() - 1);
            setArrival(arrivalDate);
            // Call API
            findConnections(true)
        }
    };

    return (
        <div className="connection-list">
            <div className="horizontal-wrapper">
                <div className="horizontal-wrapper">
                    <img className="vgi-logo-mark" src={`${process.env.PUBLIC_URL}/images/vgi-logo-mark.svg`} alt="VGI Logo"/>
                    <h2>Verbindungen</h2>
                </div>
                <div className="horizontal-wrapper">
                    <button className={`button-secondary ${isHideBlocked ? 'active' : ''}`}
                            disabled={connections.length === 0}
                            onClick={toggleHideBooked}>Nur buchbar
                    </button>
                    <button className={`button-secondary ${isSorted ? 'active' : ''}`}
                            disabled={connections.length === 0}
                            onClick={toggleSortByOccupation}>Geringste Auslastung
                    </button>
                    <button className="button-secondary" disabled={isFirstSearch} onClick={fetchEarlierConnections}>Früher</button>
                </div>
            </div>
            <hr></hr>
            {isLoading && (
                <Loader></Loader>
            )}
            {/* Check if there are no connections after filtering */}
            {((connections.length === 0 && !isFirstSearch)  ||
            (connections.filter(connection => !isHideBlocked || !connection.isBlocked).length === 0)) && !isFirstSearch ? (
                <Loader msg={"Keine verfügbaren Verbindungen gefunden. Bitte versuchen Sie einen anderen Zeitraum oder andere Haltestellen."} isError={true}></Loader>
            ) : (
                <ul>
                    {connections
                        .filter(connection => !isHideBlocked || !connection.isBlocked)
                        .map((connection, index) => (
                            <li key={index}>
                                <Connection connection={connection} setSelectedConnection={setSelectedConnection} isBookingView={false} />
                            </li>
                        ))
                    }
                </ul>
            )}
        </div>
    );
}

export default ConnectionList;