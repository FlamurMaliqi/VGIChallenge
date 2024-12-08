package bITe.common;

import bITe.entity.Contact;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateBookingRequest(
        @Valid
        Connection connection,

        @Valid
        Contact contact,

        @Positive
        @NotNull(message = "Pax must not be null")
        Integer pax
) {}
