package bITe.common;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public record ConnectionStop(
        @NotBlank(message = "Stop name must not be null")
        String stopName,

        ZonedDateTime departure,

        ZonedDateTime arrival,

        @Valid
        Coordinates coordinates
) {
    private static final ZoneId BERLIN_TIME_ZONE = ZoneId.of("Europe/Berlin");

    public ConnectionStop {
        // Ensure both departure and arrival are in Europe/Berlin timezone
        if (departure != null) {
            departure = departure.withZoneSameInstant(BERLIN_TIME_ZONE);
        }
        if (arrival != null) {
            arrival = arrival.withZoneSameInstant(BERLIN_TIME_ZONE);
        }
    }

    public static class Builder {
        private String stopName;
        private ZonedDateTime departure;
        private ZonedDateTime arrival;
        private Coordinates coordinates;

        public Builder setStopName(String stopName) {
            this.stopName = stopName;
            return this;
        }

        public Builder setDeparture(ZonedDateTime departure) {
            if (departure != null) {
                this.departure = departure.withZoneSameInstant(BERLIN_TIME_ZONE);
            }
            return this;
        }

        public Builder setArrival(ZonedDateTime arrival) {
            if (arrival != null) {
                this.arrival = arrival.withZoneSameInstant(BERLIN_TIME_ZONE);
            }
            return this;
        }

        public Builder setCoordinates(Coordinates coordinates) {
            this.coordinates = coordinates;
            return this;
        }

        public Builder reset() {
            this.stopName = null;
            this.departure = null;
            this.arrival = null;
            this.coordinates = null;
            return this;
        }

        public ConnectionStop build() {
            ConnectionStop connectionStop =  new ConnectionStop(stopName, departure, arrival, coordinates);
            reset();
            return connectionStop;
        }
    }
}