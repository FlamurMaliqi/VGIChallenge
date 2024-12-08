import React from 'react';
import '../css/booking.css';

function Booking({ booking }) {
    return (
        <div className="connection-item vertical-wrapper">
            <div className="vertical-wrapper">
                {/* Contact Information */}
                <div>
                    <h3>Buchung #{booking.bookingId}</h3>
                    <p><strong>Name:</strong> {booking.contact.firstName} {booking.contact.lastName}</p>
                    <p><strong>Email:</strong> {booking.contact.email}</p>
                    <p><strong>Telefon:</strong> {booking.contact.phoneNumber || 'Nicht angegeben'}</p>
                    <p><strong>Institution:</strong> {booking.contact.institution || 'Keine Angabe'}</p>
                </div>

                {/* Route Blocks */}
                {booking.routeBlocks && booking.routeBlocks.map((block, index) => (
                    <React.Fragment key={index}>
                        {/* From Stop Time and Name */}
                        <div className="horizontal-wrapper flex-start stop-info">
                            <span>{new Date(block.departureTime).toLocaleTimeString('de-DE', { hour: '2-digit', minute: '2-digit' })}</span>
                            <span>{block.departureStopName}</span>
                        </div>

                        {/* Route Information */}
                        <div className="vertical-wrapper booking-info">
                            <span className="pill purple">Linie {block.shortName} <span className="arrow">→</span> {block.headSign}</span>
                        </div>

                        {/* To Stop Time and Name */}
                        <div className="horizontal-wrapper flex-start stop-info">
                            <span>{new Date(block.arrivalTime).toLocaleTimeString('de-DE', { hour: '2-digit', minute: '2-digit' })}</span>
                            <span>{block.arrivalStopName}</span>
                        </div>
                    </React.Fragment>
                ))}
            </div>
            {/* Additional Information */}
            <div>
                <p><strong>Personen:</strong> {booking.pax}</p>
                <p><strong>Booking Hash:</strong> {booking.bookingHash}</p>
                <button>Stornieren</button>
            </div>
        </div>
    );
}

export default Booking;
