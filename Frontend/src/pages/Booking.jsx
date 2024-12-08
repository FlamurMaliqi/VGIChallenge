import React, {useContext} from 'react';
import Footer from "../components/Footer";
import {LoadingContext} from "../contexts/LoadingContext";
import {ConnectionContext} from "../contexts/ConnectionContext";
import Connection from "../components/Connection";
import Loader from "../components/Loader";
import ContactForm from "../components/ContactForm";
import '../css/booking.css';
import {Link} from "react-router-dom";
import HomeLogo from "../components/HomeLogo";

function Booking() {
    const { isLoading } = useContext(LoadingContext);
    const { selectedConnection, setSelectedConnection } = useContext(ConnectionContext);

    return (
        <React.Fragment>
            <section className="max-width-1800px">
                <div className="horizontal-wrapper flex-start nav">
                    <HomeLogo></HomeLogo>
                    <Link to="/" className="button button-secondary back-button">Zurück</Link>
                </div>
                { selectedConnection !== null ? (
                    <div className="booking-wrapper">
                        <div>
                            <h1>Gewählte Verbindung</h1>
                            <Connection connection={selectedConnection} setSelectedConnection={setSelectedConnection}
                                        isBookingView={true}></Connection>
                        </div>
                        <ContactForm buttonText={"Jetzt buchen"}></ContactForm>
                    </div>
                ) : (
                    <Loader msg={"Bitte wählen Sie eine Verbindung aus."} isError={true}></Loader>
                )}
                {isLoading && (
                    <Loader isPopup={true}></Loader>
                )}
            </section>
            <Footer></Footer>
        </React.Fragment>
    )
}

export default Booking;