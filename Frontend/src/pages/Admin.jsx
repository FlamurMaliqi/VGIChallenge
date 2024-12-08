import React from 'react';
import Footer from "../components/Footer";
import BookingList from "../components/BookingList";

function Admin() {
    return (
        <React.Fragment>
            <section>
                <BookingList></BookingList>
            </section>
            <Footer></Footer>
        </React.Fragment>
    )
}

export default Admin;