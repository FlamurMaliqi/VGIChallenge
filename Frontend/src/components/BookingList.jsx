import React, { useContext, useState } from 'react';
import '../css/connection-list.css';
import Loader from './Loader';
import { LoadingContext } from '../contexts/LoadingContext';
import Booking from './Booking';
import {AdminContext} from "../contexts/AdminContext";

function BookingList() {
    const { isLoading } = useContext(LoadingContext);
    const { bookings, setBookings } = useContext(AdminContext);
    const [isSorted, setIsSorted] = useState(false);

    const toggleSortByPax = () => {
        if (isSorted) {
            // If already sorted, restore original order
            setBookings([...bookings]);
        } else {
            // Sort by pax (ascending)
            setBookings([...bookings].sort((a, b) => a.pax - b.pax));
        }
        setIsSorted(!isSorted);
    };

    return (
        <div className="connection-list">
            <div className="horizontal-wrapper">
                <div className="horizontal-wrapper">
                    <img className="vgi-logo-mark" src={`${process.env.PUBLIC_URL}/images/vgi-logo-mark.svg`} alt="VGI Logo" />
                    <h2>Buchungen</h2>
                </div>
                <div className="horizontal-wrapper">
                    <button className={`button-secondary ${isSorted ? 'active' : ''}`}
                            disabled={bookings.length === 0}
                            onClick={toggleSortByPax}>Nach Passagieren sortieren
                    </button>
                </div>
            </div>
            <hr />
            {isLoading ? (
                <Loader />
            ) : (
                <>
                    {bookings.length === 0 ? (
                        <Loader msg="Keine Buchungen gefunden." isError={true} />
                    ) : (
                        <ul>
                            {bookings.map((booking, index) => (
                                <li key={index}>
                                    <Booking booking={booking} />
                                </li>
                            ))}
                        </ul>
                    )}
                </>
            )}
        </div>
    );
}

export default BookingList;
