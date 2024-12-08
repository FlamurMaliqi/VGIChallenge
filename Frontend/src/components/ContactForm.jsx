import React, {useContext} from 'react';
import { BookingContext } from '../contexts/BookingContext';
import {ConnectionContext} from "../contexts/ConnectionContext";
import FormInput from "./FormInput";

const ContactForm = ({ buttonText }) => {
    const { contact, setContact, bookConnection } = useContext(BookingContext);
    const { pax, setPax } = useContext(ConnectionContext);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setContact((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };

    const handlePaxChange = (e) => {
        setPax(e.target.value);
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        bookConnection()
    }

    return (
        <form className="form" onSubmit={handleSubmit}>
            <FormInput
                label="Vorname*"
                type="text"
                name="firstName"
                value={contact.firstName}
                onChange={handleInputChange}
                placeholder="Vorname"
                required={true}
            />
            <FormInput
                label="Nachname*"
                type="text"
                name="lastName"
                value={contact.lastName}
                onChange={handleInputChange}
                placeholder="Nachname"
                required={true}

            />
            <FormInput
                label="Email*"
                type="email"
                name="email"
                value={contact.email}
                onChange={handleInputChange}
                placeholder="Email"
                required={true}
            />
            <FormInput
                label="Telefonnummer"
                type="tel"
                name="phone"
                value={contact.phone}
                onChange={handleInputChange}
                placeholder="Telefonnummer"
            />
            <FormInput
                label="Institution*"
                type="text"
                name="institution"
                value={contact.institution}
                onChange={handleInputChange}
                placeholder="Institution"
                required={true}
            />
            <FormInput
                label="Personen*"
                type="text"
                name="pax"
                value={pax}
                onChange={handlePaxChange}
                placeholder="20"
                required={true}
            />

            <button type="submit">{ buttonText }</button>
        </form>
    );
};

export default ContactForm;
