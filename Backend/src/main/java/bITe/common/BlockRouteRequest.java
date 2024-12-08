package bITe.common;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public record BlockRouteRequest(

        @NotBlank(message = "From stop name must not be blank")
        String fromStopName,

        @Valid
        Coordinates fromCoordinates,

        @NotBlank(message = "To stop name must not be blank")
        String toStopName,

        @Valid
        Coordinates toCoordinates,

        @NotNull(message = "Time block from must not be null")
        ZonedDateTime blockFrom,

        @NotNull(message = "Time block to must not be null")
        ZonedDateTime blockTo,

        @NotBlank(message = "Bus short name must not be blank")
        String shortName,

        @NotBlank(message = "Bus head sign must not be blank")
        String headSign
) {
        private static final ZoneId BERLIN_TIME_ZONE = ZoneId.of("Europe/Berlin");

        public BlockRouteRequest {
                // Ensure both departure and arrival are in Europe/Berlin timezone
                if (blockFrom != null) {
                        blockFrom = blockFrom.withZoneSameInstant(BERLIN_TIME_ZONE);
                }
                if (blockTo != null) {
                        blockTo = blockTo.withZoneSameInstant(BERLIN_TIME_ZONE);
                }
        }
}
