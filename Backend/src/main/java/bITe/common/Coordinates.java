package bITe.common;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record Coordinates(
        @Min(value = 48, message = "Latitude must be between 48 and 50")
        @Max(value = 50, message = "Latitude must be between 48 and 50")
        @NotNull(message = "Latitude name must not be null")
        Double latitude,

        @Min(value = 10, message = "Longitude must be between 10 and 13")
        @Max(value = 13, message = "Longitude must be between 10 and 13")
        @NotNull(message = "Longitude must not be null")
        Double longitude
) {}
