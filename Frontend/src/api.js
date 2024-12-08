import moment from "moment-timezone";

const API_URL = process.env.REACT_APP_API_URL;

function formatDate(date) {
    return moment(date).tz("Europe/Berlin", true).format("YYYY-MM-DDTHH:mm:ssZ");
}

export async function getStops() {
    try {
        const response = await fetch(`${API_URL}/v1/connections/stops`);

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(`Failed to fetch stations: ${errorData.message || response.statusText}`);
        }

        return await response.json();
    } catch (error) {
        console.error("Error fetching stations:", error.message);
        return [];
    }
}

export async function getBookings(timestamp = null) {
    try {
        // Build the URL with the optional timestamp parameter
        const url = new URL(`${API_URL}/v1/bookings`);
        url.searchParams.append('timestamp', formatDate(new Date()));

        // Perform the fetch request
        const response = await fetch(url);

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(`Failed to fetch bookings: ${errorData.message || response.statusText}`);
        }

        // Return the bookings as JSON
        return await response.json();
    } catch (error) {
        console.error("Error fetching bookings:", error.message);
        return [];
    }
}


export async function findConnections(fromStop, toStop, departure, arrival, pax) {
    try {
        const response = await fetch(`${API_URL}/v1/connections`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                fromStopName: Object.keys(fromStop)[0],
                fromCoordinates: {
                    latitude: Object.values(fromStop)[0].latitude,
                    longitude: Object.values(fromStop)[0].longitude
                },
                toStopName: Object.keys(toStop)[0],
                toCoordinates: {
                    latitude: Object.values(toStop)[0].latitude,
                    longitude: Object.values(toStop)[0].longitude
                },
                departure: departure,
                arrival: arrival,
                pax: pax
            }),
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(`Failed to fetch connections: ${errorData.message || response.statusText}`);
        }

        return await response.json();
    } catch (error) {
        console.error("Error fetching connections:", error.message);
        return [];
    }
}

export async function bookConnection(connection, pax, contact) {
    try {
        console.log(JSON.stringify({
            connection: connection,
            pax: pax,
            contact: contact
        }))
        const response = await fetch(`${API_URL}/v1/bookings`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                connection: connection,
                pax: pax,
                contact: contact
            }),
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(`Failed to book connection: ${errorData.message || response.statusText}`);
        }

        return await response.json();
    } catch (error) {
        console.error("Error posting bus booking request:", error.message);
        return null;
    }
}

