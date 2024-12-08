package bITe.common;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DeleteBookingRequest(
        @NotBlank(message = "Booking hash must not be null")
        String bookingHash,

        @Email(message = "Email must be valid")
        String email
) {}
