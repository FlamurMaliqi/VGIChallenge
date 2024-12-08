import React from 'react';
import {useNavigate} from 'react-router-dom';
import '../css/connection.css';


function Connection({ connection, setSelectedConnection, isBookingView }) {

    const navigate = useNavigate();

    const navigateToBooking = (connection) => {
        setSelectedConnection(connection);
        navigate("/buchen");
    };

    return (
        <div className={`connection-item ${connection.isBlocked ? "blocked" : ""}`}>
            <div className="connection-grid">
                {connection.connectionSegments && connection.connectionSegments.map((segment, index) => (
                        <React.Fragment key={index}>
                            {/* From Stop Time and Name */}
                            <div className="dot"></div>
                            <div className="horizontal-wrapper flex-start stop-info">
                                {/* span className="pill time" */}
                                <span>
                                    {new Date(segment.fromStop.departure).toLocaleTimeString('de-DE', {
                                        hour: '2-digit',
                                        minute: '2-digit'
                                    })}
                                </span>
                                {/* span className="pill stop-name" */}
                                <span>{segment.fromStop.stopName}</span>
                            </div>

                            {/* Route/Bus Name, Direction and expected Occupation */}
                            <div className="line"></div>
                            <div className="vertical-wrapper connection-info">
                                <div className="horizontal-wrapper">
                                    <span className="pill purple">Linie {segment.routeShortName} <span className="arrow">→</span> {segment.tripHeadsign}</span>
                                    {segment.isBlocked && (
                                        <span
                                            className="pill red">Nicht verfügbar</span>
                                    )}
                                </div>
                                {!segment.isBlocked && segment.expectedOccupation > 0 && (
                                    <span className={`pill ${segment.expectedOccupation >= 70 ? 'red' : ''}`}>
                                    Erwartete Auslastung: {segment.expectedOccupation}%</span>
                                )}

                            </div>

                            {/* To Stop Time and Name */}
                            <div className="dot"></div>
                            <div className="horizontal-wrapper flex-start stop-info">
                                {/* span className="pill time" */}
                                <span>
                                    {new Date(segment.toStop.arrival).toLocaleTimeString('de-DE', {
                                        hour: '2-digit',
                                        minute: '2-digit'
                                    })}
                                </span>
                                {/* span className="pill stop-name" */}
                                <span>{segment.toStop.stopName}</span>
                            </div>
                        </React.Fragment>
                    ))
                }
            </div>
            {/* Buttons */}
            { !isBookingView && (
                <div className="horizontal-wrapper connection-button-wrapper">
                    <button
                        className="connection-button"
                        disabled={connection.isBlocked}
                        onClick={() => navigateToBooking(connection)}
                    >
                        {connection.isBlocked ? 'Nicht verfügbar' : 'Buchen'}
                    </button>
                </div>
            )}
        </div>
    );
}

export default Connection;
