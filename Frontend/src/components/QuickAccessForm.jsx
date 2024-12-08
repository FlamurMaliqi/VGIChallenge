import React, {useContext} from 'react';
import DatePicker, {registerLocale} from "react-datepicker";
import { ConnectionContext } from "../contexts/ConnectionContext";
import '../css/quick-access-form.css';
import "react-datepicker/dist/react-datepicker.css";
import de from "date-fns/locale/de";
import FormInput from "./FormInput";

function QuickAccessForm() {
    const {
        setFromStop,
        fromStopName,
        setFromStopName,
        fromStopNameSuggestions,
        setFromStopNameSuggestions,
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
        findConnections
    } = useContext(ConnectionContext);

    const minInputLength = 3;

    registerLocale("de", de)

    const handleFromChange = (e) => {
        const input = e.target.value;
        setFromStopName(input);

        if (input.length >= minInputLength) {
            // Convert the stops object into an array of stop names
            const stopNames = Object.keys(stops);
            // Filter the stop names based on the input
            const filteredStops = stopNames.filter((stop) =>
                stop.toLowerCase().includes(input.toLowerCase())
            );
            setFromStopNameSuggestions(filteredStops);
        } else {
            setFromStopNameSuggestions([]);
        }
    };

    const handleFromSuggestionClick = (stopName) => {
        setFromStopName(stopName);
        const stop = stops[stopName];
        if (stop) {
            setFromStop({
                [stopName]: {
                    latitude: stop.latitude,
                    longitude: stop.longitude
                }
            });
        } else {
            console.error(`Stop "${stopName}" not found in stops data.`);
        }
        setFromStopNameSuggestions([]);
    };

    const handleToChange = (e) => {
        const input = e.target.value;
        setToStopName(input);

        if (input.length >= minInputLength) {
            // Convert the stops object into an array of stop names
            const stopNames = Object.keys(stops);
            // Filter the stop names based on the input
            const filteredStops = stopNames.filter((stop) =>
                stop.toLowerCase().includes(input.toLowerCase())
            );
            setToStopNameSuggestions(filteredStops);
        } else {
            setToStopNameSuggestions([]);
        }
    };

    const handleToSuggestionClick = (stopName) => {
        setToStopName(stopName);
        const stop = stops[stopName];
        if (stop) {
            setToStop({
                [stopName]: {
                    latitude: stop.latitude,
                    longitude: stop.longitude
                }
            });
        } else {
            console.error(`Stop "${stopName}" not found in stops data.`);
        }
        setToStopNameSuggestions([]);
    };

    const handlePaxChange = (e) => {
        setPax(e.target.value);
    };

    const handleToggleChange = (e) => {
        setToggle(e.target.value);
    };

    const handleSetDeparture = (date) => {
        setArrival(null)
        setDeparture(date);
    }

    const handleSetArrival = (date) => {
        setDeparture(null)
        setArrival(date);
    }

    const handleSubmit = (e) => {
        e.preventDefault();
        findConnections();
    };

    return (
        <div id="quick-access">
            <form id="quick-access-form" onSubmit={handleSubmit}>
                <FormInput
                    label="Abfahrtsort"
                    type="text"
                    name="fromStopName"
                    id="fromStopName"
                    placeholder="Abfahrtsort"
                    value={fromStopName}
                    onChange={handleFromChange}
                    required={true}
                    suggestions={fromStopNameSuggestions}
                    onSuggestionClick={handleFromSuggestionClick}
                />

                <FormInput
                    label="Ankunftsort"
                    type="text"
                    name="toStopName"
                    id="toStopName"
                    placeholder="Ankunftsort"
                    value={toStopName}
                    onChange={handleToChange}
                    required={true}
                    suggestions={toStopNameSuggestions}
                    onSuggestionClick={handleToSuggestionClick}
                />

                <div className="form-input-wrapper">
                    <label>Zeit
                        <select
                            className="form-input"
                            value={toggle}
                            onChange={handleToggleChange}
                            id="time"
                        >
                            <option value="Abfahrt">Abfahrt</option>
                            <option value="Ankunft">Ankunft</option>
                        </select>
                    </label>
                </div>

                {toggle === "Abfahrt" && (
                    <div className="form-input-wrapper">
                        <label htmlFor="dateTime">Abfahrtszeit</label>
                        <DatePicker
                            selected={departure}
                            onChange={handleSetDeparture}
                            minDate={new Date()}
                            dateFormat="dd.MM.yyyy HH:mm"
                            showTimeSelect
                            showTimeCaption={false}
                            timeFormat="HH:mm"
                            timeIntervals={15}
                            locale="de"
                            placeholderText="Datum und Uhrzeit"
                            className="form-input datepicker"
                            required
                        />
                    </div>
                )}

                {toggle === "Ankunft" && (
                    <div className="form-input-wrapper">
                        <label htmlFor="dateTime">Ankunftszeit</label>
                        <DatePicker
                            selected={arrival}
                            onChange={handleSetArrival}
                            minDate={new Date()}
                            dateFormat="dd.MM.yyyy HH:mm"
                            showTimeSelect
                            showTimeCaption={false}
                            timeFormat="HH:mm"
                            timeIntervals={15}
                            locale="de"
                            placeholderText="Datum und Uhrzeit"
                            className="form-input datepicker"
                            required
                        />
                    </div>
                )}

                <FormInput
                    label="Personen"
                    type="number"
                    name="pax"
                    id="pax"
                    placeholder="20"
                    value={pax}
                    onChange={handlePaxChange}
                    required={true}
                />

                <input type="submit" value="Verbindungen suchen" className="submit button"/>
            </form>
        </div>
    );
}

export default QuickAccessForm;
