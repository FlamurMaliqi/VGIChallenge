package bITe.common;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public record ConnectionRequest(
        @NotBlank(message = "From stop name must not be null")
        String fromStopName,

        @Valid
        Coordinates fromCoordinates,

        @NotBlank(message = "To stop name must not be null")
        String toStopName,

        @Valid
        Coordinates toCoordinates,

        ZonedDateTime departure,

        ZonedDateTime arrival,

        int pax
) {
        private static final ZoneId BERLIN_TIME_ZONE = ZoneId.of("Europe/Berlin");

        public ConnectionRequest {
                // Ensure both departure and arrival are in Europe/Berlin timezone
                if (departure != null) {
                        departure = departure.withZoneSameInstant(BERLIN_TIME_ZONE);
                }
                if (arrival != null) {
                        arrival = arrival.withZoneSameInstant(BERLIN_TIME_ZONE);
                }
        }
}
